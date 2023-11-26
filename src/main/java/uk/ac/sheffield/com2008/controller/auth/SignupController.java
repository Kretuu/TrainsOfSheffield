package uk.ac.sheffield.com2008.controller.auth;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.EmailAlreadyInUseException;
import uk.ac.sheffield.com2008.model.domain.managers.AuthenticationManager;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.auth.SignupView;

import java.sql.SQLException;

public class SignupController extends ViewController {
    private final SignupView signupView;

    public SignupController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new SignupView(this);
        signupView = (SignupView) view;
    }

    public void signup(String email, char[] password, String firstname, String surname) {
        try {
            AuthenticationManager.registerUser(email, password, firstname, surname);
            signupView.updateErrorMessage(null);
            navigation.navigate(Navigation.PROVIDE_ADDRESS);
        } catch (EmailAlreadyInUseException e) {
            signupView.updateErrorMessage(e.getMessage());
        } catch (SQLException e) {
            signupView.updateErrorMessage("Cannot connect to database.");
        }
    }

    @Override
    public void onNavigateLeave() {
        signupView.purgeTextFields();
    }
}
