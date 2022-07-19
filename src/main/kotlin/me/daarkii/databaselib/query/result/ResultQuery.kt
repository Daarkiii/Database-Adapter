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

package me.daarkii.databaselib.query.result

import java.util.stream.Collectors

class ResultQuery(val results: Collection<ResultEntry>) {

    /**
     * Gets the first loaded entry
     *
     * @return the entry or null if there is no result
     */
    val first = results.firstOrNull()

    /**
     * Gets the last loaded entry
     *
     * @return the entry or null if there is no result
     */
    val last = results.lastOrNull()

    /**
     * Checks if the result is empty
     *
     * @return true if the result is empty
     */
    val isEmpty = results.isEmpty()

    /**
     * Gets a result with the given name
     *
     * @param name of the column
     * @return the result or null if there is no result for this column
     */
    fun getResult(name: String) : ResultEntry? {
        return results.stream().filter { it.columnName == name }.collect(Collectors.toList()).firstOrNull()
    }

}