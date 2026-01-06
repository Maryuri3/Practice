package ec.edu.espe.storeinventory.controller;

import ec.edu.espe.storeinventory.model.Product;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import ec.edu.espe.storeinventory.db.MongoDBConnection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Maryuri Quiña, @ESPE
 */
public class InventoryController {

    private final MongoCollection<Document> collection;

    public InventoryController() {
        this.collection = MongoDBConnection.getDatabase().getCollection("Inventory");
    }

    public void addProduct(Product product) {
        Document doc = new Document("name", product.getName())
                .append("basePrice", product.getBasePrice())
                .append("stock", product.getStock());
        collection.insertOne(doc);
    }

    public List<String> getInventoryList() {
        List<String> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Product p = new Product(
                        doc.getString("name"),
                        doc.getInteger("stock") != null ? doc.getDouble("basePrice") : 0.0,
                        doc.getInteger("stock")
                );
                list.add(p.getName() + " | Stock: " + p.getStock() + " | Price: $" + String.format("%.2f", p.getPriceWithTax()));
            }
        }
        return list;
    }

    public double calculateTotalInventoryValue() {
        double total = 0;
        for (Document doc : collection.find()) {
            double priceWithTax = doc.getDouble("basePrice") * 1.15;
            int stock = doc.getInteger("stock");
            total += (priceWithTax * stock);
        }
        return total;
    }

    public void deleteProduct(String name) {
        collection.deleteOne(Filters.eq("name", name));
    }

    public void updateStock(String name, int newStock) {
        collection.updateOne(Filters.eq("name", name),
                new org.bson.Document("$set", new org.bson.Document("stock", newStock)));
    }

}
