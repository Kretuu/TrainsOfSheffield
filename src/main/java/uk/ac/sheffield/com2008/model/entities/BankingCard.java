package uk.ac.sheffield.com2008.model.entities;

import java.util.Date;

public class BankingCard {
    private String holderName;
    private Long number;
    private Date expiryDate;
    private int cvv;

    public BankingCard(String holderName, Long number, Date expiryDate, int cvv) {
        this.holderName = holderName;
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public Boolean checkValidity() {
        //TODO
        return true;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
}