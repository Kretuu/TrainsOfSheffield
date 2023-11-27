package uk.ac.sheffield.com2008.exceptions;

public class OrderDetailsOutdatedException extends Exception {
    public OrderDetailsOutdatedException() {
        super("Order details are outdated. Refreshing page.");
    }
}
