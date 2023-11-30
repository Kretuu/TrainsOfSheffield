package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.util.ProductCategoryHelper;
import uk.ac.sheffield.com2008.view.components.Panel;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.ProductTableMapper;
import uk.ac.sheffield.com2008.view.modals.EditProductStockModal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ManageStockView extends StaffView {

    private final ProductTableMapper mapper;
    private final StaffController staffController;
    private CustomTable<Product> customTable;

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
        JPanel topPanel = new Panel(new GridLayout(2, 1));

        JPanel row1 = new Panel(new FlowLayout(FlowLayout.CENTER));
        //Create a label for products in stock
        JLabel viewLabel = new JLabel("Products In Stock");
        row1.add(viewLabel);
        topPanel.add(row1);


        JPanel filterPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
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


        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new Panel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

        LinkedList<CustomColumn> columns = new LinkedList<>() {{
            add(new CustomColumn(0.4, "Product Name"));
            add(new CustomColumn(0.2, "Category"));
            add(new CustomColumn(0.2, "Quantity"));
            add(new CustomColumn(0.2, null));
        }};
        customTable = new CustomTable<>(columns, staffController);

        productPanel.add(customTable);
        productPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(productPanel);

        // Add an ActionListener to the filter combo box
        filterComboBox.addActionListener(e -> {
            String selectedCategory = (String) filterComboBox.getSelectedItem();
            // Get the initial letter based on the selected category
            String initialLetter = ProductCategoryHelper.getInitialLetter(selectedCategory);
            // Call the filter method based on the selected starting letter
            staffController.setCurrentFilter(initialLetter);
        });
    }

    public void populateTable(List<Product> products) {
        customTable.updateDimension(staffController, 700);
        customTable.populateTable(products, mapper);
    }
}

