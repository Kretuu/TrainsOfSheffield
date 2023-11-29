package uk.ac.sheffield.com2008.exceptions;

public class UserAlreadyHasRoleException extends Exception {
    public UserAlreadyHasRoleException(String message) {
        super(message);
    }
}
