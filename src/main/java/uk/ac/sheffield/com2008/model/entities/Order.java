package uk.ac.sheffield.com2008.model.entities;

import uk.ac.sheffield.com2008.model.domain.data.OrderLine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Order {
    private final int orderNumber;
    private final String userUUID;
    private final List<OrderLine> orderLines = new ArrayList<>();
    private Date dateOrdered;
    private float totalPrice;
    private Status status;

    public Order(int orderNumber, Date dateOrdered, float totalPrice, Status status, String userUUID) {
        this.orderNumber = orderNumber;
        this.dateOrdered = dateOrdered;
        this.totalPrice = totalPrice;
        this.status = status;
        this.userUUID = userUUID;
    }

    /**
     * Get list of OrderLines
     *
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
     *
     * @param orderLine OrderLine
     */
    public void addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
    }

    /**
     * Add an orderline to this order. +Recalculates total price for order.
     *
     * @param product  the product object
     * @param quantity the quantity
     */
    public void addProduct(Product product, Integer quantity) {

        if (hasProduct(product)) {
            throw new RuntimeException("Tried to add a product to an Order that already has this product. Modify the quantity instead.");
        }
        orderLines.add(new OrderLine(quantity, product));
        calculateTotalPrice();
    }

    /**
     * Returns whether given product is in this order
     *
     * @param product
     * @return
     */
    public boolean hasProduct(Product product) {
        return orderLines.stream().anyMatch(orderLine -> orderLine.hasProduct(product));
    }

    /**
     * Changes the quantity in an orderline. +Recalculates total price for order.
     */
    public void modifyQuantity(OrderLine orderline, int addedQuantity) {
        orderline.setQuantity(orderline.getQuantity() + addedQuantity);
        calculateTotalPrice();
    }

    /**
     * Removes given orderline from this object's orderlines list
     *
     * @param orderLine orderline to remove
     */
    public void removeOrderline(OrderLine orderLine) {
        if (hasProduct(orderLine.getProduct())) {
            orderLines.remove(orderLine);
            calculateTotalPrice();
        } else {
            throw new RuntimeException("Tried to remove a product that doesnt exist in order.");
        }
    }

    public void calculateTotalPrice() {
        totalPrice = 0;
        orderLines.forEach(OrderLine::calculatePrice);
        orderLines.forEach(orderLine -> totalPrice += orderLine.getPrice());
    }

    public void setAsConfirmed() {
        status = Status.CONFIRMED;
    }

    public void setAsFulfilled() {
        status = Status.FULFILLED;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    /**
     * @return product * quantity of that product in the order
     */
    public float getOrderLinePrice(Product product) {
        OrderLine orderLine = getOrderLineFromProduct(product);
        if (orderLine == null)
            throw new RuntimeException("Order Object does not contain this product");

        return orderLine.getPrice();
    }

    /**
     * @param product
     * @return quantity of given product in this order
     */
    public int getProductQuantity(Product product) {
        OrderLine orderLine = getOrderLineFromProduct(product);
        if (orderLine == null) throw new RuntimeException("Order Object does not contain this product");

        return orderLine.getQuantity();
    }

    public OrderLine getOrderLineFromProduct(Product product) {
        List<OrderLine> orderLines = this.orderLines.stream().filter(orderLine -> orderLine.hasProduct(product)).toList();
        if (orderLines.isEmpty()) return null;
        return orderLines.get(0);
    }

    public String getUserUUID() {
        return userUUID;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    @Override
    public String toString() {
        return "ORDERNUM:  " + orderNumber + " STATUS: " + status.toString() + " TOTAL PRICE: " + totalPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Order o)) return false;
        return new HashSet<>(o.getOrderLines()).equals(new HashSet<>(this.orderLines))
                && o.getOrderNumber() == this.orderNumber && o.getTotalPrice() == this.totalPrice
                && o.getDateOrdered() == this.dateOrdered && o.getStatus().equals(this.status);
    }

    public enum Status {PENDING, CONFIRMED, FULFILLED}

}
