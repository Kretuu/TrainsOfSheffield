package uk.ac.sheffield.com2008.model.entities;

import java.util.Date;

public class BankingCard {
    private String holderName;
    private String number;
    private Date expiryDate;
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

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }
}
