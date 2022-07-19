package me.daarkii.databaselib.query.update

import me.daarkii.databaselib.collection.DataPair

class UpdateQuery {

    val updatable: List<DataPair<Any>>
    val filter: List<DataPair<Any>>?

    constructor(pairs: List<DataPair<Any>>) {
        this.updatable = pairs
        this.filter = null
    }

    constructor(pairs: List<DataPair<Any>>, filter: List<DataPair<Any>>) {
        this.updatable = pairs
        this.filter = filter
    }

    constructor(vararg pair: DataPair<Any>) {
        this.updatable = pair.toList()
        this.filter = null
    }

    constructor(filter: List<DataPair<Any>>, vararg pair: DataPair<Any>) {
        this.updatable = pair.toList()
        this.filter = filter
    }

}