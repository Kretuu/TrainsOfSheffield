package uk.ac.sheffield.com2008.view.auth;

import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.controller.auth.LoginController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.components.CustomInputField;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LoginView extends AuthView {

    private final LoginController loginController;
    private JButton submitButton;
    private JLabel errorMessage;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();
    public LoginView(LoginController loginController){
        super();
        this.loginController = loginController;
        initializeUI();
    }

    public void initializeUI(){
        errorMessage = new JLabel(" ");
        errorMessage.setForeground(Colors.TEXT_FIELD_ERROR);
        panel.add(errorMessage);

        submitButton = new JButton("Login");
        submitButton.setEnabled(false);

        JButton registerButton = new JButton("Register");
        createTextFields();

        JPanel buttonsPanel = new JPanel(new GridLayout(1,2,10,0));
        buttonsPanel.add(registerButton);
        buttonsPanel.add(submitButton);
        panel.add(buttonsPanel);


        submitButton.addActionListener(e -> {
            String email = inputFields.get("email").getjTextField().getText();
            char[] password = inputFields.get("password").getjPasswordField().getPassword();

            loginController.login(email, password);
        });
        registerButton.addActionListener(e -> loginController.getNavigation().navigate(Navigation.SIGNUP));
    }

    private void createTextFields() {
        CustomInputField email = new CustomInputField("Email", this::updateButtonState, false);
        email.addToPanel(panel);
        inputFields.put("email", email);

        CustomInputField password = new CustomInputField(
                "Password", this::updateButtonState, false, true
        );
        password.addToPanel(panel);
        inputFields.put("password", password);
    }

    public void updateErrorMessage(String message) {
        if(message == null) {
            errorMessage.setText(" ");
            return;
        }
        errorMessage.setText("Error: " + message);
    }

    public void purgeTextFields() {
        inputFields.values().forEach(CustomInputField::purgeField);
    }

    private void updateButtonState() {
        submitButton.setEnabled(inputFields.values().stream().allMatch(CustomInputField::isValid));
    }
}
