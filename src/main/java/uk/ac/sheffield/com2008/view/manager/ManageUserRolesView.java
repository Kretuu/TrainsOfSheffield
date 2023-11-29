package uk.ac.sheffield.com2008.view.manager;

import uk.ac.sheffield.com2008.controller.manager.ManageUserRolesController;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.ManagerUserTableMapper;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ManageUserRolesView extends ManagerView {
    private final ManageUserRolesController controller;
    private final JPanel content;
    private final JButton promoteUserButton;
    private final CustomTable<User> customTable;
    private CustomInputField userEmail;
    private final ManagerUserTableMapper mapper;
    public ManageUserRolesView(ManageUserRolesController controller) {
        this.controller = controller;
        this.content = new JPanel();
        this.promoteUserButton = new JButton("Appoint staff");
        this.mapper = new ManagerUserTableMapper(controller);

        LinkedList<CustomColumn> columns = new LinkedList<>() {{
            add(new CustomColumn(0.2, "Email"));
            add(new CustomColumn(0.3, "Forename"));
            add(new CustomColumn(0.3, "Surname"));
            add(new CustomColumn(0.1, null));
        }};
        this.customTable = new CustomTable<>(columns);

        initialiseUI();
        add(content);
    }

    private void initialiseUI() {
        content.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(1, 2, 30, 0));

        userEmail = new CustomInputField("Email", this::updateButtonState, true);
        userEmail.setValidationFunction(() -> verifyCustomerEmail(userEmail.getjTextField().getText()));
        userEmail.addToPanel(headerPanel);

        promoteUserButton.addActionListener(e -> controller.appointStaffRole(userEmail.getjTextField().getText()));
        headerPanel.add(promoteUserButton);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(customTable);

    }

    public void populateTable(List<User> staffList) { customTable.populateTable(staffList, mapper); }

    private void updateButtonState() {
        promoteUserButton.setEnabled(!userEmail.getjTextField().getText().isEmpty() && userEmail.isValid());
    }

    private String verifyCustomerEmail(String email) {
        if(controller.isCustomerEmailValid(email)) return null;
        return "There is no customer who has given email";
    }
}
