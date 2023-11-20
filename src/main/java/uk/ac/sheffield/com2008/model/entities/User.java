package uk.ac.sheffield.com2008.model.entities;

public class User {
    private final String uuid;
    private String email;
    private Order basket;
    private PersonalDetails personalDetails;

    public User(String uuid, String email, Order basket, PersonalDetails personalDetails) {
        this.uuid = uuid;
        this.email = email;
        this.basket = basket;
        this.personalDetails = personalDetails;
    }

    public User(String uuid, String email, PersonalDetails personalDetails) {
        this(uuid, email, null, personalDetails);
    }

    public String getEmail() {
        return email;
    }

    public String getUuid() {
        return uuid;
    }

    public Order getBasket() {
        return basket;
    }


    public void setBasket(Order basket) {
        this.basket = basket;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
