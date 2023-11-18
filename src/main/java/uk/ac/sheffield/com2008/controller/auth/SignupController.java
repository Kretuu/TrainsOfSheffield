package uk.ac.sheffield.com2008.controller.auth;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.EmailAlreadyInUseException;
import uk.ac.sheffield.com2008.model.domain.managers.AuthenticationManager;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.auth.SignupView;

public class SignupController extends ViewController {
    private final AuthenticationManager authenticationManager;
    private final SignupView signupView;

    public SignupController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new SignupView(this);
        signupView = (SignupView) view;
        authenticationManager = new AuthenticationManager();
    }

    public void signup(String email, char[] password, String firstname, String surname) {
        try {
            authenticationManager.registerUser(email, password, firstname, surname);
            navigation.navigate(Navigation.CUSTOMER);
        } catch (EmailAlreadyInUseException e) {
            signupView.updateErrorMessage(e.getMessage());
        }
    }

}
