package Datasource;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private MongoClient mongoClient;
    private String uri = "mongodb://localhost:27017/";
    private String database = "userdb";


    public void init() {
        mongoClient = MongoClients.create(uri);
    }

    public MongoDatabase getDatabase() {
        MongoDatabase db = mongoClient.getDatabase(database);
        System.out.println("Database: " + db.getName());
        return db;
    }





}