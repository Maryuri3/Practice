package ec.edu.espe.customer.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Maryuri Quiña, @ESPE
 */
public class DatabaseConnection {

    // CAMBIA "company_db" por el nombre real de tu base de datos en MongoDB
    private static final String DATABASE_NAME = "company_db";
    private static final String URI = "mongodb://localhost:27017";

    public static MongoDatabase getDatabase() {
        try {
            MongoClient mongoClient = MongoClients.create(URI);
            return mongoClient.getDatabase(DATABASE_NAME);
        } catch (Exception e) {
            System.err.println("Error al conectar a MongoDB: " + e.getMessage());
            return null;
        }
    }
}
