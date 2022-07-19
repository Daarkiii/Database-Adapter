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