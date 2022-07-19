package me.daarkii.databaselib.query.update

import me.daarkii.databaselib.collection.DataPair

class InsertQuery {

    val insertable: List<DataPair<Any>>

    constructor(pairs: List<DataPair<Any>>) {
        this.insertable = pairs
    }

    constructor(vararg pair: DataPair<Any>) {
        this.insertable = pair.toList()
    }

}