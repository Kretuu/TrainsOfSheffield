package uk.ac.sheffield.com2008.model.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private final String uuid;
    private String email;
    private Order basket;
    private PersonalDetails personalDetails;
    private final List<Role> roles;

    public User(String uuid, String email, Order basket, PersonalDetails personalDetails, String rolesString) {
        this.uuid = uuid;
        this.email = email;
        this.basket = basket;
        this.personalDetails = personalDetails;
        this.roles = Role.parseRoles(rolesString);
    }

    public User(String uuid, String email, PersonalDetails personalDetails, String rolesString) {
        this(uuid, email, null, personalDetails, rolesString);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public enum Role {
        CUSTOMER, STAFF, MANAGER;

        public static List<Role> parseRoles(String rolesString) {
            if (rolesString == null) return new ArrayList<>();
            return Arrays.stream(rolesString.split(";")).map(Role::valueOf).toList();
        }
    }
}
