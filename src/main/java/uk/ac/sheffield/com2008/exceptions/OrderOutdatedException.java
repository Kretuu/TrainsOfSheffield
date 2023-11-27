package uk.ac.sheffield.com2008.exceptions;

public class OrderOutdatedException extends Exception {
    public OrderOutdatedException() {
        super("Order details are outdated. Basket has been reloaded.");
    }
}
