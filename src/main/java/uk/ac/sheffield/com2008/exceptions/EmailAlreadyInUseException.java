package uk.ac.sheffield.com2008.exceptions;

public class EmailAlreadyInUseException extends Exception {
    public EmailAlreadyInUseException(String email) {
        super(email + " is already in use.");
    }
}
