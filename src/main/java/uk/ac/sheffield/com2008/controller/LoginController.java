package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.exceptions.IncorrectLoginCredentialsException;
import uk.ac.sheffield.com2008.model.domain.AuthenticationManager;
import uk.ac.sheffield.com2008.view.auth.LoginView;

/**
 * Controller responsible for Login Screen.
 */
public class LoginController extends ViewController{

    public LoginView loginView;
    private final AuthenticationManager authenticationManager;
    public LoginController(){
        loginView = new LoginView(this);
        authenticationManager = new AuthenticationManager();
        setFrameContent(loginView);
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
        new BrowseItemsController();
    }
}
