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
import uk.ac.sheffield.com2008.view.components.Button;
import uk.ac.sheffield.com2008.view.components.Panel;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.components.InputForm;
import uk.ac.sheffield.com2008.view.components.UpdateAddressTemplate;
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
    private UpdateAddressTemplate updateAddressTemplate;
    private User user;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();

    public ManageProfileView(ManageProfileController controller) {
        this.controller = controller;
        this.content = new Panel();

        initialiseUI();
        add(content);
    }

    private void initialiseUI() {
        setLayout(new GridBagLayout());
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel subNav = new Panel(new BorderLayout());
        subNav.setLayout(new BoxLayout(subNav, BoxLayout.X_AXIS));

        JButton modifyCard = new Button("Update Banking Card");
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

                updateAddressTemplate = new UpdateAddressTemplate(panel, this::updateSubmitButtonState);
                inputFields.putAll(updateAddressTemplate.getInputFields());
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

        user.setEmail(inputFields.get("email").getjTextField().getText());
        pd.changeForename(inputFields.get("firstname").getjTextField().getText());
        pd.changeSurname(inputFields.get("lastname").getjTextField().getText());
        updateAddressTemplate.saveAddress(user);

        controller.updateUserDetails(user);
    }

    public void updateErrorMessage(String message) {
        inputForm.updateErrorMessage(message);
    }
}
