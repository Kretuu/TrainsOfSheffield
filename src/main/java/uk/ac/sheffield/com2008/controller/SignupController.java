package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.exceptions.EmailAlreadyInUseException;
import uk.ac.sheffield.com2008.model.domain.AuthenticationManager;
import uk.ac.sheffield.com2008.view.auth.SignupView;

public class SignupController extends ViewController {
    private final SignupView signupView;
    private final AuthenticationManager authenticationManager;

    public SignupController() {
        signupView = new SignupView(this);
        authenticationManager = new AuthenticationManager();
        setFrameContent(signupView);
    }

    public void signup(String email, char[] password, String firstname, String surname) {
        try {
            authenticationManager.registerUser(email, password, firstname, surname);
            new BrowseItemsController();
        } catch (EmailAlreadyInUseException e) {
            signupView.updateErrorMessage(e.getMessage());
        }
    }
}
