package uk.ac.sheffield.com2008.view;

import uk.ac.sheffield.com2008.controller.BrowseItemsController;
import uk.ac.sheffield.com2008.controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        // Example: Creating 10 rows with product labels
        for (int i = 1; i <= 10; i++) {
            JLabel productLabel = new JLabel("Product " + i);
            productLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK), // Border around each row
                    BorderFactory.createEmptyBorder(5, 5, 5, 5) // Inner padding
            ));
            productLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align text to the left
            productLabel.setAlignmentY(Component.CENTER_ALIGNMENT); // Center the text vertically
            productLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, productLabel.getPreferredSize().height));
            productPanel.add(productLabel);
        }

        // Create a JScrollPane and set the productPanel as its view
        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        // Set up the Staff Area button action
        staffAreaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your action when the Staff Area button is clicked
                // For example, open a new view or perform some action
            }
        });
    }
}
