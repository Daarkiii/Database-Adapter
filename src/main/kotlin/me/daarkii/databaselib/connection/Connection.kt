package me.daarkii.databaselib.connection

import me.daarkii.databaselib.collection.DataPair
import me.daarkii.databaselib.table.ColumnType
import me.daarkii.databaselib.table.Table
import me.daarkii.databaselib.holder.IConnectionHolder
import me.daarkii.databaselib.provider.Provider
import java.util.concurrent.CompletableFuture

interface Connection {

    /**
     * Opens a connection with the provider from the holder
     *
     * @param connectionHolder stores the data for the connection
     * @return a connection with the provider
     */
    fun openConnection(connectionHolder: IConnectionHolder) : Connection {
        return when(connectionHolder.provider) {
            Provider.MONGODB -> MongoConnection(connectionHolder)
            Provider.MYSQL -> MySqlConnection(connectionHolder)
        }
    }

    /**
     * Loads a database table and creates it if it's not existing
     *
     * @param name of the table
     * @param pairs a pair with the name of the column and the desired column type
     * @return the table
     */
    fun loadTable(name: String, vararg pairs: DataPair<ColumnType>) : Table

    /**
     * Loads a database table and creates it if it's not existing
     * It will execute everything async
     *
     * @param name of the table
     * @param pairs a pair with the name of the column and the desired column type
     * @return a CompletableFuture with the table
     */
    fun loadTableAsync(name: String, vararg pairs: DataPair<ColumnType>) : CompletableFuture<Table> {
        return CompletableFuture.supplyAsync { this.loadTable(name, *pairs) }
    }

}