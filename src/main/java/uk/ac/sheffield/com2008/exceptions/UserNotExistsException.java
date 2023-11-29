package uk.ac.sheffield.com2008.exceptions;

public class UserNotExistsException extends Exception {
    public UserNotExistsException() {
        super("Given user does not exist in the database");
    }
}
