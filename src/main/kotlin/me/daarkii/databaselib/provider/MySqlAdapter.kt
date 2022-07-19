package me.daarkii.databaselib.provider

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.daarkii.databaselib.holder.IConnectionHolder

class MySqlAdapter(private val holder: IConnectionHolder) {

    private val hikariConfig: HikariConfig = HikariConfig()
    var dataSource: HikariDataSource = null!!

    init {
        this.connect()
    }

    /**
     * Starts a new Datasource
     */
    private fun connect() {
        hikariConfig.jdbcUrl = "jdbc:mysql://" + holder.host + ":" + holder.port  + "/" + holder.database
        hikariConfig.username = holder.user!!
        hikariConfig.password = holder.password!!
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true")
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250")
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        hikariConfig.maximumPoolSize = 10
        dataSource = HikariDataSource(hikariConfig)
    }

}