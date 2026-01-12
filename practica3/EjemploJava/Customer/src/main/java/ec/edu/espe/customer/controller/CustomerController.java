package ec.edu.espe.customer.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import ec.edu.espe.customer.db.DatabaseConnection;
import ec.edu.espe.customer.model.Customer; // Importamos el modelo
import javax.swing.text.Document;

/**
 *
 * @author Maryuri Quiña, @ESPE
 */
public class CustomerController {
// Este método obtiene una lista de objetos Customer desde la base de datos

    public List<Customer> getCustomersFromDatabase() {
        List<Customer> customerList = new ArrayList<>();

        // 1. Conectamos a la colección "customers"
        MongoCollection<Document> collection = DatabaseConnection.getDatabase().getCollection("customers");

        // 2. Buscamos los documentos
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                // 3. Extraemos datos del documento (Asegúrate que coincidan los nombres con Mongo)
                String id = doc.getString("id");
                String name = doc.getString("fullName");

                // Si en Mongo el número es entero, usamos .getInteger().doubleValue() 
                // para evitar errores de tipo.
                double amount = doc.get("amount") != null ? doc.getDouble("amount") : 0.0;

                // 4. Creamos el objeto Customer y lo metemos a la lista
                Customer customer = new Customer(id, name, amount);
                customerList.add(customer);
            }
        }
        return customerList;
    }
}
    /*   
    public class CustomerController {

    // Este método es el que llamarás desde el botón
    public void updateTable(javax.swing.JTable table) {
        // 1. Configuramos los títulos de la tabla
        String[] header = {"ID", "NAME", "AMOUNT", "TOTAL CALC"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        table.setModel(model);

        // 2. Conectamos a la colección
        MongoCollection<Document> collection = DatabaseConnection.getDatabase().getCollection("customers");

        // 3. Traemos los datos y los convertimos en Objetos Customer
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                
                // Creamos el objeto usando los datos de Mongo
                Customer customer = new Customer(
                    doc.getString("id"),
                    doc.getString("fullName"),
                    doc.getDouble("amount")
                );

                // 4. Agregamos al modelo de la tabla usando los métodos del Objeto
                model.addRow(new Object[]{
                    customer.getId(),
                    customer.getFullName(),
                    customer.getAmount(),
                    customer.calculateTotal() // Llama al cálculo del modelo
                });
            }
        }
    }
}
}
     */
