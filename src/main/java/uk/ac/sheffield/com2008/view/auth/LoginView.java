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

//        setLayout(new GridBagLayout());
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.anchor = GridBagConstraints.CENTER;
//
//        // "Login" label
//        JLabel loginLabel = new JLabel("Login");
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 2;
//        add(loginLabel, gbc);
//
//        // Text input field
//        JTextField emailField = new JTextField(20);
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.gridwidth = 1;
//        add(emailField, gbc);
//
//        // Password input field
//        JPasswordField passwordField = new JPasswordField(20);
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        add(passwordField, gbc);
//
//        // Button panel
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for buttonPanel
//
//        // Register button
//        JButton registerButton = new JButton("Register");
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 0.5; // Set weightx to make buttons equally sized
//        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
//        registerButton.addActionListener(e -> loginController.getNavigation().navigate(Navigation.SIGNUP));
//        buttonPanel.add(registerButton, gbc);
//
//        // Login button
//        JButton loginButton = new JButton("Login");
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        buttonPanel.add(loginButton, gbc);
//        // Adding ActionListener as an anonymous function
//        loginButton.addActionListener(e -> loginController.login(emailField.getText(), passwordField.getPassword()));
//
//        // Add the button panel below the text fields
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.gridwidth = 2;
//        gbc.weightx = 0; // Reset weightx for the container
//        gbc.fill = GridBagConstraints.NONE; // Reset fill
//        add(buttonPanel, gbc);
//        add(buttonPanel, gbc);
    }

    private void createTextFields() {
        CustomInputField email = new CustomInputField("Email", this::updateButtonState);
        email.addToPanel(panel);
        inputFields.put("email", email);

        CustomInputField password = new CustomInputField("Password", this::updateButtonState, true);
        password.addToPanel(panel);
        inputFields.put("password", password);
    }

    public void updateErrorMessage(String message) {
        errorMessage.setText("Error: " + message);
    }

    private void updateButtonState() {
        submitButton.setEnabled(inputFields.values().stream().allMatch(CustomInputField::isValid));
    }
}
