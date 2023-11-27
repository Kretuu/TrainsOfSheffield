package uk.ac.sheffield.com2008.model.domain.managers;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.exceptions.EmailAlreadyInUseException;
import uk.ac.sheffield.com2008.exceptions.IncorrectLoginCredentialsException;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.domain.data.AuthUser;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.util.EncryptionManager;
import uk.ac.sheffield.com2008.util.UUIDGenerator;

import java.sql.SQLException;

public class AuthenticationManager {
    public static void loginUser(String userEmail, char[] password) throws IncorrectLoginCredentialsException, SQLException {
        User user = UserDAO.verifyPassword(userEmail, password);
        if(user == null) {
            throw new IncorrectLoginCredentialsException();
        }
        AppSessionCache.getInstance().setUserLoggedIn(user);

        OrderManager.updateUserBasket(user);
    }

    public static void registerUser(String userEmail, char[] password, String forename, String surname) throws EmailAlreadyInUseException, SQLException {
        User user = UserDAO.getUserByEmail(userEmail);
        if(user != null) {
            throw new EmailAlreadyInUseException(userEmail);
        }

        PersonalDetails personalDetails = new PersonalDetails(forename, surname);
        String uuid = UUIDGenerator.generate();
        String salt = EncryptionManager.generateSalt();
        String hashedPassword = EncryptionManager.hashPassword(password, salt);
        user = new User(uuid, userEmail, personalDetails, null, null);
        AuthUser authUser = new AuthUser(hashedPassword, salt);
        UserDAO.createUser(user, authUser);

        //GENERATE A NEW BLANK ORDER (BASKET) FOR THEM
        OrderManager.updateUserBasket(user);
        AppSessionCache.getInstance().setUserLoggedIn(user);
    }
}
