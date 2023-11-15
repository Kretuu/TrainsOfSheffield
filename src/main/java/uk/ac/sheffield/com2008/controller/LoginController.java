package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.exceptions.IncorrectLoginCredentialsException;
import uk.ac.sheffield.com2008.model.domain.AuthenticationManager;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.auth.LoginView;

/**
 * Controller responsible for Login Screen.
 */
public class LoginController extends ViewController {
    private final AuthenticationManager authenticationManager;
    public LoginController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new LoginView(this);
        authenticationManager = new AuthenticationManager();
    }
  
    public void login(String email, char[] password){
        //Validate all fields

        try {
            authenticationManager.loginUser(email, password);
        } catch (IncorrectLoginCredentialsException e) {
            //We need to view this message in the UI
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Logged in");
        //TRANSITION to Browse Items view
        navigation.navigate(Navigation.CUSTOMER);
    }
}
