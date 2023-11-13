package uk.ac.sheffield.com2008.exceptions;

public class IncorrectLoginCredentialsException extends Exception {
    public IncorrectLoginCredentialsException() {
        super("Email or password are incorrect.");
    }
}
