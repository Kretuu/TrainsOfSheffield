package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public static User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = null;
        long cardNumber = resultSet.getLong("Users.cardNumber");
        BankingCard bankingCard = null;
        if (cardNumber != 0) {
            bankingCard = new BankingCard(
                    resultSet.getString("holderName"),
                    cardNumber,
                    resultSet.getDate("expiryDate"),
                    resultSet.getInt("cvv")
            );
        }
        PersonalDetails details = new PersonalDetails(
                resultSet.getString("forename"),
                resultSet.getString("surname"),
                bankingCard
        );
        user = new User(
                resultSet.getString("uuid"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("salt"),
                details
        );

        return user;
    }
}
