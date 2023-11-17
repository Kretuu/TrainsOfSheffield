package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.ProductRecordController;
import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class ProductRecordView extends View{

    ProductRecordController controller;

    public ProductRecordView(ProductRecordController productRecordController) {
        controller = productRecordController;
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


        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Products in Stock", "Category", "Quantity", "Action"};

        // Create a DefaultTableModel with column names and no data initially
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Get products from the DAO
        ArrayList<Product> products = ProductDAO.getAllProducts();

        // Add each product to the tableModel
        for (Product product : products) {
            Object[] rowData = {product.getName(), product.getStock(), "Edit"};
            tableModel.addRow(rowData);
        }

        // Create the JTable using the DefaultTableModel
        JTable table = new JTable(tableModel);

        // Optionally, you can add the JTable to a JScrollPane if needed
        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane);
        this.add(productPanel);
    }

}
