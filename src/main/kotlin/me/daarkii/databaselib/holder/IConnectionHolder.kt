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