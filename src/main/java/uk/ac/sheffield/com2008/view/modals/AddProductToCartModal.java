package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.customer.BrowseItemsController;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.util.math.Rounding;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductToCartModal extends JDialog {

    private JLabel productName;
    private JSpinner quantitySpinner;
    private JLabel totalPriceLabel;
    private JLabel noStockLabel;
    private int selectedQuantity = 1;

    public AddProductToCartModal(BrowseItemsController browseItemsController, JFrame parentFrame, Product product) {
        super(parentFrame, "", true); // Set modal dialog with no title and bound to parent frame

        // Create a panel to hold the content
        JPanel panel = new JPanel(new BorderLayout());
        // Give the panel some padding
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());

        // Create "Add To Cart" label at the top
        JLabel titleLabel = new JLabel("Add To Cart:");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Create a label that can be customized
        productName = new JLabel(product.printName());
        topPanel.add(productName, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        // Create a panel for quantity and add to cart button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Create "Quantity:" label
        JLabel quantityLabel = new JLabel("Quantity:");
        buttonPanel.add(quantityLabel);

        // Create spinner for quantity selection
        int quantityInBasket = 0;
        Order userBasket = AppSessionCache.getInstance().getUserLoggedIn().getBasket();
        userBasket.PrintFullOrder();
        if(userBasket.hasProduct(product)){
            System.out.println("User has this item in basket");
            quantityInBasket = userBasket.getProductQuantity(product);
        }
        System.out.println(quantityInBasket);
        int stockAvailableToAdd = product.getStock() - quantityInBasket;

        SpinnerModel spinnerModel = new SpinnerNumberModel(
                Math.min(1, stockAvailableToAdd),
                Math.min(1, stockAvailableToAdd),
                stockAvailableToAdd,
                1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Method called when the spinner value changes
                selectedQuantity = (int) quantitySpinner.getValue();
                float newPrice = product.getPrice() * selectedQuantity;
                updateTotalPrice(newPrice);
            }
        });
        buttonPanel.add(quantitySpinner);

        // Create "Add To Cart" button
        JButton addToCartButton = new JButton("Add To Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseItemsController.addProductToBasket(product, selectedQuantity);

                // Close the modal dialog
                dispose();
            }
        });
        buttonPanel.add(addToCartButton);

        JPanel totalPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPriceLabel = new JLabel("Total Price: " + product.getPrice());
        totalPricePanel.add(totalPriceLabel);
        panel.add(totalPricePanel, BorderLayout.SOUTH);
        bottomPanel.add(totalPricePanel);
        bottomPanel.add(buttonPanel);

        if(stockAvailableToAdd == 0){
            noStockLabel = new JLabel("No Stock Left");
            noStockLabel.setForeground(Color.RED);
            noStockLabel.setFont(noStockLabel.getFont().deriveFont(Font.BOLD));
            bottomPanel.add(noStockLabel);
            JPanel noStockPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            noStockPanel.add(noStockLabel);
            addToCartButton.setEnabled(false);
            bottomPanel.add(noStockPanel);
        }

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Set panel to the content pane of the dialog
        setContentPane(panel);

        // Set the minimum size to ensure it doesn't become too small
        setMinimumSize(new Dimension(300, 200));

        // Set the maximum size to ensure it doesn't expand too much vertically
        // Adjust the maximum height according to your requirements
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        setResizable(false);
        setLocationRelativeTo(parentFrame); // Center the dialog relative to the parent frame
    }

    private void updateTotalPrice(float newPrice){
        totalPriceLabel.setText("Total Price: " + Rounding.roundToDecimalPlaces(newPrice, 2));
    }
}
