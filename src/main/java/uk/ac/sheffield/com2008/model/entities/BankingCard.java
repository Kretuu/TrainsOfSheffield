package uk.ac.sheffield.com2008.model.entities;

import java.util.Date;

public class BankingCard {
    private final String holderName;
    private final Date expiryDate;
    private String number;
    private String cvv;

    public BankingCard(String holderName, String number, Date expiryDate, String cvv) {
        this.holderName = holderName;
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public Boolean checkValidity() {
        return (expiryDate.getTime() - new Date().getTime()) >= 0;
    }

    public String getHolderName() {
        return holderName;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
