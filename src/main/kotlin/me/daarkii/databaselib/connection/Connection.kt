/*
 * Copyright 2022 Daarkii & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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