package uk.ac.sheffield.com2008.model.domain.managers;

import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.User;

/**
 * Mediator class between Order object in memory, UI actions, and database
 */
public class OrderManager {

    public OrderManager(){

    }

    /**
     * Creates a new blank, pending order
     * @return fresh order
     */
    public static Order createNewOrder(User user){
        OrderDAO.createOrder(user);
        return OrderDAO.getUsersBasket(user);
    }

    //TODO: Function that returns a list of orderlines for given order

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
        order.modifyQuantity(product, newQuantity);
        OrderDAO.updateOrderLineQuantity(order, product);
        OrderDAO.updateOrderTotalPrice(order);
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
