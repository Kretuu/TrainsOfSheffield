package uk.ac.sheffield.com2008.view;

import uk.ac.sheffield.com2008.controller.BrowseItemsController;
import uk.ac.sheffield.com2008.controller.StaffController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.view.modals.AddProductToCartModal;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class BrowseItemsView extends View {
    BrowseItemsController browseItemsController;
    public BrowseItemsView(BrowseItemsController loginController){
        browseItemsController = loginController;
        InitializeUI();
    }

    public void InitializeUI(){
        setLayout(new BorderLayout());

        // Create a JPanel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome");
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton staffAreaButton = new JButton("Staff Area");
        topPanel.add(staffAreaButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //create product rows
        for (Product product : browseItemsController.getAllProducts()) {
            // Create a panel to hold each row (label and button)
            JPanel rowPanel = new JPanel(new BorderLayout());

            // Create matte border between rows
            Border matteBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK); // Border between rows
            // Create empty border for inner padding
            Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5); // Inner padding for the entire row
            Border compoundBorder = BorderFactory.createCompoundBorder(matteBorder, emptyBorder);
            rowPanel.setBorder(compoundBorder);

            // Create a label for the product
            JLabel productLabel = new JLabel(product.toString());
            productLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Inner padding
            rowPanel.add(productLabel, BorderLayout.CENTER); // Add label to the center of the panel

            // Create a button for "Add To Cart"
            JButton addToCartButton = new JButton("Add To Cart");
            addToCartButton.addActionListener(e -> {
                System.out.println("clicked ADD for product: " + product.toString());
                // Create an instance of the AddProductToCartModal class
                AddProductToCartModal modal = new AddProductToCartModal((JFrame) SwingUtilities.getWindowAncestor(productPanel), product);

                modal.setVisible(true); // Show the modal dialog
            });
            rowPanel.add(addToCartButton, BorderLayout.LINE_END); // Add button to the right of the panel

            // Set the maximum height of the row panel to the height of the label + padding
            rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, productLabel.getPreferredSize().height + 10)); // Adjust 10 for desired padding

            // Add rowPanel to the productPanel
            productPanel.add(rowPanel);
        }

        // Create a JScrollPane and set the productPanel as its view
        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        // Set up the Staff Area button action
        staffAreaButton.addActionListener(e -> {
            new StaffController();
            // Add your action when the Staff Area button is clicked
            // For example, open a new view or perform some action
        });
    }
}
