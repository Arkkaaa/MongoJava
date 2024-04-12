package Datasource;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private MongoClient mongoClient;
    private String uri = "mongodb://localhost:27017";
    private String database = "userdb";
    private String collection = "users";

    public void init() {
        mongoClient = MongoClients.create(uri);
    }

    public void getDatabase() {
        MongoDatabase db = mongoClient.getDatabase(database);
        System.out.println("Database: " + db.getName());
    }

    public void getCollection() {
        MongoDatabase db = mongoClient.getDatabase(database);
        MongoCollection<Document> col = db.getCollection(collection);
        System.out.println("Collection: " + col.getNamespace().getCollectionName());
    }

    public void insertDocument() {
        MongoDatabase db = mongoClient.getDatabase(database);
        MongoCollection<Document> col = db.getCollection(collection);
        Document doc = new Document("name", "John Doe")
                .append("age", 30)
                .append("city", "New York");
    }

}