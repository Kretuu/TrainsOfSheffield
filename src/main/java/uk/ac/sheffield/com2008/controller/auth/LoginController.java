package uk.ac.sheffield.com2008.controller.auth;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.IncorrectLoginCredentialsException;
import uk.ac.sheffield.com2008.model.domain.managers.AuthenticationManager;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.auth.LoginView;

import java.sql.SQLException;

/**
 * Controller responsible for Login Screen.
 */
public class LoginController extends ViewController {
    private final LoginView loginView;
    public LoginController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new LoginView(this);
        loginView = (LoginView) view;
    }

    @Override
    public void onNavigateLeave() {
        loginView.purgeTextFields();
    }

    public void login(String email, char[] password){
        try {
            AuthenticationManager.loginUser(email, password);
            loginView.updateErrorMessage(null);
            navigation.navigate(Navigation.CUSTOMER);
        } catch (IncorrectLoginCredentialsException e) {
            loginView.updateErrorMessage(e.getMessage());
        } catch (SQLException e) {
            loginView.updateErrorMessage("Cannot connect to database.");
        }
    }
}
