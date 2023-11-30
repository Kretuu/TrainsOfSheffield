package uk.ac.sheffield.com2008.controller.auth;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.domain.managers.UserManager;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.auth.ProvideAddressView;

import java.sql.SQLException;

public class ProvideAddressController extends ViewController {
    private final ProvideAddressView provideAddressView;

    public ProvideAddressController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new ProvideAddressView(this);
        provideAddressView = (ProvideAddressView) view;
    }

    @Override
    public void onNavigateTo() {
        provideAddressView.updateUser();
    }

    public void updateUserDetails(User user) {
        try {
            UserManager.updateUser(user);
            provideAddressView.purgeTextFields();
            navigation.navigate(Navigation.CUSTOMER);
        } catch (SQLException e) {
            provideAddressView.updateErrorMessage("Cannot connect to database.");
        }
    }
}
