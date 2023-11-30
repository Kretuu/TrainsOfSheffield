package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.SalesController;
import uk.ac.sheffield.com2008.view.components.Panel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class SalesView extends StaffView {
    SalesController salesController;

    public SalesView(SalesController salesController) {
        this.salesController = salesController;
        initializeUI();
    }

    public void onRefresh() {
        removeAll();
        initializeUI(); //Reinitialize UI
        revalidate();
        repaint();
    }

    public void initializeUI() {
        setLayout(new GridBagLayout());

        // top panel
        JPanel topPanel = new Panel(new GridLayout(2, 1));

        JPanel row1 = new Panel(new FlowLayout(FlowLayout.CENTER));
        // Create a label for Manage Orders
        JLabel viewLabel = new JLabel("Sales Dashboard");
        viewLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        // Add the top panel to the top of the frame
        row1.add(viewLabel);
        topPanel.add(row1);

        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.gridx = 0;
        gbcTop.gridy = 0;
        gbcTop.gridwidth = 2;
        gbcTop.insets = new Insets(10, 10, 10, 10);
        add(topPanel, gbcTop);

        // Create the left panel for monthly orders
        JPanel monthlyOrdersPanel = new Panel();
        monthlyOrdersPanel.setBorder(createBorder("Total Orders", 20));
        monthlyOrdersPanel.setPreferredSize(new Dimension(400, 300));

        // Sample label in the left panel (replace with your data)
        JLabel monthlyOrdersLabel = new JLabel("Total Orders: " + salesController.getNumberOfSales());
        monthlyOrdersPanel.add(monthlyOrdersLabel);

        // Make the text larger
        Font largerText = monthlyOrdersLabel.getFont().deriveFont(Font.BOLD, 16);
        monthlyOrdersLabel.setFont(largerText);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(monthlyOrdersPanel, gbc);

        // Create the right panel for total sales
        JPanel totalSalesPanel = new Panel();
        totalSalesPanel.setBorder(createBorder("Total Sales", 20));
        totalSalesPanel.setPreferredSize(new Dimension(400, 300));


        // Sample label in the right panel (replace with your data)
        JLabel totalSalesLabel = new JLabel("Total Sales Amount: " + salesController.getTotalSales());

        // Make the text larger
        Font largerFont = totalSalesLabel.getFont().deriveFont(Font.BOLD, 16);
        totalSalesLabel.setFont(largerFont);

        totalSalesPanel.add(totalSalesLabel);

        gbc.gridx = 1;
        add(totalSalesPanel, gbc);


        GridBagConstraints gbcBottom = new GridBagConstraints();
        gbcBottom.gridx = 0;
        gbcBottom.gridy = 2; // Place it in the third row, at the bottom
        gbcBottom.gridwidth = 2; // Span across two columns
        gbcBottom.anchor = GridBagConstraints.WEST;
        gbcBottom.insets = new Insets(90, 10, 10, 10); // Add padding


    }

    private Border createBorder(String title, int fontSize) {
        Border line = new LineBorder(Color.BLACK, 3); // Thick black border
        Border empty = new EmptyBorder(10, 10, 10, 10); // Padding around the border
        Border compound = new CompoundBorder(line, empty); // Compound border
        TitledBorder titledBorder = BorderFactory.createTitledBorder(compound, title,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font(Font.SANS_SERIF, Font.BOLD, fontSize), Color.BLACK); // Title border with increased font size
        titledBorder.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize)); // Set font for title
        return titledBorder;
    }
}
