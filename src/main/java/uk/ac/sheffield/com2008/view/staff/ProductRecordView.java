package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.ProductRecordController;

import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ProductRecordView extends View {

    private final ProductRecordController productRecordController;

    public ProductRecordView(ProductRecordController productRecordController) {
        this.productRecordController = productRecordController;
        InitializeUI();
    }

    public void InitializeUI() {

        setLayout(new BorderLayout());
        int padding = 40;

        // top panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //Create a label for products in stock
        JLabel viewLabel = new JLabel("Product Record");
        row1.add(viewLabel);
        topPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel filterLabel = new JLabel("Filter by: ");
        String[] categories = {"All", "Locomotive", "Carriage", "Wagon", "Starter Oval TrackPack", "Extension TrackPack"};
        JComboBox<String> filterComboBox = new JComboBox<>(categories);
        JButton addRecordButton = new JButton("Create New Record");
        addRecordButton.addActionListener(e -> productRecordController.getNavigation().navigate(Navigation.PRODUCTFORM));

        // Set tooltip for the combo box
        filterComboBox.setToolTipText("Select a category to filter the products");
        row2.add(filterLabel);
        row2.add(filterComboBox);
        row2.add(addRecordButton);
        row2.setBorder(BorderFactory.createEmptyBorder(0, padding, 0, 0));
        topPanel.add(row2);

        add(topPanel, BorderLayout.NORTH);

        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] columnNames = {"Product Code", "Product Name", "Category", "Quantity", "Action"};

        // Create a DefaultTableModel with column names and no data initially
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Get products from the DAO
        ArrayList<Product> products = (ArrayList<Product>) ProductDAO.getAllProducts();


        // Add each product to the tableModel
        for (Product product : products) {
            // Customize the category based on the productCode
            String customCategory = determineCustomCategory(product.getProductCode());
            Object[] rowData = {product.getProductCode(), product.getName(), customCategory, product.getStock(), "Edit"};
            tableModel.addRow(rowData);
        }

        // Create the JTable using the DefaultTableModel
        JTable table = new JTable(tableModel);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane);
        this.add(productPanel);
        productPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        // Create a JPanel for the bottom section with BoxLayout in Y_AXIS
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JButton navigationButton = new JButton("Home");
        bottomPanel.add(navigationButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
        navigationButton.addActionListener(e -> productRecordController.getNavigation().navigate(Navigation.STAFF));
        add(bottomPanel, BorderLayout.SOUTH);

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
