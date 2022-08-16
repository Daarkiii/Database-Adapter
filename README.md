# Database-Adapter
version: **1.0.0**
***

This database adapter helps you with projects where you provide multiple databases. Even if you provide only one database form, this adapter simplifies e.g. the MySQL queries.

Currently, the following databases are integrated:
- MySQL
- MariaDB
- MongoDB

tEST

***
## API
First you need to create a connection, for this you need an IConnectionHolder, this you create with the provider and database data.

```` java
private Connection connection;
    
public void start() {
    //execute this on your main class and only once!
        
    //insert your data here
    IConnectionHolder holder = new IConnectionHolder(Provider.MONGODB, "localhost", 27077, "testdb");
        
    //open connection
    connection = Connection.openConnection(holder);
}
    
//Create getter
public Connection getConnection() {
    return this.connection;
}
````

With a connection like this, you can load tables, also here it is recommended to load them only once

```` java
private Connection connection;
private Table defaultTable;

public void start() {
    //execute this on your main class and only once!

    //insert your data here
    IConnectionHolder holder = new IConnectionHolder(Provider.MONGODB, "localhost", 27077, "testdb");

    //open connection
    connection = Connection.openConnection(holder);
    defaultTable = connection.loadTable("default_table", new DataPair<>("column1", ColumnType.BOOLEAN), new DataPair<>("column2", ColumnType.STRING));
}

//Create getter
public Connection getConnection() {
    return this.connection;
}
    
//Create getter for the table
public Table getDefaultTable() {
    return this.defaultTable;
}
````

In this table, all queries can now be executed, such as update, insert or select queries.

**This was the basic API, have fun using it!**

***
## Integration

You can easily use the adapter in your project, look below for Maven and Gradle and otherwise in the release tab!

### Maven:
``` xml
<repositories>
    <!--For releases and stable builds-->
    <repository>
	    <id>releases</id>
	    <url>https://repo.aysu.tv/repository/releases/</url>
    </repository>
    <!--For snapshots-->
    <repository>
	    <id>snapshots</id>
	    <url>https://repo.aysu.tv/repository/snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>me.daarkii</groupId>
        <artifactId>database-adapter</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
``` 

### Gradle:
``` kotlin

repositories {
    //For releases and stable builds
    maven("https://repo.aysu.tv/repository/releases/")

    //For snapshots
    maven("https://repo.aysu.tv/repository/snapshots/")
}

dependencies {
    implementation("me.daarkii", "database-adapter", "VERSION")
}
```
