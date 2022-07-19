package me.daarkii.databaselib.connection

import me.daarkii.databaselib.collection.DataPair
import me.daarkii.databaselib.table.ColumnType
import me.daarkii.databaselib.table.MongoTable
import me.daarkii.databaselib.table.Table
import me.daarkii.databaselib.holder.IConnectionHolder
import me.daarkii.databaselib.provider.MongoAdapter
import java.util.stream.Collectors

class MongoConnection(holder: IConnectionHolder) : Connection {

    private val mongo = MongoAdapter(holder)

    /**
     * Loads a database table and creates it if it's not existing
     *
     * @param name of the table
     * @param pairs a pair with the name of the column and the desired column type
     */
    override fun loadTable(name: String, vararg pairs: DataPair<ColumnType>): Table {
        //With a MongoDB we do not have to create a table manually
        return MongoTable(name, pairs.toList().stream().map { it.key }.collect(Collectors.toList()), mongo)
    }

}