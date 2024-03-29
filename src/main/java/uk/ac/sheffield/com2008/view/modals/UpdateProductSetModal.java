package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.controller.staff.EditFormController;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.view.components.Panel;
import uk.ac.sheffield.com2008.view.staff.EditProductRecordForm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class UpdateProductSetModal extends JDialog {

    public UpdateProductSetModal(EditFormController editFormController, JFrame parentFrame, EditProductRecordForm editProductRecordForm, List<Product> filteredProducts) {
        super(parentFrame, "", true);
        setSize(500, 400);

        // Create a panel to hold the content
        JPanel panel = new Panel(new BorderLayout());


        // Give the panel some padding
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section
        JPanel itemsPanel = new Panel(new BorderLayout());
        JLabel selectLabel = new JLabel("Select Items: ");
        itemsPanel.add(selectLabel, BorderLayout.NORTH);

        ButtonGroup buttonGroup = new ButtonGroup();

        // Create a panel to hold the radio buttons
        JPanel radioButtonsPanel = new Panel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));

        // Create and add radio buttons for each product
        for (Product product : filteredProducts) {
            JRadioButton radioButton = new JRadioButton(product.printName());
            radioButton.setBackground(Colors.BACKGROUND);
            radioButton.setActionCommand(product.getProductCode());
            radioButton.addActionListener(e -> {
                editProductRecordForm.setSelectedSetProduct(product);
            });
            buttonGroup.add(radioButton);
            radioButtonsPanel.add(radioButton);
        }
        itemsPanel.add(new JScrollPane(radioButtonsPanel), BorderLayout.CENTER);
        panel.add(itemsPanel, BorderLayout.CENTER);
        setContentPane(panel);
    }

}