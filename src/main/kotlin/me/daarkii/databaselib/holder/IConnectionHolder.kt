package me.daarkii.databaselib.holder

import me.daarkii.databaselib.provider.Provider

class IConnectionHolder {

    val provider: Provider
    val host: String
    val port: Int
    val user: String?
    val password: String?
    val database: String

    constructor(provider: Provider, host: String, port: Int, user: String, password: String, database: String) {
        this.provider = provider
        this.host = host
        this.port = port
        this.user = user
        this.password = password
        this.database = database
    }

    constructor(provider: Provider, host: String, port: Int, database: String) {
        this.provider = provider
        this.host = host
        this.port = port
        this.user = null
        this.password = null
        this.database = database
    }

}