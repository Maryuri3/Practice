package ec.edu.espe.customer.model;

/**
 *
 * @author Maryuri Quiña, @ESPE
 */
public class Customer {

    private String id;
    private String fullName;
    private double amount;

    // Constructor: Para crear el objeto con datos
    public Customer(String id, String fullName, double amount) {
        this.id = id;
        this.fullName = fullName;
        this.amount = amount;
    }

    // Métodos Getter: Permiten obtener los datos de forma segura
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public double getAmount() {
        return amount;
    }

    // --- LÓGICA DE CÁLCULO (OOP) ---
    // Si el profesor cambia el cálculo, lo modificas aquí.
    public double calculateTotal() {
        double tax = 0.12; // Ejemplo: 12%
        return this.amount + (this.amount * tax);
    }
}
