package uk.ac.sheffield.com2008.model.domain;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.exceptions.EmailAlreadyInUseException;
import uk.ac.sheffield.com2008.exceptions.IncorrectLoginCredentialsException;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.util.HashedPasswordGenerator;
import uk.ac.sheffield.com2008.util.UUIDGenerator;

public class AuthenticationManager {
    private final UserDAO userDAO;

    public AuthenticationManager(){
        userDAO = new UserDAO();
    }

    public void loginUser(String userEmail, char[] password) throws IncorrectLoginCredentialsException {
        User user = userDAO.verifyPassword(userEmail, password);
        if(user == null) {
            throw new IncorrectLoginCredentialsException();
        }
        AppSessionCache.getInstance().setUserLoggedIn(user);

        //GET THEIR BASKET AND ASSIGN IT TO THE USER
        Order usersBasket = OrderDAO.getUsersBasket(user);
        user.setBasket(usersBasket);
    }

    public void registerUser(String userEmail, char[] password, String forename, String surname) throws EmailAlreadyInUseException {
        User user = userDAO.getUserByEmail(userEmail);
        if(user != null) {
            throw new EmailAlreadyInUseException(userEmail);
        }

        PersonalDetails personalDetails = new PersonalDetails(forename, surname);
        String uuid = UUIDGenerator.generate();
        String salt = HashedPasswordGenerator.generateSalt();
        String hashedPassword = HashedPasswordGenerator.hashPassword(password, salt);
        user = new User(uuid, userEmail, hashedPassword, salt, personalDetails);
        userDAO.createUser(user);

        //GENERATE A NEW BLANK ORDER (BASKET) FOR THEM
        OrderDAO.createOrder(user);
    }
}
