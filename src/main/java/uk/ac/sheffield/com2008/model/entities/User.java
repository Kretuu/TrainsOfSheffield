package uk.ac.sheffield.com2008.model.entities;

import uk.ac.sheffield.com2008.model.domain.Basket;

public class User {
    private Basket basket;
    private PersonalDetails personalDetails;

    public User(Basket basket, PersonalDetails personalDetails) {
        this.basket = basket;
        this.personalDetails = personalDetails;
    }

    public User(PersonalDetails personalDetails) {
        this(null, personalDetails);
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }
}
