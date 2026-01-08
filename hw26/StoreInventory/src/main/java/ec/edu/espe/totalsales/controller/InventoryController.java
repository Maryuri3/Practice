package ec.edu.espe.totalsales.controller;

import ec.edu.espe.totalsales.model.Product;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import ec.edu.espe.totalsales.db.MongoDBConnection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maryuri Quiña, @ESPE
 */
public class InventoryController {

    private final MongoCollection<Document> collection;

    public InventoryController() {
        this.collection = MongoDBConnection.getDatabase().getCollection("Customers");
    }

    public void addProduct(Product product) {
        Document doc = new Document("id", product.getId())
                .append("fullName", product.getFullName())
                .append("email", product.getEmail())
                .append("type", product.getType());
        collection.insertOne(doc);
    }

    public List<Product> getInventoryList() {
        List<Product> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Product p = new Product(
                        doc.getString("id"),
                        doc.getString("fullName"),
                        doc.getString("email"),
                        doc.getString("type")
                );
                list.add(p);
            }
        }
        return list;
    }

}
