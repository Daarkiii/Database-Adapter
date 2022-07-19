package me.daarkii.databaselib.table

import me.daarkii.databaselib.collection.DataPair
import me.daarkii.databaselib.provider.MySqlAdapter
import me.daarkii.databaselib.query.result.ResultEntry
import me.daarkii.databaselib.query.result.ResultQuery
import me.daarkii.databaselib.query.update.InsertQuery
import me.daarkii.databaselib.query.update.UpdateQuery
import java.util.LinkedList

class MySqlTable(
    override val columns: List<String>,
    override val name: String,
    private val mySqlAdapter: MySqlAdapter
    ) : Table {

    /**
     * Select all entries from this table
     *
     * @param filter the filters for the select query including name of the column && the value to filter
     * @return a ResultQuery which may be empty
     */
    override fun select(vararg filter: DataPair<Any>): ResultQuery {

        val filters = filter.toList()
        val builder = StringBuilder().append("SELECT * FROM $name WHERE ")
        val entries: MutableList<ResultEntry> = LinkedList()

        for(i in filters.indices) {

            builder.append("${filters[i].key} = ?")

            if(i != filters.lastIndex)
                builder.append(", ")
        }

        mySqlAdapter.dataSource.connection.use { it.prepareStatement(builder.toString()).use { ps ->

            for(i in filters.indices)
                ps.setObject(i, filters[i])

            val result = ps.executeQuery()

            while (result.next()) {
                val metaData = result.metaData

                for(i in 0 until metaData.columnCount)
                    entries.add(ResultEntry(metaData.getTableName(i), result.getObject(i)))
            }
        } }

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

        val filters = filter.toList()
        val builder = StringBuilder().append("SELECT $columnName FROM $name WHERE ")
        val entries: MutableList<ResultEntry> = LinkedList()

        for(i in filters.indices) {

            builder.append("${filters[i].key} = ?")

            if(i != filters.lastIndex)
                builder.append(", ")
        }

        mySqlAdapter.dataSource.connection.use { it.prepareStatement(builder.toString()).use { ps ->

            for(i in filters.indices)
                ps.setObject(i, filters[i])

            val result = ps.executeQuery()

            while (result.next())
                entries.add(ResultEntry(columnName, result.getObject(columnName)))
        } }

        return ResultQuery(entries)
    }

    /**
     * Creates a new entry without an existing check
     *
     * @param query with insert values
     */
    override fun insert(query: InsertQuery) {

        val first = StringBuilder().append("INSERT INTO $name (")
        val second = StringBuilder().append(" VALUES (")

        for(i in query.insertable.indices) {
            first.append(query.insertable[i].key)
            first.append("?")

            if(i != query.insertable.lastIndex) {
                first.append(", ")
                second.append(", ")
            }
        }

        first.append(")")
        second.append(")")

        val final = first.append(second).toString()

        mySqlAdapter.dataSource.connection.use { it.prepareStatement(final).use { ps ->

            for(i in query.insertable.indices) {
                ps.setObject(i, query.insertable[i].value)
            }

            ps.executeUpdate()
        } }
    }

    /**
     * Creates a new entry and check if shouldCheck is true if there is something that can be updated
     * if not it will insert the value
     *
     * @param query with the filter and the update values
     * @param shouldCheck if the method should check if there is something that can be updated
     */
    override fun update(query: UpdateQuery, shouldCheck: Boolean) {

        //Insert the values instead updating it if there was no result which can be updated
        if(shouldCheck && query.filter != null && !this.checkExisting(*query.filter.toTypedArray())) {
            this.insert(InsertQuery(query.filter))
            return
        }

        val update = StringBuilder().append("UPDATE $name SET ")
        val where = StringBuilder()

        //finish update builder
        for(i in query.updatable.indices) {
            update.append("${query.updatable[i].key} = ?")

            if(i != query.updatable.lastIndex)
                update.append(", ")
        }

        //finish where builder if needed
        if(!query.filter.isNullOrEmpty()) {
            where.append(" WHERE ")

            for(i in query.filter.indices) {

                where.append("${query.filter[i].key} = ?")

                if(i != query.filter.lastIndex)
                    where.append(" AND ")
            }
        }

        //build final String
        val final = update.append(where).toString()

        //execute mysql query
        mySqlAdapter.dataSource.connection.use { it.prepareStatement(final).use { ps ->

            var count = 0

            //set update values
            for(i in query.updatable.indices) {
                count++
                ps.setObject(count, query.updatable[i].value)
            }

            //set filter values
            if(query.filter != null) {
                for(i in query.filter.indices) {
                    count++
                    ps.setObject(count, query.filter[i].value)
                }
            }

            ps.executeUpdate()
        } }
    }

    /**
     * The number of inserted values in this table
     */
    override val count: Int
        get(){
            mySqlAdapter.dataSource.connection.use { it.prepareStatement("SELECT COUNT(*) FROM $name").use { ps ->
                val result = ps.executeQuery()

                if(result.next())
                    return result.getInt(1)
            } }

            return 0
        }

    /**
     * Gets every entry on this table from the given column, for every entry from this table a ResultQuery will be created
     *
     * @param columnName of the column
     * @return a List with Results
     */
    override fun selectEverything(columnName: String): List<ResultQuery> {

        val queries: MutableList<ResultQuery> = LinkedList()

        mySqlAdapter.dataSource.connection.use { it.prepareStatement("SELECT $columnName FROM $name").use { ps ->

            val result = ps.executeQuery()

            while (result.next()) {
                queries.add(ResultQuery(listOf(ResultEntry(columnName, result.getObject(columnName)))))
            }
        } }

        return queries
    }

    /**
     * Gets every entry on this table, for every entry from this table a ResultQuery will be created
     *
     * @return a List with Results
     */
    override fun selectEverything(): List<ResultQuery> {

        val queries: MutableList<ResultQuery> = LinkedList()

        mySqlAdapter.dataSource.connection.use { it.prepareStatement("SELECT * FROM $name").use { ps ->

            val result = ps.executeQuery()

            while (result.next()) {
                val metaData = result.metaData
                val entries: MutableList<ResultEntry> = LinkedList()

                for(i in 0 until metaData.columnCount)
                    entries.add(ResultEntry(metaData.getTableName(i), result.getObject(i)))

                queries.add(ResultQuery(entries))
            }
        } }

        return queries
    }

}