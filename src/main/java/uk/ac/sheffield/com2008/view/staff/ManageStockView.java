package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.awt.*;

public class ManageStockView extends View {

    private final StaffController staffController;

    public ManageStockView(StaffController staffController) {
        this.staffController = staffController;
        InitializeUI();
    }

    public void InitializeUI() {
        setLayout(new BorderLayout());

        // Create a JPanel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Create panel for the filter section
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Using FlowLayout to align components horizontally
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        //Create a label for products in stock
        JLabel welcomeLabel = new JLabel("Products in Stock");
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton staffAreaButton = new JButton("Logout");
        topPanel.add(staffAreaButton, BorderLayout.EAST);

        JLabel filterLabel = new JLabel("Filter by: ");
        String[] filterOptions = {"Locomotive", "Carriage", "Wagon", "Controller", "Starter Oval TrackPack", "Extension TrackPack"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        // Set tooltip for the combo box
        filterComboBox.setToolTipText("Select a category to filter the products");
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);


        // Table for products in stock
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        String[] columnNames = {"Products in Stock", "Category", "Quantity", "Action"};
        Object[][] data = {
                {"Flying Scotsman", "Locomotive", 65, "Edit"},
                {"Product 2", "Carriage", 100, "Edit"},
                {"Product 3", "Wagon", 86, "Edit"},
                {"Product 4", "Controller", 15, "Edit"},
                // This is for example only
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

    }
}
