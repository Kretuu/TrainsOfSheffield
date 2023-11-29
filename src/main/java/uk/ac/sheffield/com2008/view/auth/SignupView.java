package uk.ac.sheffield.com2008.view.auth;

import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.controller.auth.SignupController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.util.FieldsValidationManager;
import uk.ac.sheffield.com2008.view.components.InputForm;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SignupView extends AuthView {
    private final SignupController signupController;
    private final InputForm inputForm;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();

    public SignupView(SignupController signupController) {
        super();
        this.signupController = signupController;
        this.inputForm = createInputForm();

        JPanel header = new JPanel();
        JLabel label = new JLabel("Sign up");
        label.setFont(new Font(null, Font.BOLD, 16));
        header.add(label);

        panel.add(header);
        panel.add(inputForm);
    }

    private InputForm createInputForm() {
        return new InputForm(this, "Sign up", "Go back") {
            @Override
            protected void createTextFields(JPanel panel) {
                CustomInputField email = new CustomInputField(
                        "Email", this::updateSubmitButtonState, false);
                email.setValidationFunction(
                        () -> FieldsValidationManager.validateEmail(email.getjTextField().getText())
                );
                email.addToPanel(panel);
                inputFields.put("email", email);

                CustomInputField password = new CustomInputField(
                        "Password", this::updateSubmitButtonState, false, true);
                password.setValidationFunction(
                        () -> FieldsValidationManager.validatePassword(password.getjTextField().getText())
                );
                password.addToPanel(panel);
                inputFields.put("password", password);

                CustomInputField confirmPassword = new CustomInputField(
                        "Confirm Password", this::updateSubmitButtonState, false, true);
                confirmPassword.setValidationFunction(() -> FieldsValidationManager.validateConfirmPassword(
                        password.getjTextField().getText(), confirmPassword.getjTextField().getText()
                ));
                confirmPassword.addToPanel(panel);
                inputFields.put("confirmPassword", confirmPassword);

                CustomInputField firstname = new CustomInputField(
                        "First Name", this::updateSubmitButtonState, false);
                firstname.addToPanel(panel);
                inputFields.put("firstname", firstname);

                CustomInputField surname = new CustomInputField(
                        "Last Name", this::updateSubmitButtonState, false);
                surname.addToPanel(panel);
                inputFields.put("surname", surname);
            }

            @Override
            protected void onSubmit() {
                String email = inputFields.get("email").getjTextField().getText();
                char[] password = inputFields.get("password").getjPasswordField().getPassword();
                String firstname = inputFields.get("firstname").getjTextField().getText();
                String surname = inputFields.get("surname").getjTextField().getText();

                signupController.signup(email, password, firstname, surname);
            }

            @Override
            protected void onCancel() {
                purgeTextFields();
                signupController.getNavigation().navigate(Navigation.LOGIN);
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
