package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.ProductTableMapper;
import uk.ac.sheffield.com2008.view.modals.EditProductStockModal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ManageStockView extends StaffView {

    StaffController staffController;
    private CustomTable<Product> customTable;
    private final ProductTableMapper mapper;

    public ManageStockView(StaffController staffController) {
        super();
        this.staffController = staffController;
        this.mapper = new ProductTableMapper("Edit stock") {
            @Override
            public void onClick(Product product) {
                new EditProductStockModal(
                        staffController, staffController.getNavigation().getFrame(),
                        product, ManageStockView.this
                ).setVisible(true);
            }
        };

        initializeUI();
    }

    public void initializeUI() {
        setLayout(new BorderLayout());

        // top panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //Create a label for products in stock
        JLabel viewLabel = new JLabel("Products In Stock");
        row1.add(viewLabel);
        topPanel.add(row1);


        /*JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        // Create a Manage Order button
        JButton manageOrderButton = new JButton("Manage Order");
        bottomPanel.add(manageOrderButton);


        // Add indentation between buttons using EmptyBorder
        int buttonIndentation = 10;
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, buttonIndentation, 0, 0));*/


        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel filterLabel = new JLabel("Filter by: ");
        String[] categories = {"All", "Locomotive", "Controller", "Rolling Stock", "Track", "Train Set", "Track Pack"};
        JComboBox<String> filterComboBox = new JComboBox<>(categories);
        // Set tooltip for the combo box
        filterComboBox.setToolTipText("Select a category to filter the products");


        // Add the filter panel to the frame
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
        topPanel.add(filterPanel);
        add(topPanel, BorderLayout.NORTH);
        //topPanel.add(filterPanel, BorderLayout.SOUTH);


        // Add the bottom panel to the bottom of the frame
       /* add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));*/


        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

        LinkedList<CustomColumn> columns = new LinkedList<>() {{
            add(new CustomColumn(0.4, "Product Name"));
            add(new CustomColumn(0.2, "Category"));
            add(new CustomColumn(0.2, "Quantity"));
            add(new CustomColumn(0.2, null));
        }};
        customTable = new CustomTable<>(columns);

        JScrollPane scrollPane = new JScrollPane(customTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1300, 700));
        productPanel.add(scrollPane);
        productPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(productPanel);

        /*
        manageOrderButton.addActionListener(e -> {
            staffController.getNavigation().navigate(Navigation.MANAGE_ORDER);
            // Repopulate the table upon returning to the ManageStockView
            staffController.onNavigateTo();
        });*/

        // Add an ActionListener to the filter combo box
        filterComboBox.addActionListener(e -> {
            String selectedCategory = (String) filterComboBox.getSelectedItem();
            // Get the initial letter based on the selected category
            String initialLetter = getInitialLetter(selectedCategory);
            // Call the filter method based on the selected starting letter
            staffController.setCurrentFilter(initialLetter);
        });
    }

    public void populateTable(List<Product> products) {
        customTable.populateTable(products, mapper);
    }

    // Method to get the initial letter based on the selected category
    private String getInitialLetter(String selectedCategory) {
        if ("Locomotive".equals(selectedCategory)) {
            return "L";
        } else if ("Controller".equals(selectedCategory)) {
            return "C";
        } else if ("Track".equals(selectedCategory)) {
            return "R";
        } else if ("Rolling Stock".equals(selectedCategory)) {
            return "S";
        } else if ("Train Set".equals(selectedCategory)) {
            return "M";
        } else if ("Train Pack".equals(selectedCategory)) {
            return "P";
        } else {
            return "";
        }
    }
}

