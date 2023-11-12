package uk.ac.sheffield.com2008.model.entities;

public class Address {
    private int houseNumber;
    private String flatIdentifier;
    private String street;
    private String city;
    private String postCode;

    public Address(int houseNumber, String flatIdentifier, String street, String city, String postCode) {
        this.houseNumber = houseNumber;
        this.flatIdentifier = flatIdentifier;
        this.street = street;
        this.city = city;
        this.postCode = postCode;
    }

    public Address(int houseNumber, String street, String city, String postCode) {
        this(houseNumber, null, street, city, postCode);
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getFlatIdentifier() {
        return flatIdentifier;
    }

    public void setFlatIdentifier(String flatIdentifier) {
        this.flatIdentifier = flatIdentifier;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
