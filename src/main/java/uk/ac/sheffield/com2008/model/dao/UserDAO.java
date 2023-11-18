package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;
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
        String query = "SELECT * FROM Users LEFT OUTER JOIN BankingDetails " +
                "ON Users.cardNumber=BankingDetails.cardNumber WHERE " + fieldName + " = ?";

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
        User user = getUserByEmail(userEmail);
        if(user == null) {
            return null;
        }
        String passwordHash = HashedPasswordGenerator.hashPassword(password, user.getSalt());

        if(user.getPasswordHash().equals(passwordHash)) {
            return user;
        }
        return null;
    }

    public void createUser(User user) {
        String query = "INSERT INTO Users (uuid, email, password, salt, forename, surname) VALUES (?, ?, ?, ?, ?, ?)";
        PersonalDetails details = user.getPersonalDetails();

        try {
            DatabaseConnectionHandler.insert(
                    query,
                    user.getUuid(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getSalt(),
                    details.getForename(),
                    details.getSurname()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
