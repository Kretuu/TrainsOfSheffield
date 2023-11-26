package uk.ac.sheffield.com2008.model.entities;

public class PersonalDetails {
    private String forename;
    private String surname;

    public PersonalDetails(String forename, String surname) {
        this.forename = forename;
        this.surname = surname;
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
