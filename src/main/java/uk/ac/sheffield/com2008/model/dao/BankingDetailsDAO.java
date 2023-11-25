package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.BankingCardMapper;

import java.sql.SQLException;
import java.util.List;

public class BankingDetailsDAO {
    public static void createBankingDetails(BankingCard card, User user) {
        String query = "INSERT INTO BankingDetails (cardNumber, holderName, expiryDate, cvv) VALUES (?, ?, ?, ?)";
        String updateUserQuery = "UPDATE Users SET cardNumber = ? WHERE uuid = ?";

        try {
            DatabaseConnectionHandler.insert(query, card.getNumber(), card.getHolderName(), card.getExpiryDate(), card.getCvv());
            DatabaseConnectionHandler.update(updateUserQuery, card.getNumber(), user.getUuid());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void updateBankingDetails(BankingCard card, User user) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE BankingDetails BD INNER JOIN Users U ON U.cardNumber = BD.cardNumber ");
        queryBuilder.append("SET BD.cardNumber = ?, holderName = ?, expiryDate = ?, cvv = ? WHERE U.uuid = ?");
        String query = queryBuilder.toString();

        try {
            DatabaseConnectionHandler.update(query, card.getNumber(), card.getHolderName(), card.getExpiryDate(), card.getCvv(), user.getUuid());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean hasUserBankingDetails(User user) {
        return getUserBankingCard(user) != null;
    }


    public static BankingCard getUserBankingCard(User user) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM BankingDetails BD INNER JOIN Users U ON U.cardNumber = BD.cardNumber ");
        queryBuilder.append("WHERE U.uuid = ?");
        String query = queryBuilder.toString();

        List<BankingCard> bankingCards;
        try {
            BankingCardMapper mapper = new BankingCardMapper();
            bankingCards = DatabaseConnectionHandler.select(mapper, query, user.getUuid());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(bankingCards.isEmpty()) return null;
        return bankingCards.get(0);
    }
}
