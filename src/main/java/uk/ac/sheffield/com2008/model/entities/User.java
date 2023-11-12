package uk.ac.sheffield.com2008.model.entities;

import uk.ac.sheffield.com2008.model.domain.Basket;

public class User {
    private int id;
    private String email;
    private String password;
    private Basket basket;
    private PersonalDetails personalDetails;

    public User(int id, String email, String password, Basket basket, PersonalDetails personalDetails) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.basket = basket;
        this.personalDetails = personalDetails;
    }

    public User(int id, String email, String password, PersonalDetails personalDetails) {
        this(id, email, password, null, personalDetails);
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
