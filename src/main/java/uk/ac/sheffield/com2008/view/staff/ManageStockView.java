package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class ManageStockView extends StaffView {

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
        String[] categories = {"All", "Locomotive", "Carriage", "Wagon", "Starter Oval TrackPack", "Extension TrackPack"};
        JComboBox<String> filterComboBox = new JComboBox<>(categories);
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
        List<Product> products = ProductDAO.getAllProducts();

        // Add each product to the tableModel
        for (Product product : products) {
            // Customize the category based on the productCode
            String customCategory = determineCustomCategory(product.getProductCode());
            Object[] rowData = {product.getProductCode(),product.getName(),customCategory, product.getStock(), "Edit"};
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

        // Set up the Product Record button action
        productRecordButton.addActionListener(e -> staffController.getNavigation().navigate(Navigation.PRODUCTRECORD));

        // Add an ActionListener to the filter combo box
        filterComboBox.addActionListener(e -> {
            String selectedCategory = (String) filterComboBox.getSelectedItem();
            // Get the initial letter based on the selected category
            String initialLetter = getInitialLetter(selectedCategory);
            // Call the filter method based on the selected starting letter
            filterTableByCategory(tableModel, initialLetter);
        });
    }

    // Method to get the initial letter based on the selected category
    private String getInitialLetter(String selectedCategory) {
        if ("Locomotive".equals(selectedCategory)) {
            return "L";
        } else if ("Carriage".equals(selectedCategory)) {
            return "C";
        } else if ("Wagon".equals(selectedCategory)) {
            return "W";
        }else if ("Starter Oval TrackPack".equals(selectedCategory)) {
            return "S";
        }else if ("Extension TrackPack".equals(selectedCategory)) {
            return "E";
        } else {
            return "";
        }
    }

    private void filterTableByCategory(DefaultTableModel tableModel, String initialLetter) {
        // Clear the existing rows in the table
        tableModel.setRowCount(0);

        // Get products based on the selected category from the DAO
        List<Product> filteredProducts;
        if ("All".equals(initialLetter)) {
            // If "All" is selected, get all products
            filteredProducts = ProductDAO.getAllProducts();
        } else {
            // Otherwise, get products for the selected category
            filteredProducts = ProductDAO.getProductsByCategory(initialLetter);
        }

        // Add each filtered product to the tableModel
        for (Product product : filteredProducts) {
            // Customize the category based on the productCode
            String customCategory = determineCustomCategory(product.getProductCode());

            Object[] rowData = {product.getProductCode(), product.getName(), customCategory, product.getStock(), "Edit"};
            tableModel.addRow(rowData);
            //System.out.println("Number of Rows in Table Model: " + tableModel.getRowCount());
        }

    }
    private String determineCustomCategory(String productCode) {
        // Check if the productCode starts with the letter 'L'
        if (productCode.startsWith("L")) {
            return "Locomotive";
        } else if (productCode.startsWith("C")) {
            return "Carriage";
        } else if (productCode.startsWith("W")) {
            return "Wagon";
        } else if (productCode.startsWith("S")) {
            return "Starter Oval TrackPack";
        } else if (productCode.startsWith("E")) {
            return "Extension TrackPack";
        } else {
                // Add more custom category conditions as needed
            return "Other Category";
        }

    }
}
