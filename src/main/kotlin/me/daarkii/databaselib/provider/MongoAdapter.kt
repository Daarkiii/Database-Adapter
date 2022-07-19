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

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.daarkii.databaselib.holder.IConnectionHolder
import org.bson.Document

class MongoAdapter(private val holder: IConnectionHolder) {

    private var mongoClient: MongoClient? = null
    private var mongoDatabase: MongoDatabase? = null

    /**
     * Connects to a MongoDB database and creates the MongoClient if needed
     */
    fun connect(database: String) {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(this.buildConnectionString())
        }

        mongoDatabase = mongoClient?.getDatabase(database)
    }

    /**
     * Closes the MongoClient if it is connected
     */
    fun disconnect() {
        if (mongoClient != null) {
            mongoClient!!.close()
        }
    }

    /**
     * Gets the collection with the given name, if it's not existing its get created
     *
     * @param name of the collection
     * @return the collection if exists or an empty collection
     */
    fun getCollection(name : String) : MongoCollection<Document> {
        return mongoDatabase!!.getCollection(name)
    }

    /**
     * Creates the string which is needed to create a MongoClient
     */
    private fun buildConnectionString() : String {

        if(holder.user == null || holder.password == null) {
            return "mongodb://${holder.host}:${holder.port}/?maxPoolSize=20&w=majority"
        }

        return "mongodb://${holder.user}:${holder.password}@${holder.host}:${holder.port}/?maxPoolSize=20&w=majority"
    }
}