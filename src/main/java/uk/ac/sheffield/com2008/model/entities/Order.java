package uk.ac.sheffield.com2008.model.entities;

import java.util.Date;
import java.util.HashMap;

public class Order {

    private int orderNumber;
    private Date dateOrdered;
    private float totalPrice;
    public enum Status {PENDING, CONFIRMED, FULFILLED};
    private Status status;
    // Product -> Quantity
    private HashMap<Product, Integer> orderLines;

    public Order(int orderNumber, Date dateOrdered, float totalPrice, Status status){
        this.orderNumber = orderNumber;
        this.dateOrdered = dateOrdered;
        this.totalPrice = totalPrice;
        this.status = status;
        orderLines = new HashMap<>();
    }

    /**
     * Add an orderline to this order
     * @param product the product object
     * @param quantity the quantity
     */
    public void addProduct(Product product, Integer quantity){

        //check if this product already is in the order, in which case
        // we would just add more onto the quantity
        if (orderLines.containsKey(product)) {
            orderLines.put(product, orderLines.get(product) + quantity);
        } else {
            // If not, add the Product to the map
            orderLines.put(product, quantity);
        }
        calculateTotalPrice();
    }

    /**
     * Changes the quantity in an orderline
     */
    public void modifyQuantity(Product product, int newQuantity){
        orderLines.put(product, newQuantity);
        calculateTotalPrice();
    }

    public void calculateTotalPrice(){
        totalPrice = totalPrice;
        //TODO calculate total price
    }

    public void setAsConfirmed(){
        status = Status.CONFIRMED;
    }
    public void setAsFulfilled(){
        status = Status.FULFILLED;
    }

    public float getTotalPrice(){
        //TODO: Calculate Total Price and return it
        return totalPrice;
    }

    @Override
    public String toString(){
        return "NUM: " + orderNumber + " STATUS: " + status.toString();
    }
}
