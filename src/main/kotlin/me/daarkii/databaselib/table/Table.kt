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

package me.daarkii.databaselib.table

import me.daarkii.databaselib.collection.DataPair
import me.daarkii.databaselib.query.result.ResultQuery
import me.daarkii.databaselib.query.update.InsertQuery
import me.daarkii.databaselib.query.update.UpdateQuery
import java.util.concurrent.CompletableFuture

/**
 * A Table safes multiple entries which includes columns
 */
interface Table {

    /**
     * The name of the table
     */
    val name: String

    /**
     * The number of inserted values in this table
     */
    val count: Int

    /**
     * A list with all column names and types
     */
    val columns: List<String>

    /**
     * Gets every entry on this table from the given column, for every entry from this table a ResultQuery will be created
     *
     * @param columnName of the column
     * @return a List with Results
     */
    fun selectEverything(columnName: String) : List<ResultQuery>


    /**
     * Gets every entry on this table, for every entry from this table a ResultQuery will be created
     *
     * @return a List with Results
     */
    fun selectEverything() : List<ResultQuery>

    /**
     * Select all entries from this table
     *
     * @param filter the filters for the select query including name of the column && the value to filter
     * @return a ResultQuery which may be empty
     */
    fun select(vararg filter: DataPair<Any>) : ResultQuery

    /**
     * Select an entry from this table from the column
     *
     * @param columnName the name of the column table
     * @param filter the filters for the select query including name of the column && the value to filter
     * @return a ResultQuery which may be empty
     */
    fun select(columnName: String, vararg filter: DataPair<Any>) : ResultQuery


    /**
     * Select all entries async from this table
     *
     * @param filter the filters for the select query including name of the column && the value to filter
     * @return a CompletableFuture with a ResultQuery which may be empty
     */
    fun selectAsync(vararg filter: DataPair<Any>) : CompletableFuture<ResultQuery> {
        return CompletableFuture.supplyAsync { this.select(*filter) }
    }

    /**
     * Select an entry async from this table from the column
     *
     * @param columnName the name of the column table
     * @param filter the filters for the select query including name of the column && the value to filter
     * @return a CompletableFuture with a ResultQuery which may be empty
     */
    fun selectAsync(columnName: String, vararg filter: DataPair<Any>) : CompletableFuture<ResultQuery> {
        return CompletableFuture.supplyAsync { this.select(columnName, *filter) }
    }

    /**
     * Checks if an entry with the given filter exists
     *
     * @param filter the filters for the select query including name of the column && the value to filter
     * @return true if there is a (not empty) result
     */
    fun checkExisting(vararg filter: DataPair<Any>) : Boolean {
        return !this.select(columns[0], *filter).isEmpty
    }

    /**
     * Checks async if an entry with the given filter exists
     *
     * @param filter the filters for the select query including name of the column && the value to filter
     * @return a CompletableFuture with true if there is a (not empty) result
     */
    fun checkExistingAsync(vararg filter: DataPair<Any>) : CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync { this.checkExisting(*filter) }
    }

    /**
     * Creates a new entry without an existing check
     *
     * @param query with insert values
     */
    fun insert(query: InsertQuery)

    /**
     * Creates a new entry async without an existing check
     *
     * @param query with insert values
     */
    fun insertAsync(query: InsertQuery) : CompletableFuture<Void> {
        return CompletableFuture.runAsync { this.insert(query) }
    }

    /**
     * Creates a new entry without an existing check
     *
     * @param query with the filter and the update values
     */
    fun update(query: UpdateQuery) {
        this.update(query, false)
    }

    /**
     * Creates a new entry and check if shouldCheck is true if there is something that can be updated
     * if not it will insert the value
     *
     * @param query with the filter and the update values
     * @param shouldCheck if the method should check if there is something that can be updated
     */
    fun update(query: UpdateQuery, shouldCheck: Boolean)

    /**
     * Creates a new entry async without an existing check
     *
     * @param query with the filter and the update values
     */
    fun updateAsync(query: UpdateQuery) : CompletableFuture<Void> {
        return CompletableFuture.runAsync { this.update(query) }
    }

    /**
     * Creates a new entry async and check async if shouldCheck is true if there is something that can be updated
     * if not it will insert the value
     *
     * @param query with the filter and the update values
     * @param shouldCheck if the method should check if there is something that can be updated
     */
    fun updateAsync(query: UpdateQuery, shouldCheck: Boolean) : CompletableFuture<Void> {
        return CompletableFuture.runAsync { this.update(query, shouldCheck) }
    }
}