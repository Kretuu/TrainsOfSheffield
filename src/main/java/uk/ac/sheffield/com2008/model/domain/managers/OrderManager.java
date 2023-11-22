package uk.ac.sheffield.com2008.model.domain.managers;

import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.User;

/**
 * Mediator class between Order object in memory, UI actions, and database
 */
public class OrderManager {

    /**
     * Creates a new blank, pending order
     * @return fresh order
     */
    public static Order createNewOrder(User user){
        OrderDAO.createOrder(user);
        return OrderDAO.getUsersBasket(user);
    }

    /**
     * Add an orderline to a given order
     * @param order order to add to
     * @param product the product object
     * @param quantity the quantity
     */
    public static void addProductToOrder(Order order, Product product, int quantity){
        order.addProduct(product, quantity);
        OrderDAO.createOrderLine(order, product);
        OrderDAO.updateOrderTotalPrice(order);
    }

    /**
     * modify the quantity of a given order
     * @param order order to update
     * @param product product to modify quantity of
     * @param newQuantity new quantity of product
     */
    public static void modifyProductQuantity(Order order, Product product, int newQuantity){
        OrderLine modifiedOrderLine = order.getOrderLineFromProduct(product);
        if(modifiedOrderLine == null) {
            throw new RuntimeException("Tried to modify the quantity of a product not in this Order. Add a new Product instead.");
        }
        order.modifyQuantity(modifiedOrderLine, newQuantity);
        OrderDAO.updateOrderLineEntirely(order, modifiedOrderLine);
        OrderDAO.updateOrderTotalPrice(order);
    }

    /**
     *  removes from given order the given orderline in the object then from
     *  the database
     * @param order order in question
     * @param orderLine orderline to remove
     */
    public static void deleteOrderline(Order order, OrderLine orderLine){
        order.removeOrderline(orderLine);
        OrderDAO.deleteOrderline(order, orderLine);
        OrderDAO.updateOrderTotalPrice(order);
    }

    /**
     * Takes the entire order and updates it in the database.
     * Takes each associated orderline and does the same.
     */
    public static void saveFullOrderState(Order order){
        order.calculateTotalPrice();
        OrderDAO.updateAllOrderlinesEntirely(order);
        OrderDAO.updateOrderEntirely(order);
    }

    /**
     * Set an order as confirmed
     * @param order order to confirm
     * @param user the user who that order belongs to
     */
    public static void confirmOrder(Order order, User user){
        order.setAsConfirmed();
        createNewOrder(user);
        //TODO: OrderDAO UPDATE status FOR ORDER
    }

    /**
     * Set an order as confirmed
     * @param order order to confirm
     */
    public static void fulfilOrder(Order order){
        order.setAsFulfilled();
        //TODO: OrderDAO UPDATE status FOR ORDER
    }
}
