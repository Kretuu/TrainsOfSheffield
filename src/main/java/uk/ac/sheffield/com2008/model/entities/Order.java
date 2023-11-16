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
    private String userUUID;

    public Order(int orderNumber, Date dateOrdered, float totalPrice, Status status, String userUUID){
        this.orderNumber = orderNumber;
        this.dateOrdered = dateOrdered;
        this.totalPrice = totalPrice;
        this.status = status;
        orderLines = new HashMap<>();
        this.userUUID = userUUID;
    }

    /**
     * Add an orderline to this order. +Recalculates total price for order.
     * @param product the product object
     * @param quantity the quantity
     */
    public void addProduct(Product product, Integer quantity){

        if(hasProduct(product)){
            throw new RuntimeException("Tried to add a product to an Order that already has this product. Modify the quantity instead.");
        }
        orderLines.put(product, quantity);
        calculateTotalPrice();
    }

    /**
     * Returns whether given product is in this order
     * @param product
     * @return
     */
    public boolean hasProduct(Product product){
        return orderLines.containsKey(product);
    }

    /**
     * Changes the quantity in an orderline. +Recalculates total price for order.
     */
    public void modifyQuantity(Product product, int addedQuantity){
        if(!hasProduct(product)){
            throw new RuntimeException("Tried to modify the quantity of a product not in this Order. Add a new Product instead.");
        }
        orderLines.put(product, orderLines.get(product) + addedQuantity);
        calculateTotalPrice();
    }

    public void calculateTotalPrice(){
        totalPrice = 0;
        orderLines.forEach((product, quantity) -> {
            totalPrice += product.getPrice() * quantity;
        });
    }

    public void setAsConfirmed(){
        status = Status.CONFIRMED;
    }
    public void setAsFulfilled(){
        status = Status.FULFILLED;
    }

    public float getTotalPrice(){
        return totalPrice;
    }

    /**
     * @return product * quantity of that product in the order
     */
    public float getOrderLinePrice(Product product){
        if(!hasProduct(product)){
            throw new RuntimeException("Order Object does not contain this product");
        }
        else{
            return product.getPrice() * orderLines.get(product);
        }
    }

    /**
     * @param product
     * @return quantity of given product in this order
     */
    public int getProductQuantity(Product product){
        if(!hasProduct(product)){
            throw new RuntimeException("Order Object does not contain this product");
        }
        else{
            return orderLines.get(product);
        }
    }

    public String getUserUUID(){
        return userUUID;
    }
    public int getOrderNumber(){
        return orderNumber;
    }

    @Override
    public String toString(){
        return "ORDERNUM:  " + orderNumber + " STATUS: " + status.toString() + " TOTAL PRICE: " + totalPrice;
    }

    public void PrintFullOrder(){
        System.out.println(this.toString() + "CONTAINS: ");
        orderLines.forEach((product, quantity) -> {
            System.out.println("\t " + product.getProductCode() + " " + product.getName() + " Qty: " + quantity);
        });
    }
}
