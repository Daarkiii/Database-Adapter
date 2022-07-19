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
import me.daarkii.databaselib.table.MySqlTable
import me.daarkii.databaselib.table.Table
import me.daarkii.databaselib.holder.IConnectionHolder
import me.daarkii.databaselib.provider.MySqlAdapter
import java.util.stream.Collectors

class MySqlConnection(holder: IConnectionHolder) : Connection {

    private val mySQL = MySqlAdapter(holder)

    /**
     * Loads a database table and creates it if it's not existing
     *
     * @param name of the table
     * @param pairs a pair with the name of the column and the desired column type
     */
    override fun loadTable(name: String, vararg pairs: DataPair<ColumnType>): Table {

        //Create the table if needed
        this.createTable(name, pairs.toList())

        return MySqlTable(pairs.toList().stream().map { it.key }.collect(Collectors.toList()), name, mySQL)
    }

    /**
     * Creates a mysql table with the given name and the columns
     *
     * @param tableName which the table should have
     * @param pairs a List with all columns (names and types)
     */
    private fun createTable(tableName: String, pairs: List<DataPair<ColumnType>>) {

        val builder = StringBuilder().append("CREATE TABLE IF NOT EXISTS `$tableName` (")

        for(i in pairs.indices) {

            builder.append("`${pairs[i].key}` ${pairs[i].value.mySqlType}")

            if(i != pairs.lastIndex)
                builder.append(", ")
        }

        builder.append(")")

        mySQL.dataSource.connection.prepareStatement(builder.toString()).use { it.executeUpdate() }
    }

}