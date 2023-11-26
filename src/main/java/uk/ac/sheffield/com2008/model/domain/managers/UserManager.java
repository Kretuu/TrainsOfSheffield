package uk.ac.sheffield.com2008.model.domain.managers;

import uk.ac.sheffield.com2008.exceptions.UserHasNoBankDetailsException;
import uk.ac.sheffield.com2008.model.dao.BankingDetailsDAO;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.util.EncryptionManager;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserManager {
    public static void updateUserBankDetails(User user, BankingCard card, char[] password) {
        String salt = UserDAO.fetchUserSalt(user);

        try {
            card.setNumber(EncryptionManager.encrypt(card.getNumber(), password, salt));
            card.setCvv(EncryptionManager.encrypt(card.getCvv(), password, salt));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        if(BankingDetailsDAO.hasUserBankingDetails(user)) {
            BankingDetailsDAO.updateBankingDetails(card, user);
        } else {
            BankingDetailsDAO.createBankingDetails(card, user);
        }
    }

    public static BankingCard fetchUserBankDetails(User user, char[] password) throws UserHasNoBankDetailsException {
        BankingCard bankingCard = BankingDetailsDAO.getUserBankingCard(user);
        if(bankingCard == null)
            throw new UserHasNoBankDetailsException();

        String salt = UserDAO.fetchUserSalt(user);

        try {
            bankingCard.setNumber(EncryptionManager.decrypt(bankingCard.getNumber(), password, salt));
            bankingCard.setCvv(EncryptionManager.decrypt(bankingCard.getCvv(), password, salt));
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return bankingCard;
    }
}