package uk.ac.sheffield.com2008.exceptions;

public class InvalidProductQuantityException extends Exception {
    public InvalidProductQuantityException() {
        super("Product quantity cannot be negative");
    }
}
