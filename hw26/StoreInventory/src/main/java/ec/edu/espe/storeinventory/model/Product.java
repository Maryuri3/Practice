package ec.edu.espe.storeinventory.model;

/**
 *
 * @author Maryuri Quiña, @ESPE
 */
public class Product {

    private String name;
    private double basePrice;
    private int stock;
    private double TAX_RATE = 0.15;

    public Product(String name, double basePrice, int stock) {
        this.name = name;
        this.basePrice = basePrice;
        this.stock = stock;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the basePrice
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * @param basePrice the basePrice to set
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the TAX_RATE
     */
    public double getTAX_RATE() {
        return TAX_RATE;
    }

    /**
     * @param TAX_RATE the TAX_RATE to set
     */
    public void setTAX_RATE(double TAX_RATE) {
        this.TAX_RATE = TAX_RATE;
    }
    
    public double getPriceWithTax() {
        return this.basePrice + (this.basePrice * this.TAX_RATE);
    }

}
