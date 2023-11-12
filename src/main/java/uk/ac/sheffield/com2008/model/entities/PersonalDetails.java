package uk.ac.sheffield.com2008.model.entities;

import uk.ac.sheffield.com2008.model.domain.BankingCard;

public class PersonalDetails {
    private String forename;
    private String surname;
    private BankingCard bankingCard;

    public PersonalDetails(String forename, String surname, BankingCard bankingCard) {
        this.forename = forename;
        this.surname = surname;
        this.bankingCard = bankingCard;
    }

    public BankingCard getBankingCard() {
        return bankingCard;
    }

    public void setBankingCard(BankingCard bankingCard) {
        this.bankingCard = bankingCard;
    }

    public PersonalDetails(String forename, String surname) {
        this(forename, surname, null);
    }

    public void changeForename(String forename) {
        this.forename = forename;
    }

    public void changeSurname(String surname) {
        this.surname = surname;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }
}
