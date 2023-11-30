package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.view.components.Button;
import uk.ac.sheffield.com2008.view.staff.ManageStockView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class EditProductStockModal extends JDialog {

    private JLabel productName;
    private JSpinner quantitySpinner;
    private JLabel totalStockLabel;
    private int selectedQuantity;

    private ManageStockView manageStockView; // Reference to ManageStockView

    public EditProductStockModal(StaffController staffController, JFrame parentFrame, Product product, ManageStockView manageStockView){
        super(parentFrame, "", true); // Set modal dialog with no title and bound to parent frame
        this.manageStockView = manageStockView; // Assign the reference
        // Create a panel to hold the content
        JPanel panel = new JPanel(new BorderLayout());

        // Give the panel some padding
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Create "Edit Stock" label at the top
        JLabel titleLabel = new JLabel("Edit Stock");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Create a label that can be customized
        productName = new JLabel("<html><div style='width: 100%;'>" + product.printName() + "</div></html>");
        topPanel.add(productName, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);


        // Create a panel for quantity and update button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create "Quantity:" label
        JLabel quantityLabel = new JLabel("Quantity :");
        buttonPanel.add(quantityLabel);


        // Retrieve current stock quantity from the database
        int currentStockQuantity = product.getStock();

        // Quantity Spinner
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(currentStockQuantity, currentStockQuantity, Integer.MAX_VALUE, 1);
        quantitySpinner = new JSpinner(spinnerModel); // Initial value, min, max, step

        // Create an Update Button
        quantitySpinner.addChangeListener(e -> {
            // Method called when the spinner value changes
            selectedQuantity = (int) quantitySpinner.getValue();
            int newQuantity =  selectedQuantity;
            updateStock(newQuantity);
        });
        buttonPanel.add(quantitySpinner);

        JButton updateButton = new Button("Update");
        updateButton.addActionListener(e -> {
            staffController.updateProductQuantity(product, selectedQuantity);
            System.out.println("Updated quantity: " + selectedQuantity);

            // Show a message indicating a successful update
            JOptionPane.showMessageDialog(this, "Stocks has been updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Close the modal dialog
            dispose();
            //Refresh the UI
            staffController.onNavigateTo();
            // Repopulate the table with updated data
//            staffController.repopulateTable();
        });
        buttonPanel2.add(updateButton);


        JPanel summaryInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        totalStockLabel = new JLabel("Currently in Stock : " + product.getStock());
        summaryInfoPanel.add(totalStockLabel, gbc);
        summaryInfoPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        bottomPanel.add(summaryInfoPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel);
        bottomPanel.add(buttonPanel2);
        panel.add(bottomPanel, BorderLayout.CENTER);


        // Set panel to the content pane of the dialog
        setContentPane(panel);
        setMinimumSize(new Dimension(300, 300));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        setResizable(false);
        setLocationRelativeTo(parentFrame);
    }

    private void updateStock(int newQuantity){
        totalStockLabel.setText("Total Stocks : " + newQuantity );
    }



}
