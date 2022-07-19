package me.daarkii.databaselib.table

import me.daarkii.databaselib.collection.DataPair
import me.daarkii.databaselib.provider.MongoAdapter
import me.daarkii.databaselib.query.result.ResultEntry
import me.daarkii.databaselib.query.result.ResultQuery
import me.daarkii.databaselib.query.update.InsertQuery
import me.daarkii.databaselib.query.update.UpdateQuery
import org.bson.Document
import java.util.LinkedList

class MongoTable(
    override val name: String,
    override val columns: List<String>,
    mongoAdapter: MongoAdapter
    ) : Table {

    /**
     * Our mongo synonym for our table
     */
    private val collection = mongoAdapter.getCollection(name)

    /**
     * The number of inserted values in this table
     */
    override val count: Int = collection.countDocuments().toInt()

    /**
     * Gets every entry on this table from the given column, for every entry from this table a ResultQuery will be created
     *
     * @param columnName of the column
     * @return a List with Results
     */
    override fun selectEverything(columnName: String): List<ResultQuery> {

        val queries: MutableList<ResultQuery> = LinkedList()

        this.collection.find().forEach {
            val result = it[columnName]
            if(result != null) {
                queries.add(ResultQuery(listOf(ResultEntry(columnName, result))))
            }
        }

        return queries
    }

    /**
     * Gets every entry on this table, for every entry from this table a ResultQuery will be created
     *
     * @return a List with Results
     */
    override fun selectEverything(): List<ResultQuery> {

        val queries: MutableList<ResultQuery> = LinkedList()

        this.collection.find().forEach {

            val entries: MutableList<ResultEntry> = LinkedList()

            it.forEach { key, value -> entries.add(ResultEntry(key, value)) }

            queries.add(ResultQuery(entries))
        }

        return queries
    }

    /**
     * Select all entries from this table
     *
     * @param filter the filters for the select query including name of the column && the value to filter
     * @return a ResultQuery which may be empty
     */
    override fun select(vararg filter: DataPair<Any>): ResultQuery {

        val entries: MutableList<ResultEntry> = LinkedList()

        //build the filter
        val filterDoc = Document()
        filter.iterator().forEach { filterDoc.append(it.key, it.value) }

        //accept the documents and add all values from every document
        collection.find(filterDoc).iterator().forEach { document -> document.forEach { key, value -> entries.add(
            ResultEntry(key, value)
        ) } }

        return ResultQuery(entries)
    }

    /**
     * Select an entry from this table from the column
     *
     * @param columnName the name of the column table
     * @param filter the filters for the select query including name of the column && the value to filter
     * @return a ResultQuery which may be empty
     */
    override fun select(columnName: String, vararg filter: DataPair<Any>): ResultQuery {

        val entries: MutableList<ResultEntry> = LinkedList()

        //build the filter
        val filterDoc = Document()
        filter.iterator().forEach { filterDoc.append(it.key, it.value) }

        //accept the founded document and add values

        val documents = collection.find(filterDoc).iterator()

        documents.forEach { document ->
            if(document != null && document.containsKey(columnName))
                entries.add(ResultEntry(columnName, document[columnName]!!))
        }

        return ResultQuery(entries)
    }

    /**
     * Creates a new entry without an existing check
     *
     * @param query with insert values
     */
    override fun insert(query: InsertQuery) {

        //build document
        val document = Document()
        query.insertable.forEach { document.append(it.key, it.value) }

        //update database
        this.collection.insertOne(document)
    }

    /**
     * Creates a new entry and check if shouldCheck is true if there is something that can be updated
     * if not it will insert the value
     *
     * @param query with the filter and the update values
     * @param shouldCheck if the method should check if there is something that can be updated
     */
    override fun update(query: UpdateQuery, shouldCheck: Boolean) {

        if(shouldCheck && query.filter != null && !this.checkExisting(*query.filter.toTypedArray())) {
            this.insert(InsertQuery(query.updatable))
            return
        }

        val filter = Document()

        if(query.filter != null) {
            query.filter.forEach { filter.append(it.key, it.value) }
        }

        var current = Document()
        val found = this.collection.find(filter).first()

        if(found != null)
            current = found

        query.updatable.forEach {
            if(current.containsKey(it.key))
                current.replace(it.key, it.value)
            else
                current.append(it.key, it.value)
        }

        this.collection.replaceOne(filter, current)
    }

}