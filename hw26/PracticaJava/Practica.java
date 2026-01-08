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
        this.collection = MongoDBConnection.getDatabase().getCollection("Customers");
    }

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

//Este evento captura el nombre, consulta al controlador y llena los campos automáticamente si el producto existe.
private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {                                          
    // 1. Obtener y validar el nombre ingresado
    String nameToSearch = txtProduct.getText().trim();
    
    if (nameToSearch.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a product name to search.");
        return;
    }

    // 2. Llamar al controlador (Lógica modular)
    InventoryController controller = new InventoryController();
    Product foundProduct = controller.findByName(nameToSearch);

    // 3. Procesar el resultado
    if (foundProduct != null) {
        // Cargar los datos encontrados en los campos de texto
        txtPrice.setText(String.valueOf(foundProduct.getBasePrice()));
        
        // Mensaje de éxito
        JOptionPane.showMessageDialog(this, "Product '" + nameToSearch + "' found!");
    } else {
        // Mensaje si no existe en MongoDB Atlas
        JOptionPane.showMessageDialog(this, "Product not found in the inventory.");
        clearFields();
    }
}

//controller
public Product findByName(String name) {
    // Busca el primer documento que coincida con el nombre
    Document doc = collection.find(Filters.eq("name", name)).first();
    
    if (doc != null) {
        // Mapeo modular de Documento a Objeto Java
        return new Product(
            doc.getString("name"),
            doc.getDouble("basePrice"),
            0 // El stock ya no se usa, pero se mantiene el constructor por compatibilidad
        );
    }
    return null; // Retorna nulo si no hay coincidencias
}

//
private void refreshTable() {
    try {
        InventoryController controller = new InventoryController();
        DefaultTableModel model = new DefaultTableModel();
        
        // Definir columnas según tu modelo Product (Clientes)
        model.addColumn("ID");
        model.addColumn("Nombre Completo");
        model.addColumn("Email");
        model.addColumn("Tipo");

        // Obtener la lista de objetos Product
        java.util.List<Product> products = controller.getInventoryList();

        for (Product p : products) {
            model.addRow(new Object[]{
                p.getId(), 
                p.getFullName(), 
                p.getEmail(), 
                p.getType()
            });
        }

        jTable1.setModel(model);
        
        // Nota: Si son clientes, quizás no necesites "calculateTotalInventoryValue"
        // Si lo necesitas, asegúrate que el método exista en el Controller
        lblTotalValue.setText("Registros encontrados: " + products.size());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        System.err.println("Error: " + e.getMessage());
    }
}