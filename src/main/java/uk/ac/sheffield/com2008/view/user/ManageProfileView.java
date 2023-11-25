package uk.ac.sheffield.com2008.view.user;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.customer.ManageProfileController;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.domain.managers.UserManager;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.util.FieldsValidationManager;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.modals.UpdateCreditCardModal;
import uk.ac.sheffield.com2008.view.modals.VerifyPasswordModal;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ManageProfileView extends UserView {
    private final ManageProfileController controller;
    private final JButton submitButton;
    private final JPanel content;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();

    public ManageProfileView(ManageProfileController controller) {
        this.controller = controller;
        this.content = new JPanel();
        this.submitButton = new JButton("Update");

        initialiseUI();
        add(content);
    }

    private void initialiseUI() {
        setLayout(new GridBagLayout());

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setEnabled(false);


        JPanel subNav = new JPanel();
        subNav.setLayout(new BoxLayout(subNav, BoxLayout.X_AXIS));

        JButton modifyCard = new JButton("Update Banking Card");

        modifyCard.addActionListener(e -> new VerifyPasswordModal(controller.getNavigation().getFrame()) {

            @Override
            public String onConfirm(char[] password) {
                User user = AppSessionCache.getInstance().getUserLoggedIn();
                if(UserDAO.verifyPassword(user.getEmail(), password) != null) {
                    CompletableFuture.runAsync(() -> new UpdateCreditCardModal(controller.getNavigation().getFrame(), controller.getBankingCard(password)) {
                        @Override
                        public boolean onSave(String cardNumber, Date expiryDate, String securityCode, String holderName) {
                            UserManager.updateUserBankDetails(user, new BankingCard(
                                    holderName, cardNumber, expiryDate, securityCode
                            ), password);
                            return true;
                        }
                    }.setVisible(true));
                    return null;
                }
                return "Incorrect password";
            }
        }.setVisible(true));

//        modifyCard.addActionListener(e -> {
//
//        });

        subNav.add(modifyCard);
        content.add(subNav);

        JButton cancelButton = new JButton("Reset");
        createTextFields();

        JPanel buttonsPanel = new JPanel(new GridLayout(1,2,10,0));
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(submitButton);
        content.add(buttonsPanel);
    }

    private void createTextFields() {
        CustomInputField email = new CustomInputField("Email", this::updateButtonState, false);
        email.setValidationFunction(() -> FieldsValidationManager.validateEmail(email.getjTextField().getText()));
        email.addToPanel(content);
        inputFields.put("email", email);

        CustomInputField firstname = new CustomInputField(
                "First Name", this::updateButtonState, false
        );
        firstname.addToPanel(content);
        inputFields.put("firstname", firstname);

        CustomInputField lastname = new CustomInputField(
                "Last Name", this::updateButtonState, false
        );
        lastname.addToPanel(content);
        inputFields.put("lastname", lastname);

        content.add(new JSeparator());

        CustomInputField street = new CustomInputField("Street", this::updateButtonState, false);
        street.addToPanel(content);
        inputFields.put("street", street);

        CustomInputField houseNo = new CustomInputField("House number", this::updateButtonState, false);
        houseNo.setValidationFunction(() -> FieldsValidationManager.validateHouseNo(houseNo.getjTextField().getText()));
        houseNo.addToPanel(content);
        inputFields.put("houseNo", houseNo);

        CustomInputField flat = new CustomInputField("Flat", this::updateButtonState, false);
        flat.addToPanel(content);
        inputFields.put("flat", flat);

        CustomInputField town = new CustomInputField("Town", this::updateButtonState, false);
        town.setValidationFunction(() -> FieldsValidationManager.validateTown(town.getjTextField().getText()));
        town.addToPanel(content);
        inputFields.put("town", town);

        CustomInputField postcode = new CustomInputField("Postcode", this::updateButtonState, false);
        postcode.setValidationFunction(() -> FieldsValidationManager.validatePostcode(postcode.getjTextField().getText()));
        postcode.addToPanel(content);
        inputFields.put("postcode", postcode);
    }

    private void updateButtonState() {
        submitButton.setEnabled(inputFields.values().stream().allMatch(CustomInputField::isValid));
    }
}
