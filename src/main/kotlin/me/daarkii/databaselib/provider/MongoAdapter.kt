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