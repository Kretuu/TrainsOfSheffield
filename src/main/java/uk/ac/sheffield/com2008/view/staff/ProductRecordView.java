package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.ProductRecordController;

import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class ProductRecordView extends View {

    ProductRecordController productRecordController;
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
        String[] filterOptions = {"All Categories", "Locomotive", "Carriage", "Wagon", "Controller" };
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        JButton addRecordButton = new JButton("Create New Record");

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
        String[] columnNames = {"Product Code", "Product Name", "Category", "Price", "Quantity", "Action"};

        // Create a DefaultTableModel with column names and no data initially
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Get products from the DAO
        ArrayList<Product> products = (ArrayList<Product>) ProductDAO.getAllProducts();

        // Add each product to the tableModel
        for (Product product : products) {
            Object[] rowData = {product.getProductCode(), product.getName(), "Category", product.getPrice(),
                    product.getStock(), "Edit"};
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
    }

}
