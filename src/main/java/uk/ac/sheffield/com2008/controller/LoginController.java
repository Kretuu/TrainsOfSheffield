package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.view.LoginView;

/**
 * Controller responsible for Login Screen.
 */
public class LoginController extends ViewController{

    public LoginView loginView;
    public LoginController(){
        loginView = new LoginView(this);
        setFrameContent(loginView);
    }

    public void login(String email, String password){
        System.out.println(email);
        System.out.println(password);
    }
}
