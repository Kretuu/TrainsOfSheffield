package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.controller.customer.BrowseItemsController;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.util.ProductCategoryHelper;
import uk.ac.sheffield.com2008.view.components.Button;
import uk.ac.sheffield.com2008.view.components.Panel;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.ProductCustomerTableMapper;
import uk.ac.sheffield.com2008.view.modals.AddProductToCartModal;
import uk.ac.sheffield.com2008.view.user.UserView;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class BrowseItemsView extends UserView {
    private final ProductCustomerTableMapper mapper;
    BrowseItemsController browseItemsController;
    private CustomTable<Product> customTable;

    public BrowseItemsView(BrowseItemsController browseItemsController) {
        this.browseItemsController = browseItemsController;
        this.mapper = new ProductCustomerTableMapper() {
            @Override
            public void onClick(Product product) {
                // Create an instance of the AddProductToCartModal class
                AddProductToCartModal modal = new AddProductToCartModal(
                        BrowseItemsView.this.browseItemsController,
                        browseItemsController.getNavigation().getFrame(),
                        product);

                modal.setVisible(true); // Show the modal dialog
            }
        };
        InitializeUI();
    }

    public void InitializeUI() {
        setLayout(new BorderLayout());

        // Create a JPanel for the top section
        JPanel topPanel = new Panel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel row = new Panel(new FlowLayout(FlowLayout.LEFT));
        JLabel filterLabel = new JLabel("Filter by: ");
        String[] categories = {"All", "Locomotive", "Controller", "Rolling Stock", "Track", "Train Set", "Track Pack"};
        JComboBox<String> filterComboBox = new JComboBox<>(categories);

        filterComboBox.setToolTipText("Select a category to filter the products");
        // Add an ActionListener to the filter combo box
        filterComboBox.addActionListener(e -> {
            String selectedCategory = (String) filterComboBox.getSelectedItem();
            String initialLetter = ProductCategoryHelper.getInitialLetter(selectedCategory);
            // Call the filter method based on the selected starting letter
            browseItemsController.setCurrentFilter(initialLetter);
        });
        row.add(filterLabel);
        row.add(filterComboBox);
        row.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
        topPanel.add(row);

        add(row, BorderLayout.NORTH);


        JPanel topButtonsPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        JButton staffAreaButton = new Button("Staff Area");
        JButton basketButton = new Button("Basket");
        topButtonsPanel.add(basketButton);
        topButtonsPanel.add(staffAreaButton);
        // Set up the Staff Area button action
        staffAreaButton.addActionListener(e -> browseItemsController.getNavigation().navigate(Navigation.STAFF));
        // Set up Basket button action
        basketButton.addActionListener(e -> browseItemsController.getNavigation().navigate(Navigation.BASKET));

        topPanel.add(topButtonsPanel, BorderLayout.EAST);

        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new Panel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        LinkedList<CustomColumn> columns = new LinkedList<>() {{
            add(new CustomColumn(0.6, "Product Name"));
            add(new CustomColumn(0.2, "Category"));
            add(new CustomColumn(0.2, null));
        }};
        customTable = new CustomTable<>(columns, browseItemsController);

        productPanel.add(customTable);
        productPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(productPanel, BorderLayout.CENTER);
    }

    public void refreshTable(List<Product> productList) {
        customTable.updateDimension(browseItemsController, 500);
        customTable.populateTable(productList, mapper);
    }
}
