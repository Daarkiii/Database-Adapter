package me.daarkii.databaselib.collection

data class DataPair<V>(var key: String, var value: V) {


    companion object {
        fun of(key: String, value: Any) : DataPair<Any> {
            return DataPair(key, value)
        }
    }

}