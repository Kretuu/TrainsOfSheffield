package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.UserHasNoBankDetailsException;
import uk.ac.sheffield.com2008.model.domain.managers.UserManager;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.user.ManageProfileView;

public class ManageProfileController extends ViewController {
    public ManageProfileController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new ManageProfileView(this);
    }

    public BankingCard getBankingCard(char[] password) {
        User user = AppSessionCache.getInstance().getUserLoggedIn();
        try {
            return UserManager.fetchUserBankDetails(user, password);
        } catch (UserHasNoBankDetailsException e) {
            return null;
        }
    }

}
