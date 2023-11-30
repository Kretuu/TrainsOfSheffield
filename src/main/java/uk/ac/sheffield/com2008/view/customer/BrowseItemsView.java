package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.controller.customer.BrowseItemsController;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
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
    BrowseItemsController browseItemsController;

    private CustomTable<Product> customTable;
    private final ProductCustomerTableMapper mapper;
    public BrowseItemsView(BrowseItemsController browseItemsController){
        this.browseItemsController = browseItemsController;
        this.mapper = new ProductCustomerTableMapper() {
            @Override
            public void onClick(Product product) {
                System.out.println("clicked ADD for product: " + product);
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

    public void InitializeUI(){
        setLayout(new BorderLayout());

        // Create a JPanel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("welcome");
        topPanel.add(welcomeLabel, BorderLayout.WEST);


        JPanel topButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton staffAreaButton = new JButton("Staff Area");
        JButton basketButton = new JButton("Basket");
        topButtonsPanel.add(basketButton);
        topButtonsPanel.add(staffAreaButton);
        // Set up the Staff Area button action
        staffAreaButton.addActionListener(e -> browseItemsController.getNavigation().navigate(Navigation.STAFF));
        // Set up Basket button action
        basketButton.addActionListener(e -> browseItemsController.getNavigation().navigate(Navigation.BASKET));

        topPanel.add(topButtonsPanel, BorderLayout.EAST);

        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        LinkedList<CustomColumn> columns = new LinkedList<>() {{
            add(new CustomColumn(0.6, "Product Name"));
            add(new CustomColumn(0.2, "Category"));
            add(new CustomColumn(0.2, null));
        }};
        customTable = new CustomTable<>(columns);

        productPanel.add(customTable);
        productPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(productPanel);
    }

    public void refreshTable(List<Product> productList) {
        customTable.updateDimension(browseItemsController, 700);
        customTable.populateTable(productList, mapper);
    }
}
