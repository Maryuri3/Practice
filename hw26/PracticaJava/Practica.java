//product
package ec.edu.espe.storeinventory.model;

public class Product {
    private String name;
    private double basePrice;
    private int stock;
    public static final double TAX_RATE = 0.15; // Constante reutilizable

    public Product(String name, double basePrice, int stock) {
        this.name = name;
        this.basePrice = basePrice;
        this.stock = stock;
    }

    // Getters y Setters...

    public double getPriceWithTax() {
        return this.basePrice * (1 + TAX_RATE); // Lógica centralizada
    }
}

//inventoryController
package ec.edu.espe.storeinventory.controller;

import ec.edu.espe.storeinventory.model.Product;
import ec.edu.espe.storeinventory.db.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class InventoryController {
    private final MongoCollection<Document> collection;

    public InventoryController() {
        // Modularidad: La conexión se obtiene de una clase externa especializada
        this.collection = MongoDBConnection.getDatabase().getCollection("Inventory");
    }

    // Reutilizable: Recibe un objeto, no datos sueltos
    public void addProduct(Product product) {
        Document doc = new Document("name", product.getName())
                .append("basePrice", product.getBasePrice())
                .append("stock", product.getStock());
        collection.insertOne(doc);
    }

    // Retorna una lista de Objetos, no de Strings. Esto permite que la View decida cómo mostrarlo.
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        for (Document doc : collection.find()) {
            products.add(new Product(
                doc.getString("name"),
                doc.getDouble("basePrice"),
                doc.getInteger("stock")
            ));
        }
        return products;
    }

    public double calculateTotalValue() {
        double total = 0;
        for (Product p : getAllProducts()) {
            total += (p.getPriceWithTax() * p.getStock());
        }
        return total;
    }

    public void delete(String name) {
        collection.deleteOne(Filters.eq("name", name));
    }
}

//frm
private void refreshTable() {
    InventoryController controller = new InventoryController();
    DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
    tableModel.setRowCount(0); // Limpiar tabla de forma eficiente

    // Modular: La vista recorre objetos Product
    List<Product> inventory = controller.getAllProducts();
    for (Product p : inventory) {
        tableModel.addRow(new Object[]{
            p.getName(), 
            p.getStock(), 
            String.format("%.2f", p.getPriceWithTax())
        });
    }

    lblTotalValue.setText("Total Warehouse Value: $" + 
                         String.format("%.2f", controller.calculateTotalValue()));
}

//Para eliminar el campo stock y añadir la funcionalidad de buscar por nombre en Java,

//product
package ec.edu.espe.storeinventory.model;

public class Product {
    private String name;
    private double basePrice;
    private final double TAX_RATE = 0.15; // Regla de negocio: 15% IVA

    public Product(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    // Getters y Setters para name y basePrice

    public double getPriceWithTax() {
        return this.basePrice + (this.basePrice * this.TAX_RATE); // Lógica de cálculo centralizada
    }
}

//invetoryController
public class InventoryController {
    // ... (constructor y conexión se mantienen iguales)

    public void addProduct(Product product) {
        Document doc = new Document("name", product.getName())
                .append("basePrice", product.getBasePrice()); // Sin campo 'stock'
        collection.insertOne(doc);
    }

    public Product findByName(String name) {
        // Nueva función modular para buscar un solo producto por nombre
        Document doc = collection.find(Filters.eq("name", name)).first();
        if (doc != null) {
            return new Product(doc.getString("name"), doc.getDouble("basePrice"));
        }
        return null;
    }

    public double calculateTotalInventoryValue() {
        double total = 0;
        for (Document doc : collection.find()) {
            total += (doc.getDouble("basePrice") * 1.15); // Suma simple de precios con IVA
        }
        return total;
    }
}

//frm 
private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
    String name = txtProduct.getText().trim();
    if (name.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter a name to search.");
        return;
    }

    InventoryController controller = new InventoryController();
    Product p = controller.findByName(name); // Uso del nuevo método modular

    if (p != null) {
        txtPrice.setText(String.valueOf(p.getBasePrice()));
        JOptionPane.showMessageDialog(this, "Product found and loaded.");
    } else {
        JOptionPane.showMessageDialog(this, "Product not found.");
    }
}

private void refreshTable() {
    // ... (lógica de tabla ajustada a 2 columnas: Name y Sales Price)
    model.addColumn("Product Name");
    model.addColumn("Sales Price (Tax)");
    // ...
}