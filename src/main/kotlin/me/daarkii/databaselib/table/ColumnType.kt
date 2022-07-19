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

enum class ColumnType {

    STRING,
    INT,
    LONG,
    BOOLEAN,
    DOUBLE,
    FLOAT;

    val mySqlType: String
        get() {
            return when(this) {
                STRING -> "VARCHAR"
                INT -> "INT"
                FLOAT -> "FLOAT"
                DOUBLE -> "DOUBLE"
                LONG -> "LONG"
                BOOLEAN -> "BOOL"
            }
        }

    companion object {

        /**
         * Gets the type for a column with the given object instance
         *
         * @param any a random value of the desired class object
         * @return the Column Type for the class instance
         */
        fun fromObject(any: Any) : ColumnType {
            return when(any) {
                is Int -> INT
                is Float -> FLOAT
                is Double -> DOUBLE
                is Long -> LONG
                is Boolean -> BOOLEAN
                else -> STRING
            }
        }

    }

}