package uk.ac.sheffield.com2008.exceptions;

public class UserHasNoBankDetailsException extends Exception {
    public UserHasNoBankDetailsException() {
        super("User has no bank details set.");
    }
}
