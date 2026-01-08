package ec.edu.espe.totalsales.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Maryuri Quiña, @ESPE
 */
public class MongoDBConnection {
    private static final String URI = "mongodb+srv://oop:oop@cluster0.9knxc.mongodb.net/?appName=Cluster0";
    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(URI);
        }
        return mongoClient.getDatabase("oop");
    }
}
