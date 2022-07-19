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