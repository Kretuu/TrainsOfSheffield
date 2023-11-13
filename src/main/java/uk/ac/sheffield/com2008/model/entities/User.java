package uk.ac.sheffield.com2008.model.entities;

public class User {
    private final String uuid;
    private final String salt;
    private String email;
    private String passwordHash;
    private Basket basket;
    private PersonalDetails personalDetails;

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getUuid() {
        return uuid;
    }

    public User(
            String uuid, String email,
            String passwordHash, String salt, Basket basket,
            PersonalDetails personalDetails) {
        this.uuid = uuid;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.basket = basket;
        this.personalDetails = personalDetails;
    }

    public User(String uuid, String email, String password, String salt,  PersonalDetails personalDetails) {
        this(uuid, email, password, salt, null, personalDetails);
    }

    public Basket getBasket() {
        return basket;
    }

    public String getSalt() {
        return salt;
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
