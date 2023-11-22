package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.controller.customer.BrowseItemsController;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.modals.AddProductToCartModal;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BrowseItemsView extends CustomerView {
    BrowseItemsController browseItemsController;
    public BrowseItemsView(BrowseItemsController loginController){
        browseItemsController = loginController;
        InitializeUI();
    }

    public void onRefresh(){
        removeAll();
        InitializeUI();
        revalidate();
        repaint();
    }

    public void InitializeUI(){
        setLayout(new BorderLayout());

        // Create a JPanel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("welcome");
        topPanel.add(welcomeLabel, BorderLayout.WEST);


        JPanel topButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton staffAreaButton = new JButton("Staff Area");
        JButton basketButton = new JButton("Basket");
        topButtonsPanel.add(basketButton);
        topButtonsPanel.add(staffAreaButton);
        // Set up the Staff Area button action
        staffAreaButton.addActionListener(e -> browseItemsController.getNavigation().navigate(Navigation.STAFF));
        // Set up Basket button action
        basketButton.addActionListener(e -> browseItemsController.getNavigation().navigate(Navigation.BASKET));

        topPanel.add(topButtonsPanel, BorderLayout.EAST);

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
            JLabel productLabel = new JLabel(product.printName());
            productLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Inner padding
            rowPanel.add(productLabel, BorderLayout.CENTER); // Add label to the center of the panel

            // Create a button for "Add To Cart"
            JButton addToCartButton = new JButton("Add To Cart");
            addToCartButton.addActionListener(e -> {
                System.out.println("clicked ADD for product: " + product);
                // Create an instance of the AddProductToCartModal class
                AddProductToCartModal modal = new AddProductToCartModal(
                        browseItemsController,
                        (JFrame) SwingUtilities.getWindowAncestor(productPanel),
                        product);

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
    }
}
