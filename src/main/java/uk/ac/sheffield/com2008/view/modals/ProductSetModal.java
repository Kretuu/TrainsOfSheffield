package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.controller.staff.FormController;
import uk.ac.sheffield.com2008.view.staff.ProductRecordForm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ProductSetModal extends JDialog {


    public ProductSetModal(FormController formController, JFrame parentFrame){
        super(parentFrame, "", true);

        // Create a panel to hold the content
        JPanel panel = new JPanel(new BorderLayout());

        // Give the panel some padding
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section
        JPanel itemsPanel = new JPanel(new BorderLayout());
        JLabel selectLabel = new JLabel("Select Items: ");
    }

}
