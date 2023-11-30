package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.EditFormController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.components.Button;

import javax.swing.*;
import java.awt.*;

public class EditProductRecordForm extends StaffView{
    private final EditFormController editFormController;

    public EditProductRecordForm(EditFormController editFormController) {
        super();
        this.editFormController = editFormController;
        initializeUI();
    }
    private void initializeUI() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(new JLabel("Category: "), gbc);
        String[] categories = {"All categories", "Locomotive", "Rolling Stock", "Track", "Controller",
                "Train Set", "Track Pack"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Product Code: "), gbc);
        //TODO: figure out how the text input would take the value from db
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField productCodeField = new JTextField();
        add(productCodeField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("Product Name: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField productNameField = new JTextField();
        add(productNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(new JLabel("Description: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3; // Move to the next row
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea descriptionField = new JTextArea(5, 30); // Specify the number of rows and columns
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        add(new JScrollPane(descriptionField), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(new JLabel("Gauge: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] gauges = {"All gauges", "OO Gauge (1/76th scale)", "TT Gauge (1/120th scale)",
                "N gauge (1/148th scale)"};
        JComboBox<String> gaugesComboBox = new JComboBox<>(gauges);
        add(gaugesComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(new JLabel("Price: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField priceField = new JTextField();
        add(priceField, gbc);


        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(buttonPanel(), gbc);
    }

    private JPanel buttonPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton cancelButton = new Button("Cancel");
        buttonsPanel.add(cancelButton);
        cancelButton.addActionListener(e -> editFormController.getNavigation().navigate(Navigation.PRODUCT_RECORD));

        JButton submitButton = new Button("Save");
        buttonsPanel.add(submitButton);

        return buttonsPanel;
    }
}
