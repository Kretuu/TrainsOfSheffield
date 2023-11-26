package uk.ac.sheffield.com2008.view.user;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.customer.ManageProfileController;
import uk.ac.sheffield.com2008.exceptions.BankDetailsEncryptionException;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.domain.managers.UserManager;
import uk.ac.sheffield.com2008.model.entities.Address;
import uk.ac.sheffield.com2008.model.entities.BankingCard;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.util.FieldsValidationManager;
import uk.ac.sheffield.com2008.util.listeners.AuthorisationActionListener;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.modals.UpdateCreditCardModal;
import uk.ac.sheffield.com2008.view.modals.VerifyPasswordModal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ManageProfileView extends UserView {
    private final ManageProfileController controller;
    private final JButton submitButton;
    private final JPanel content;
    private final JLabel errorMessage;
    private User user;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();

    public ManageProfileView(ManageProfileController controller) {
        this.controller = controller;
        this.content = new JPanel();
        this.submitButton = new JButton("Update");
        this.errorMessage = new JLabel(" ");

        initialiseUI();
        add(content);
    }

    private void initialiseUI() {
        setLayout(new GridBagLayout());

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setEnabled(false);
        submitButton.addActionListener(new AuthorisationActionListener(this) {
            @Override
            public void action(ActionEvent e) { updateUserDetails(); }
        });


        JPanel subNav = new JPanel(new BorderLayout());
        subNav.setLayout(new BoxLayout(subNav, BoxLayout.X_AXIS));

        JButton modifyCard = new JButton("Update Banking Card");
        modifyCard.addActionListener(e -> changeBankDetails());

        subNav.add(modifyCard);
        content.add(subNav);
        content.add(new JSeparator());

        errorMessage.setForeground(Color.RED);
        content.add(errorMessage);

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

        CustomInputField flat = new CustomInputField("Flat", this::updateButtonState, true);
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

    public void populateTextFields() {
        this.user = AppSessionCache.getInstance().getUserLoggedIn();
        PersonalDetails personalDetails = user.getPersonalDetails();
        Address address = user.getAddress();


        inputFields.get("email").getjTextField().setText(user.getEmail());
        inputFields.get("firstname").getjTextField().setText(personalDetails.getForename());
        inputFields.get("lastname").getjTextField().setText(personalDetails.getSurname());
        if(address == null) return;

        inputFields.get("street").getjTextField().setText(address.getStreet());
        inputFields.get("houseNo").getjTextField().setText(String.valueOf(address.getHouseNumber()));
        inputFields.get("flat").getjTextField().setText(address.getFlatIdentifier());
        inputFields.get("town").getjTextField().setText(address.getCity());
        inputFields.get("postcode").getjTextField().setText(address.getPostCode());
    }

    private void changeBankDetails() {
        new VerifyPasswordModal(controller.getNavigation().getFrame()) {

            @Override
            public String onConfirm(char[] password) throws SQLException, BankDetailsEncryptionException {
                if(UserDAO.verifyPassword(user.getEmail(), password) != null) {
                    BankingCard bankingCard = controller.getBankingCard(password);
                    CompletableFuture.runAsync(() -> new UpdateCreditCardModal(
                            controller.getNavigation().getFrame(), bankingCard) {
                        @Override
                        public boolean onSave(
                                String cardNumber, Date expiryDate, String securityCode, String holderName
                        ) throws SQLException, BankDetailsEncryptionException {
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
        }.setVisible(true);
    }

    private void updateUserDetails() {
        PersonalDetails pd = user.getPersonalDetails();
        Address ad = user.getAddress() == null ? new Address() : user.getAddress();
        user.setAddress(ad);

        user.setEmail(inputFields.get("email").getjTextField().getText());
        pd.changeForename(inputFields.get("firstname").getjTextField().getText());
        pd.changeSurname(inputFields.get("lastname").getjTextField().getText());

        ad.setStreet(inputFields.get("street").getjTextField().getText());
        ad.setHouseNumber(Integer.parseInt(inputFields.get("houseNo").getjTextField().getText()));
        ad.setFlatIdentifier(inputFields.get("flat").getjTextField().getText());
        ad.setCity(inputFields.get("town").getjTextField().getText());
        ad.setPostCode(inputFields.get("postcode").getjTextField().getText());

        controller.updateUserDetails(user);
    }

    public void updateErrorMessage(String message) {
        if(message == null) {
            errorMessage.setText(" ");
            return;
        }
        errorMessage.setText("Error: " + message);
    }

    private void updateButtonState() {
        submitButton.setEnabled(inputFields.values().stream().allMatch(CustomInputField::isValid));
    }
}
