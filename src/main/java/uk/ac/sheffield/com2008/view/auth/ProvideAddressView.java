package uk.ac.sheffield.com2008.view.auth;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.auth.ProvideAddressController;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.components.Panel;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.components.InputForm;
import uk.ac.sheffield.com2008.view.components.UpdateAddressTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ProvideAddressView extends AuthView {
    private final ProvideAddressController controller;
    private UpdateAddressTemplate updateAddressTemplate;
    private final InputForm inputForm;
    private User user;
    private final Map<String, CustomInputField> inputFields = new HashMap<>();

    public ProvideAddressView(ProvideAddressController controller) {
        super();
        this.controller = controller;
        this.inputForm = createInputForm();
        JPanel header = new Panel();
        JLabel label = new JLabel("Provide address");
        label.setFont(new Font(null, Font.BOLD, 16));
        header.add(label);

        panel.add(header);
        panel.add(inputForm);
    }

    private InputForm createInputForm() {
        return new InputForm(this, "Submit", "Cancel") {
            @Override
            protected void createTextFields(JPanel panel) {
                updateAddressTemplate = new UpdateAddressTemplate(panel, this::updateSubmitButtonState);
                inputFields.putAll(updateAddressTemplate.getInputFields());
            }

            @Override
            protected void onSubmit() {
                updateAddressTemplate.saveAddress(user);
                controller.updateUserDetails(user);
            }

            @Override
            protected void onCancel() {
                purgeTextFields();
                controller.getNavigation().navigate(Navigation.LOGIN);
            }

            @Override
            protected boolean submitEnabled() {
                return inputFields.values().stream().allMatch(CustomInputField::isValid);
            }
        };
    }

    public void updateUser() {
        this.user = AppSessionCache.getInstance().getUserLoggedIn();
        if(user == null) controller.getNavigation().navigate(Navigation.LOGIN);
        if(user.getAddress() != null) controller.getNavigation().navigate(Navigation.CUSTOMER);
    }

    public void purgeTextFields() {
        inputFields.get("street").getjTextField().setText("");
        inputFields.get("houseNo").getjTextField().setText("");
        inputFields.get("flat").getjTextField().setText("");
        inputFields.get("town").getjTextField().setText("");
        inputFields.get("postcode").getjTextField().setText("");
    }

    public void updateErrorMessage(String message) {
        inputForm.updateErrorMessage(message);
    }
}
