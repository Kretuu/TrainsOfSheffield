package uk.ac.sheffield.com2008.controller.auth;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.IncorrectLoginCredentialsException;
import uk.ac.sheffield.com2008.model.domain.AuthenticationManager;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.auth.LoginView;

/**
 * Controller responsible for Login Screen.
 */
public class LoginController extends ViewController {
    private final LoginView loginView;
    private final AuthenticationManager authenticationManager;
    public LoginController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new LoginView(this);
        loginView = (LoginView) view;
        authenticationManager = new AuthenticationManager();
    }
  
    public void login(String email, char[] password){
        try {
            authenticationManager.loginUser(email, password);
            navigation.navigate(Navigation.CUSTOMER);
        } catch (IncorrectLoginCredentialsException e) {
            loginView.updateErrorMessage(e.getMessage());
        }
    }
}
