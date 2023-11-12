package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.UserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

//Mediator pattern - connection between User entity and database. Fetching, updating, deleting etc. all information
//related to User entity.
public class UserDAO {
    public User getUserById(int id) {
        return getUserByField("userId", id);
    }

    public static User getUserByEmail(String email) {
        return new UserDAO().getUserByField("email", email);
    }

    private User getUserByField(String fieldName, Object value) {
        String query = "SELECT * FROM User LEFT OUTER JOIN BankingDetail " +
                "ON User.cardNumber=BankingDetail.cardNumber WHERE " + fieldName + " = ?";
        User user = null;

        try {
            ResultSet resultSet = DatabaseConnectionHandler.select(query, value);
            user = UserMapper.mapResultSetToUser(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
