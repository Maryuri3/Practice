package ec.edu.espe.storeinventory.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Maryuri Quiña, @ESPE
 */
public class MongoDBConnection {
    private static final String URI = "mongodb+srv://maryuri:maryuri2007@cluster0.iektq66.mongodb.net/?retryWrites=true&w=majority";
    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(URI);
        }
        return mongoClient.getDatabase("StoreDB");
    }
}
