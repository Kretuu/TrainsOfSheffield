package uk.ac.sheffield.com2008.model.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private final String uuid;
    private String email;
    private Order basket;
    private PersonalDetails personalDetails;

    private Address address;
    private final List<Role> roles;

    public User(String uuid, String email, Order basket, PersonalDetails personalDetails, String rolesString, Address address) {
        this.uuid = uuid;
        this.email = email;
        this.basket = basket;
        this.personalDetails = personalDetails;
        this.roles = Role.parseRoles(rolesString);
        this.address = address;
    }

    public User(String uuid, String email, PersonalDetails personalDetails, String rolesString, Address address) {
        this(uuid, email, null, personalDetails, rolesString, address);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public void addRole(Role role) {
        if(!roles.contains(role)) roles.add(role);
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
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

        public static String parseRolesToString(List<Role> roles) {
            if(roles.isEmpty()) return null;

            StringBuilder builder = new StringBuilder();
            roles.forEach(role -> builder.append(role.toString()).append(";"));
            builder.setLength(builder.length() - 1);
            return builder.toString();
        }
    }
}
