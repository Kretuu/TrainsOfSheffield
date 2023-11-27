package uk.ac.sheffield.com2008.model.domain.managers;

import uk.ac.sheffield.com2008.exceptions.*;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static void updateUserBasket(User user) {
        Order usersBasket = OrderDAO.getUsersBasket(user);
        if(usersBasket == null) usersBasket = OrderManager.createNewOrder(user);
        user.setBasket(usersBasket);
    }

    /**
     * Set an order as confirmed
     * @param order order to confirm
     * @param user the user who that order belongs to
     */
    public static void confirmOrder(Order order, User user)
            throws SQLException, BankDetailsNotValidException, OrderQuantitiesInvalidException,
            OrderOutdatedException, InvalidOrderStateException {
        if(order.getOrderLines().isEmpty()) throw new InvalidOrderStateException("Empty order cannot be confirmed");

        if(!order.getStatus().equals(Order.Status.PENDING))
            throw new InvalidOrderStateException("Order is already confirmed");

        if(!UserManager.validateUserBankingCard(user))
            throw new BankDetailsNotValidException();

        validateOrder(order);
        order.setAsConfirmed();
        order.setDateOrdered(new Date());
        saveFullOrderState(order);
    }

    public static void validateOrder(Order order)
            throws OrderQuantitiesInvalidException, OrderOutdatedException {
        List<OrderLine> orderLines = order.getOrderLines();

        String[] productCodes = order.getOrderLines().stream()
                .map(orderLine -> orderLine.getProduct().getProductCode()).toArray(String[]::new);
        Map<String, Product> orderProducts = ProductDAO.getProductsByCodes(productCodes).stream()
                .collect(Collectors.toMap(Product::getProductCode, product -> product));
        if(orderProducts.size() != orderLines.size()) throw new OrderOutdatedException();

        List<OrderLine> invalidOrderLines = new ArrayList<>();
        for(OrderLine orderLine : orderLines){
            int stockProductQty = orderProducts.get(orderLine.getProduct().getProductCode()).getStock();
            if(stockProductQty < orderLine.getQuantity()) {
                orderLine.setQuantity(stockProductQty);
                invalidOrderLines.add(orderLine);
            }
        }

        if(!invalidOrderLines.isEmpty()) throw new OrderQuantitiesInvalidException(invalidOrderLines);
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
