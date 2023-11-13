package uk.ac.sheffield.com2008.view;

import uk.ac.sheffield.com2008.controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends View{

    private LoginController loginController;
    public LoginView(LoginController loginController){
        this.loginController = loginController;
        InitializeUI();
    }

    public void InitializeUI(){
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // "Login" label
        JLabel loginLabel = new JLabel("Login");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(loginLabel, gbc);

        // Text input field
        JTextField emailField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(emailField, gbc);

        // Password input field
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for buttonPanel

        // Register button
        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5; // Set weightx to make buttons equally sized
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
        registerButton.addActionListener(e -> {
            // Add logic for register button click
            // You can switch to another state or perform other actions
        });
        buttonPanel.add(registerButton, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(loginButton, gbc);
        // Adding ActionListener as an anonymous function
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginController.login(emailField.getText(), passwordField.getPassword());
            }
        });

        // Add the button panel below the text fields
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0; // Reset weightx for the container
        gbc.fill = GridBagConstraints.NONE; // Reset fill
        add(buttonPanel, gbc);
        add(buttonPanel, gbc);
    }
}
