package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.entities.Address;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    public User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        User user;

        PersonalDetails details = new PersonalDetails(
                resultSet.getString("forename"),
                resultSet.getString("surname")
        );

        String postcode = resultSet.getString("Users.postcode");
        Address address = null;
        if (postcode != null) {
            address = new Address(
                    resultSet.getString("Users.houseNumber"),
                    resultSet.getString("roadName"),
                    resultSet.getString("cityName"),
                    postcode
            );
        }
        user = new User(
                resultSet.getString("uuid"),
                resultSet.getString("email"),
                details,
                resultSet.getString("roles"),
                address
        );

        return user;
    }
}
