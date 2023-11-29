package uk.ac.sheffield.com2008.view.components;

import uk.ac.sheffield.com2008.model.entities.Address;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.util.FieldsValidationManager;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class UpdateAddressTemplate {
    private final JPanel panel;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();

    public UpdateAddressTemplate(JPanel panel, Runnable onTextFieldUpdate) {
        this.panel = panel;
        createTextFields(onTextFieldUpdate);
    }

    private void createTextFields(Runnable onTextFieldUpdate) {
        CustomInputField street = new CustomInputField(
                "Street", onTextFieldUpdate, false);
        street.addToPanel(panel);
        inputFields.put("street", street);

        CustomInputField houseNo = new CustomInputField(
                "House number", onTextFieldUpdate, false);
        houseNo.setValidationFunction(
                () -> FieldsValidationManager.validateHouseNo(houseNo.getjTextField().getText())
        );
        houseNo.addToPanel(panel);
        inputFields.put("houseNo", houseNo);

        CustomInputField flat = new CustomInputField(
                "Flat", onTextFieldUpdate, true);
        flat.addToPanel(panel);
        inputFields.put("flat", flat);

        CustomInputField town = new CustomInputField(
                "Town", onTextFieldUpdate, false
        );
        town.setValidationFunction(() -> FieldsValidationManager.validateTown(town.getjTextField().getText()));
        town.addToPanel(panel);
        inputFields.put("town", town);

        CustomInputField postcode = new CustomInputField(
                "Postcode", onTextFieldUpdate, false);
        postcode.setValidationFunction(
                () -> FieldsValidationManager.validatePostcode(postcode.getjTextField().getText())
        );
        postcode.addToPanel(panel);
        inputFields.put("postcode", postcode);
    }

    public Map<String, CustomInputField> getInputFields() {
        return inputFields;
    }

    public void saveAddress(User user) {
        Address ad = user.getAddress() == null ? new Address() : user.getAddress();
        user.setAddress(ad);

        ad.setStreet(inputFields.get("street").getjTextField().getText());
        ad.setHouseNumber(Integer.parseInt(inputFields.get("houseNo").getjTextField().getText()));
        ad.setFlatIdentifier(inputFields.get("flat").getjTextField().getText());
        ad.setCity(inputFields.get("town").getjTextField().getText());
        ad.setPostCode(inputFields.get("postcode").getjTextField().getText());
    }
}
