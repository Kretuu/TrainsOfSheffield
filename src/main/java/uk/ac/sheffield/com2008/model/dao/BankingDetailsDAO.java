package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.BankingCardMapper;

import java.sql.SQLException;
import java.util.List;

public class BankingDetailsDAO {
    public static void createBankingDetails(BankingCard card, User user) throws SQLException {
        String query = "INSERT INTO BankingDetails (cardNumber, holderName, expiryDate, cvv) VALUES (?, ?, ?, ?)";
        String updateUserQuery = "UPDATE Users SET cardNumber = ? WHERE uuid = ?";

        DatabaseConnectionHandler.insert(query, card.getNumber(), card.getHolderName(), card.getExpiryDate(), card.getCvv());
        DatabaseConnectionHandler.update(updateUserQuery, card.getNumber(), user.getUuid());
    }

    public static void updateBankingDetails(BankingCard card, User user) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE BankingDetails BD INNER JOIN Users U ON U.cardNumber = BD.cardNumber ");
        queryBuilder.append("SET BD.cardNumber = ?, holderName = ?, expiryDate = ?, cvv = ? WHERE U.uuid = ?");
        String query = queryBuilder.toString();

        DatabaseConnectionHandler.update(query, card.getNumber(), card.getHolderName(), card.getExpiryDate(), card.getCvv(), user.getUuid());
    }

    public static boolean hasUserBankingDetails(User user) throws SQLException {
        return getUserBankingCardByUuid(user.getUuid()) != null;
    }

    public static BankingCard getUserBankingCardByUuid(String uuid) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM BankingDetails BD INNER JOIN Users U ON U.cardNumber = BD.cardNumber ");
        queryBuilder.append("WHERE U.uuid = ?");
        String query = queryBuilder.toString();

        BankingCardMapper mapper = new BankingCardMapper();
        List<BankingCard> bankingCards = DatabaseConnectionHandler.select(mapper, query, uuid);

        if (bankingCards.isEmpty()) return null;
        return bankingCards.get(0);
    }
}
