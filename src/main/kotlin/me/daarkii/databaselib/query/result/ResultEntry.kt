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

data class ResultEntry(val columnName: String, private val any: Any) {

    /**
     * Migrates the object to a String
     *
     * @return the object as String
     */
    val asString = any.toString()

    /**
     * Migrates the object to an Integer or null
     *
     * @return the object as Integer or null if the object is no Integer
     */
    val asInt = this.asString.toIntOrNull()

    /**
     * Migrates the object to an Integer
     *
     * @throws numberFormatError if the object is not an Integer
     * @return the object as Integer
     */
    val asIntStrict = this.asString.toInt()

    /**
     * Migrates the object to a Double or null
     *
     * @return the object as Double or null if the object is no Double
     */
    val asDouble = this.asString.toDoubleOrNull()

    /**
     * Migrates the object to a Double
     *
     * @throws numberFormatError if the object is not a Double
     * @return the object as Double
     */
    val asDoubleStrict = this.asString.toDouble()

    /**
     * Migrates the object to a Float or null
     *
     * @return the object as Float or null if the object is no Float
     */
    val asFloat = this.asString.toDoubleOrNull()

    /**
     * Migrates the object to a Float
     *
     * @throws numberFormatError if the object is not a Float
     * @return the object as Float
     */
    val asFloatStrict = this.asString.toDouble()

    /**
     * Migrates the object to a Long or null
     *
     * @return the object as Long or null if the object is no Long
     */
    val asLong = this.asString.toLongOrNull()

    /**
     * Migrates the object to a Long
     *
     * @throws numberFormatError if the object is not a Long
     * @return the object as Long
     */
    val asLongStrict = this.asString.toLong()

    /**
     * Migrates the object to a Boolean
     *
     * @return the object as Boolean or null if the object is no Boolean
     */
    val asBoolean = this.asString.toBooleanStrictOrNull()

    /**
     * Migrates the object to a Boolean
     *
     * @throws Exception if the object is not a Boolean
     * @return the object as Boolean
     */
    val asBooleanStrict = this.asString.toBooleanStrict()

}