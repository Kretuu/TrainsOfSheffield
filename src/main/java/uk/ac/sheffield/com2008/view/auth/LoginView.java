package uk.ac.sheffield.com2008.view.auth;

import uk.ac.sheffield.com2008.controller.auth.LoginController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.components.InputForm;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LoginView extends AuthView {

    private final LoginController loginController;
    private final InputForm inputForm;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();
    public LoginView(LoginController loginController){
        super();
        this.loginController = loginController;
        this.inputForm = createInputForm();

        JPanel header = new JPanel();
        JLabel label = new JLabel("Log in");
        label.setFont(new Font(null, Font.BOLD, 16));
        header.add(label);

        panel.add(header);

        panel.add(inputForm);
    }

    private InputForm createInputForm() {
        return new InputForm(this, "Log in", "Register") {
            @Override
            protected void createTextFields(JPanel panel) {
                CustomInputField email = new CustomInputField(
                        "Email", this::updateSubmitButtonState, false);
                email.addToPanel(panel);
                inputFields.put("email", email);

                CustomInputField password = new CustomInputField(
                        "Password", this::updateSubmitButtonState, false, true
                );
                password.addToPanel(panel);
                inputFields.put("password", password);
            }

            @Override
            protected void onSubmit() {
                String email = inputFields.get("email").getjTextField().getText();
                char[] password = inputFields.get("password").getjPasswordField().getPassword();

                loginController.login(email, password);
            }

            @Override
            protected void onCancel() {
                loginController.getNavigation().navigate(Navigation.SIGNUP);
            }

            @Override
            protected boolean submitEnabled() {
                return inputFields.values().stream().allMatch(CustomInputField::isValid);
            }
        };
    }


    public void updateErrorMessage(String message) {
        inputForm.updateErrorMessage(message);
    }

    public void purgeTextFields() {
        inputFields.values().forEach(CustomInputField::purgeField);
    }
}
