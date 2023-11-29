package uk.ac.sheffield.com2008.exceptions;

public class OrderHasNoOwnerException extends Exception {
    public OrderHasNoOwnerException() {
       super("Cannot find user who placed this order");
    }
}
