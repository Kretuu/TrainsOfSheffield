package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.domain.data.AuthUser;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.AuthUserMapper;
import uk.ac.sheffield.com2008.model.mappers.UserMapper;
import uk.ac.sheffield.com2008.util.HashedPasswordGenerator;

import java.sql.SQLException;
import java.util.List;

//Mediator pattern - connection between User entity and database. Fetching, updating, deleting etc. all information
//related to User entity.
public class UserDAO {
    public User getUserById(int id) {
        return getUserByField("userId", id);
    }

    public User getUserByEmail(String email) {
        return new UserDAO().getUserByField("email", email);
    }

    private User getUserByField(String fieldName, Object value) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT uuid, email, forename, surname, Users.cardNumber, houseNumber, postcode, ")
                .append("holderName, expiryDate, cvv FROM Users LEFT OUTER JOIN BankingDetails ON ")
                .append("Users.cardNumber=BankingDetails.cardNumber WHERE ").append(fieldName).append(" = ?");
        String query = queryBuilder.toString();

        List<User> users;
        try {
            UserMapper mapper = new UserMapper();
            users = DatabaseConnectionHandler.select(mapper, query, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(users.isEmpty()) return null;

        return users.get(0);
    }

    public User verifyPassword(String userEmail, char[] password) {
        String query = "SELECT password, salt FROM Users WHERE email = ?";

        List<AuthUser> authUsers;
        try {
            AuthUserMapper mapper = new AuthUserMapper();
            authUsers = DatabaseConnectionHandler.select(mapper, query, userEmail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(authUsers.isEmpty()) return null;

        AuthUser authUser = authUsers.get(0);
        String passwordHash = HashedPasswordGenerator.hashPassword(password, authUser.salt());

        if(authUser.passwordHash().equals(passwordHash)) {
            return getUserByEmail(userEmail);
        }
        return null;
    }

    public void createUser(User user, AuthUser authUser) {
        String query = "INSERT INTO Users (uuid, email, password, salt, forename, surname) VALUES (?, ?, ?, ?, ?, ?)";
        PersonalDetails details = user.getPersonalDetails();

        try {
            DatabaseConnectionHandler.insert(
                    query,
                    user.getUuid(),
                    user.getEmail(),
                    authUser.passwordHash(),
                    authUser.salt(),
                    details.getForename(),
                    details.getSurname()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
