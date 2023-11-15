package uk.ac.sheffield.com2008.model.domain;

import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;

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
    private static Order createNewOrder(){
        //TODO: OrderDAO CREATE NEW ORDER
        // TODO: GRAB THE NEW ORDER CREATED

        return null;
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
        //TODO: OrderDAO INSERT NEW ORDERLINE
    }

    /**
     * modify the quantity of a given order
     * @param order order to update
     * @param product product to modify quantity of
     * @param newQuantity new quantity of product
     */
    public static void modifyProductQuantity(Order order, Product product, int newQuantity){
        order.modifyQuantity(product, newQuantity);
        //TODO: OrderDAO UPDATE quantity FOR PRODUCT in this ORDER
    }

    /**
     * Set an order as confirmed
     * @param order order to confirm
     */
    public static void confirmOrder(Order order){
        order.setAsConfirmed();
        createNewOrder();
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
