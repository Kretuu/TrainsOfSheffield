package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.View;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class ManageStockView extends View {

    StaffController staffController;

    public ManageStockView(StaffController staffController ) {
        super();
        this.staffController = staffController;
        initializeUI();
    }

    public void initializeUI() {
        setLayout(new BorderLayout());

        // Create a JPanel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a Jpanel for the filter section
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Using FlowLayout to align components horizontally
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create a JPanel for the bottom section with BoxLayout in Y_AXIS
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));


        // Create a label for products in stock
        JLabel welcomeLabel = new JLabel("Products in Stock");
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        // Create a Logout button
        JButton logoutButton = new JButton("Logout");
        topPanel.add(logoutButton, BorderLayout.EAST);

        // Create a Product Record button
        JButton productRecordButton = new JButton("Product Record");
        bottomPanel.add(productRecordButton);

        // Create a Manage Order button
        JButton manageOrderButton = new JButton("Manage Order");
        bottomPanel.add(manageOrderButton);

        // Add indentation between buttons using EmptyBorder
        int buttonIndentation = 10;
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, buttonIndentation, 0, 0));

        JLabel filterLabel = new JLabel("Filter by: ");
        String[] filterOptions = {"Locomotive", "Carriage", "Wagon", "Controller", "Starter Oval TrackPack", "Extension TrackPack"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        // Set tooltip for the combo box
        filterComboBox.setToolTipText("Select a category to filter the products");
        // Add the filter panel to the frame
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        // Add the top panel to the top of the frame
        add(topPanel, BorderLayout.NORTH);
        // Add the bottom panel to the bottom of the frame
        add(bottomPanel, BorderLayout.SOUTH);


        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Product Code","Products in Stock", "Category", "Quantity", "Action"};

        // Create a DefaultTableModel with column names and no data initially
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Get products from the DAO
        ArrayList<Product> products = ProductDAO.getAllProducts();

        // Add each product to the tableModel
        for (Product product : products) {
            Object[] rowData = {product.getProductCode(),product.getName(),"Category", product.getStock(), "Edit"};
            tableModel.addRow(rowData);
        }

        // Create the JTable using the DefaultTableModel
        JTable table = new JTable(tableModel);

        // Center the content in "Quantity" and "Action" column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane);
        this.add(productPanel);

        productRecordButton.addActionListener(e -> staffController.getNavigation().navigate(Navigation.PRODUCTRECORD));


        // Set up the Product Record button action



        // Table for products in stock
        /*
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; */

        /*String[] columnNames = {"Products in Stock", "Category", "Quantity", "Action"};
        Object[][] data = {
                {"Flying Scotsman", "Locomotive", 65, "Edit"},
                {"Product 2", "Carriage", 100, "Edit"},
                {"Product 3", "Wagon", 86, "Edit"},
                {"Product 4", "Controller", 15, "Edit"},
                // This is for example only
        };
        */

        //JTable table = new JTable(data, columnNames);
        //JScrollPane scrollPane = new JScrollPane(table);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //add(scrollPane, BorderLayout.CENTER);

    }
}
