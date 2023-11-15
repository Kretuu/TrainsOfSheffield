package uk.ac.sheffield.com2008.view.auth;

import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.controller.auth.SignupController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.util.ui.CustomInputField;
import uk.ac.sheffield.com2008.util.ValidationManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SignupView extends AuthView {
    private final SignupController signupController;
    private JButton submitButton;
    private JLabel errorMessage;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();

    public SignupView(SignupController signupController) {
        super();
        this.signupController = signupController;
        initializeUI();
    }

    private void initializeUI() {
        errorMessage = new JLabel(" ");
        errorMessage.setForeground(Colors.TEXT_FIELD_ERROR);
        panel.add(errorMessage);

        submitButton = new JButton("Register");
        submitButton.setEnabled(false);

        JButton cancelButton = new JButton("Cancel");
        createTextFields();

        JPanel buttonsPanel = new JPanel(new GridLayout(1,2,10,0));
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(submitButton);
        panel.add(buttonsPanel);


        submitButton.addActionListener(e -> {
            String email = inputFields.get("email").getjTextField().getText();
            char[] password = inputFields.get("password").getjPasswordField().getPassword();
            String firstname = inputFields.get("firstname").getjTextField().getText();
            String surname = inputFields.get("surname").getjTextField().getText();

            signupController.signup(email, password, firstname, surname);
            // Add logic for register button click
            // You can switch to another state or perform other actions
        });
        cancelButton.addActionListener(e -> signupController.getNavigation().navigate(Navigation.LOGIN));
    }

    private void createTextFields() {
        CustomInputField email = new CustomInputField("Email", this::updateButtonState);
        email.setValidationFunction(() -> ValidationManager.validateEmail(email.getjTextField().getText()));
        email.addToPanel(panel);
        inputFields.put("email", email);

        CustomInputField password = new CustomInputField("Password", this::updateButtonState, true);
        password.setValidationFunction(() -> ValidationManager.validatePassword(password.getjTextField().getText()));
        password.addToPanel(panel);
        inputFields.put("password", password);

        CustomInputField confirmPassword = new CustomInputField(
                "Confirm Password", this::updateButtonState, true
        );
        confirmPassword.setValidationFunction(() -> ValidationManager.validateConfirmPassword(
                password.getjTextField().getText(), confirmPassword.getjTextField().getText()
        ));
        confirmPassword.addToPanel(panel);
        inputFields.put("confirmPassword", confirmPassword);

        CustomInputField firstname = new CustomInputField("First Name", this::updateButtonState);
        firstname.addToPanel(panel);
        inputFields.put("firstname", firstname);

        CustomInputField surname = new CustomInputField("Last Name", this::updateButtonState);
        surname.addToPanel(panel);
        inputFields.put("surname", surname);
    }

    public void updateErrorMessage(String message) {
        errorMessage.setText("Error: " + message);
    }


    private void updateButtonState() {
        submitButton.setEnabled(inputFields.values().stream().allMatch(CustomInputField::isValid));
    }

}
