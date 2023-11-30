package uk.ac.sheffield.com2008.view.manager;

import org.w3c.dom.css.Rect;
import uk.ac.sheffield.com2008.controller.manager.ManageUserRolesController;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.view.components.CustomInputField;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.ManagerUserTableMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ManageUserRolesView extends ManagerView {
    private final ManageUserRolesController controller;
    private final JPanel content;
    private final JButton promoteUserButton;
    private CustomTable<User> customTable;
    private CustomInputField userEmail;
    private JPanel panel;
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

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel appointStaffTitle = new JLabel("Appoint staff");
        appointStaffTitle.setFont(new Font(null, Font.BOLD, 16));
        appointStaffTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        appointStaffTitle.setAlignmentX(CENTER_ALIGNMENT);

        JPanel appointStaffContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel appointStaffForm = new JPanel(new GridLayout(1, 2, 30, 0));

        userEmail = new CustomInputField("Email", this::updateButtonState, true);
        userEmail.setValidationFunction(() -> verifyCustomerEmail(userEmail.getjTextField().getText()));
        userEmail.addToPanel(appointStaffForm);

        JPanel buttonPanel = new JPanel();
        promoteUserButton.addActionListener(e -> controller.appointStaffRole(userEmail.getjTextField().getText()));
        buttonPanel.add(promoteUserButton);
        appointStaffForm.add(buttonPanel);
        appointStaffContainer.add(appointStaffForm);

        headerPanel.add(appointStaffTitle);
        headerPanel.add(appointStaffContainer);


        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel tableTitle = new JLabel("Staff users");
        tableTitle.setFont(new Font(null, Font.BOLD, 16));
        tableTitle.setBorder(new EmptyBorder(15, 0,  15, 0));
        tableTitle.setAlignmentX(CENTER_ALIGNMENT);


        centerPanel.add(new JSeparator());
        centerPanel.add(tableTitle);
        centerPanel.add(customTable);

        content.add(headerPanel, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);

    }

    public void populateTable(List<User> staffList) {
        customTable.updateDimension(controller, 500);
        customTable.populateTable(staffList, mapper);
    }

    private void updateButtonState() {
        promoteUserButton.setEnabled(!userEmail.getjTextField().getText().isEmpty() && userEmail.isValid());
    }

    private String verifyCustomerEmail(String email) {
        if(controller.isCustomerEmailValid(email)) return null;
        return "There is no customer who has given email";
    }
}
