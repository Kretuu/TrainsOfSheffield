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
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.components.InputForm;
import uk.ac.sheffield.com2008.view.modals.UpdateCreditCardModal;
import uk.ac.sheffield.com2008.view.modals.VerifyPasswordModal;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ManageProfileView extends UserView {
    private final ManageProfileController controller;
    private final JPanel content;
    private InputForm inputForm;
    private User user;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();

    public ManageProfileView(ManageProfileController controller) {
        this.controller = controller;
        this.content = new JPanel();

        initialiseUI();
        add(content);
    }

    private void initialiseUI() {
        setLayout(new GridBagLayout());
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel subNav = new JPanel(new BorderLayout());
        subNav.setLayout(new BoxLayout(subNav, BoxLayout.X_AXIS));

        JButton modifyCard = new JButton("Update Banking Card");
        modifyCard.addActionListener(e -> startChangeBankDetailsProcess());

        subNav.add(modifyCard);
        content.add(subNav);
        content.add(new JSeparator());

        inputForm = createInputForm();
        content.add(inputForm);
    }

    private InputForm createInputForm() {
        return new InputForm(this, "Update", "Reset") {
            @Override
            protected void createTextFields(JPanel panel) {
                CustomInputField email = new CustomInputField(
                        "Email", this::updateSubmitButtonState, false);
                email.setValidationFunction(
                        () -> FieldsValidationManager.validateEmail(email.getjTextField().getText())
                );
                email.addToPanel(panel);
                inputFields.put("email", email);

                CustomInputField firstname = new CustomInputField(
                        "First Name", this::updateSubmitButtonState, false);
                firstname.addToPanel(panel);
                inputFields.put("firstname", firstname);

                CustomInputField lastname = new CustomInputField(
                        "Last Name", this::updateSubmitButtonState, false);
                lastname.addToPanel(panel);
                inputFields.put("lastname", lastname);

                panel.add(new JSeparator());

                CustomInputField street = new CustomInputField(
                        "Street", this::updateSubmitButtonState, false);
                street.addToPanel(panel);
                inputFields.put("street", street);

                CustomInputField houseNo = new CustomInputField(
                        "House number", this::updateSubmitButtonState, false);
                houseNo.setValidationFunction(
                        () -> FieldsValidationManager.validateHouseNo(houseNo.getjTextField().getText())
                );
                houseNo.addToPanel(panel);
                inputFields.put("houseNo", houseNo);

                CustomInputField flat = new CustomInputField(
                        "Flat", this::updateSubmitButtonState, true);
                flat.addToPanel(panel);
                inputFields.put("flat", flat);

                CustomInputField town = new CustomInputField(
                        "Town", this::updateSubmitButtonState, false
                );
                town.setValidationFunction(() -> FieldsValidationManager.validateTown(town.getjTextField().getText()));
                town.addToPanel(panel);
                inputFields.put("town", town);

                CustomInputField postcode = new CustomInputField(
                        "Postcode", this::updateSubmitButtonState, false);
                postcode.setValidationFunction(
                        () -> FieldsValidationManager.validatePostcode(postcode.getjTextField().getText())
                );
                postcode.addToPanel(panel);
                inputFields.put("postcode", postcode);
            }

            @Override
            protected void onSubmit() {
                updateUserDetails();
            }

            @Override
            protected void onCancel() {
                populateTextFields();
            }

            @Override
            protected boolean submitEnabled() {
                return inputFields.values().stream().allMatch(CustomInputField::isValid);
            }
        };
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

    private void startChangeBankDetailsProcess() {
        new VerifyPasswordModal(controller.getNavigation().getFrame(), this) {

            @Override
            public String onConfirm(char[] password) throws SQLException, BankDetailsEncryptionException {
                if(UserDAO.verifyPassword(user.getEmail(), password) != null) {
                    BankingCard bankingCard = controller.getBankingCard(password);
                    CompletableFuture.runAsync(() -> openChangeBankDetailsModal(bankingCard, password));
                    return null;
                }
                return "Incorrect password";
            }
        }.setVisible(true);
    }

    private void openChangeBankDetailsModal(BankingCard bankingCard, char[] password) {
        new UpdateCreditCardModal(
                controller.getNavigation().getFrame(), bankingCard, this) {
            @Override
            public void onSave(
                    String cardNumber, Date expiryDate, String securityCode, String holderName
            ) throws SQLException, BankDetailsEncryptionException {
                UserManager.updateUserBankDetails(
                        user, new BankingCard(holderName, cardNumber, expiryDate, securityCode), password
                );
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
        inputForm.updateErrorMessage(message);
    }
}
