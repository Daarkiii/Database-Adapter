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