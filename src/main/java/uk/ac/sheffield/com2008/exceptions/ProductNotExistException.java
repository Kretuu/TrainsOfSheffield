package uk.ac.sheffield.com2008.exceptions;

public class ProductNotExistException extends Exception {
    public ProductNotExistException() {
        super("Given product does not exist in the database");
    }
}
