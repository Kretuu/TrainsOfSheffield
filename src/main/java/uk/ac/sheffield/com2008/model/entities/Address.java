package uk.ac.sheffield.com2008.model.entities;

import java.util.Arrays;
import java.util.List;

public class Address {
    private int houseNumber;
    private String flatIdentifier;
    private String street;
    private String city;
    private String postCode;

    public Address() {}

    public Address(String combinedHouseNumber, String street, String city, String postCode) {
        List<String> combinedHouseNumberList = Arrays.stream(combinedHouseNumber.split("/")).toList();
        this.houseNumber = Integer.parseInt(combinedHouseNumberList.get(0));
        this.flatIdentifier = combinedHouseNumberList.size() > 1 ? combinedHouseNumberList.get(1) : null;
        this.street = street;
        this.city = city;
        this.postCode = postCode;
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
        this.flatIdentifier = flatIdentifier.isEmpty() ? null : flatIdentifier;
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

    public String getCombinedHouseNo() {
        if(flatIdentifier == null) return String.valueOf(houseNumber);
        return new StringBuilder().append(houseNumber).append("/").append(flatIdentifier).toString();
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
