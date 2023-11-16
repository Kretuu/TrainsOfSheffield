package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.model.entities.Product;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductToCartModal extends JDialog {

    private JLabel productName;
    private JSpinner quantitySpinner;
    private JLabel totalPriceLabel;

    public AddProductToCartModal(JFrame parentFrame, Product product) {
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
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create "Quantity:" label
        JLabel quantityLabel = new JLabel("Quantity:");
        bottomPanel.add(quantityLabel);

        // Create spinner for quantity selection
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 99, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        bottomPanel.add(quantitySpinner);

        // Create "Add To Cart" button
        JButton addToCartButton = new JButton("Add To Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Your custom function here
                // For example, print the selected quantity
                int selectedQuantity = (int) quantitySpinner.getValue();
                System.out.println("Selected Quantity: " + selectedQuantity);

                // Close the modal dialog
                dispose();
            }
        });

        bottomPanel.add(addToCartButton);

        panel.add(bottomPanel, BorderLayout.CENTER);

        JPanel totalPriceLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel totalPriceLabel = new JLabel("Total Price:");
        totalPriceLabelPanel.add(totalPriceLabel);
        panel.add(totalPriceLabelPanel, BorderLayout.SOUTH);

        // Set panel to the content pane of the dialog
        setContentPane(panel);

        setSize(300, 200); // Set the size of the dialog
        setLocationRelativeTo(parentFrame); // Center the dialog relative to the parent frame
    }

    // You can create getter and setter methods for customLabel and quantitySpinner
    // to modify their content or properties from outside this class if needed
}
