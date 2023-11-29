package uk.ac.sheffield.com2008.exceptions;

import uk.ac.sheffield.com2008.model.entities.Order;

public class OrderNotExistException extends Exception {
    public OrderNotExistException(Order order) {
        super("Order of id " + order.getOrderNumber() + " does not exist.");
    }
}
