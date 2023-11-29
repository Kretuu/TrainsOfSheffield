package uk.ac.sheffield.com2008.exceptions;

public class BankDetailsNotValidException extends Exception {
    public BankDetailsNotValidException() {
        super("Banking details are not valid");
    }
}
