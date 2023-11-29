package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.BankDetailsEncryptionException;
import uk.ac.sheffield.com2008.exceptions.UserHasNoBankDetailsException;
import uk.ac.sheffield.com2008.model.domain.managers.UserManager;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.user.ManageProfileView;

import java.sql.SQLException;

public class ManageProfileController extends ViewController {
    private final ManageProfileView manageProfileView;
    public ManageProfileController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new ManageProfileView(this);
        manageProfileView = (ManageProfileView) view;
    }

    public BankingCard getBankingCard(char[] password) throws SQLException, BankDetailsEncryptionException {
        User user = AppSessionCache.getInstance().getUserLoggedIn();
        try {
            return UserManager.fetchUserBankDetails(user, password);
        } catch (UserHasNoBankDetailsException e) {
            return null;
        }
    }

    public void updateUserDetails(User user) {
        try {
            UserManager.updateUser(user);
        } catch (SQLException e) {
            manageProfileView.updateErrorMessage("Cannot connect to database.");
        }
    }

    @Override
    public void onNavigateTo() {
        manageProfileView.populateTextFields();
    }
}
