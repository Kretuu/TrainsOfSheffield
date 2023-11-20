package uk.ac.sheffield.com2008.model.entities;

import uk.ac.sheffield.com2008.model.domain.data.OrderLine;

import java.util.*;

public class Order {

    private final int orderNumber;
    private Date dateOrdered;
    private float totalPrice;
    public enum Status {PENDING, CONFIRMED, FULFILLED}
    private Status status;
    // Product -> Quantity
//    private Map<Product, Integer> orderLines = new HashMap<>();
    private List<OrderLine> orderLines = new ArrayList<>();
    private final String userUUID;

    public Order(int orderNumber, Date dateOrdered, float totalPrice, Status status, String userUUID){
        this.orderNumber = orderNumber;
        this.dateOrdered = dateOrdered;
        this.totalPrice = totalPrice;
        this.status = status;
//        orderLines = new HashMap<>();
        this.userUUID = userUUID;
    }


    /**
     * Get list of OrderLines
     * @return list of OrderLines
     */
    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public Date getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(Date dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * add single OrderLine object to list
     * @param orderLine OrderLine
     */
    public void addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
    }

    /**
     * Set orderLines list. Used only in DAO class.
     * @param orderLines list of OrderLine objects
     */
    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
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
        orderLines.add(new OrderLine(quantity, product));
//        orderLines.put(product, quantity);
        calculateTotalPrice();
    }

    /**
     * Returns whether given product is in this order
     * @param product
     * @return
     */
    public boolean hasProduct(Product product){
        return orderLines.stream().anyMatch(orderLine -> orderLine.hasProduct(product));
    }

    /**
     * Changes the quantity in an orderline. +Recalculates total price for order.
     */
    public void modifyQuantity(Product product, int addedQuantity){
        OrderLine modifiedOrderLine = getOrderLineFromProduct(product);
        if(modifiedOrderLine == null) {
            throw new RuntimeException("Tried to modify the quantity of a product not in this Order. Add a new Product instead.");
        }
        modifiedOrderLine.setQuantity(modifiedOrderLine.getQuantity() + addedQuantity);
//        if(!hasProduct(product)){
//            throw new RuntimeException("Tried to modify the quantity of a product not in this Order. Add a new Product instead.");
//        }
//        orderLines.put(product, orderLines.get(product) + addedQuantity);
        calculateTotalPrice();
    }

    public void calculateTotalPrice(){
        totalPrice = 0;
        orderLines.forEach(orderLine -> totalPrice += orderLine.getPrice());
//        orderLines.forEach((product, quantity) -> {
//            totalPrice += product.getPrice() * quantity;
//        });
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
        OrderLine orderLine = getOrderLineFromProduct(product);
        if(orderLine == null)
            throw new RuntimeException("Order Object does not contain this product");

        return orderLine.getPrice();

//        if(!hasProduct(product)){
//            throw new RuntimeException("Order Object does not contain this product");
//        }
//        else{
//
//            return orderLine.getPrice();
//        }
    }

    /**
     * @param product
     * @return quantity of given product in this order
     */
    public int getProductQuantity(Product product){
        OrderLine orderLine = getOrderLineFromProduct(product);
        if(orderLine == null) throw new RuntimeException("Order Object does not contain this product");

        return orderLine.getQuantity();

//        if(!hasProduct(product)){
//            throw new RuntimeException("Order Object does not contain this product");
//        }
//        else{
//            return orderLine.getQuantity();
//        }
    }

    private OrderLine getOrderLineFromProduct(Product product) {
        List<OrderLine> orderLines = this.orderLines.stream().filter(orderLine -> orderLine.hasProduct(product)).toList();
        if(orderLines.isEmpty()) return null;
        return orderLines.get(0);
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
        System.out.println(this + "CONTAINS: ");
        orderLines.forEach(orderLine -> {
            Product product = orderLine.getProduct();
            System.out.println("\t " + product.getProductCode() + " " + product.getName() + " Qty: " + orderLine.getQuantity());
        });

//        orderLines.forEach((product, quantity) -> {
//            System.out.println("\t " + product.getProductCode() + " " + product.getName() + " Qty: " + quantity);
//        });
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Order o)) return false;
        return new HashSet<>(o.getOrderLines()).equals(new HashSet<>(this.orderLines))
                && o.getOrderNumber() == this.orderNumber && o.getTotalPrice() == this.totalPrice
                && o.getDateOrdered() == this.dateOrdered && o.getStatus().equals(this.status);
    }
}
