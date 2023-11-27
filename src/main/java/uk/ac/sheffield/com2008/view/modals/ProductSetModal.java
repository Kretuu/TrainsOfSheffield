package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.controller.staff.FormController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.view.staff.ProductRecordForm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class ProductSetModal extends JDialog {


    public ProductSetModal(FormController formController, JFrame parentFrame, ProductRecordForm productRecordForm, List<Product> filteredProducts){
        super(parentFrame, "", true);
        setSize(500, 400);

        // Create a panel to hold the content
        JPanel panel = new JPanel(new BorderLayout());


        // Give the panel some padding
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section
        JPanel itemsPanel = new JPanel(new BorderLayout());
        JLabel selectLabel = new JLabel("Select Items: ");
        itemsPanel.add(selectLabel, BorderLayout.NORTH);

        ButtonGroup buttonGroup = new ButtonGroup();

        // Create a panel to hold the radio buttons
        JPanel radioButtonsPanel = new JPanel(new GridLayout(filteredProducts.size(), 1));

        // Create and add radio buttons for each product
        for (Product product : filteredProducts) {
            JRadioButton radioButton = new JRadioButton(product.getName());
            radioButton.setActionCommand(product.getProductCode());
            radioButton.addActionListener(e -> {
                // Retrieve the selected product name using the action command (product code)
                String selectedProductCode = radioButton.getActionCommand();
                System.out.println("Selected Product Code: " + selectedProductCode);
                String selectedProductName = findProductNameByCode(filteredProducts, selectedProductCode);
                System.out.println("Selected Product Name: " + selectedProductName);
                // Update the itemSelected label in ProductRecordForm
                productRecordForm.updateItemSelectedLabel(selectedProductName);
            });
            buttonGroup.add(radioButton);
            radioButtonsPanel.add(radioButton);
        }

        itemsPanel.add(new JScrollPane(radioButtonsPanel), BorderLayout.CENTER);
        panel.add(itemsPanel, BorderLayout.CENTER);



        setContentPane(panel);




    }

    private String findProductNameByCode(List<Product> products, String productCode) {
        for (Product product : products) {
            if (product.getProductCode().equals(productCode)) {
                return product.getName();
            }
        }
        return null;
    }

}
