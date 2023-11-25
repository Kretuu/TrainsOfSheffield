package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.entities.BankingCard;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BankingCardMapper implements RowMapper<BankingCard> {
    @Override
    public BankingCard mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new BankingCard(
                resultSet.getString("holderName"),
                resultSet.getString("BD.cardNumber"),
                resultSet.getDate("expiryDate"),
                resultSet.getString("cvv")
        );
    }
}
