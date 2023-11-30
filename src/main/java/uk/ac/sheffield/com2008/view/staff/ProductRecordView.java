package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.controller.staff.EditFormController;
import uk.ac.sheffield.com2008.controller.staff.ProductRecordController;

import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.TrackPack;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.util.listeners.AuthorisationActionListener;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.ProductRecordTableMapper;
import uk.ac.sheffield.com2008.view.modals.NotificationModal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class ProductRecordView extends StaffView {

    ProductRecordController productRecordController;
    private CustomTable<Product> customTable;
    private final ProductRecordTableMapper mapper;
    private JComboBox<String> filterComboBox;

    public ProductRecordView(ProductRecordController productRecordController) {
        this.productRecordController = productRecordController;
        this.mapper = new ProductRecordTableMapper() {
            @Override
            public void onClick(Product product) {
                productRecordController.getNavigation().navigate(Navigation.EDIT_PRODUCT_RECORD);

                if(product instanceof TrackPack){
                    System.out.println("in record view");
                    TrackPack productSet = (TrackPack) product;
                    productSet.PrintFullSet();
                }
                ViewController nextController = productRecordController.getNavigation().getCurrentController();
                if(nextController instanceof EditFormController){
                    EditFormController editFormController = (EditFormController) nextController;
                    editFormController.setProductUnderEdit(product);
                    editFormController.forceRefresh();
                }

            }

            @Override
            public void onDelete(Product product) {
                StringBuilder messageBuilder = new StringBuilder()
                        .append("Do you really want to delete product record of name ")
                        .append(product.printName()).append("? This cannot be undone.");
                new NotificationModal(productRecordController.getNavigation().getFrame(), ProductRecordView.this,
                        "Delete product record", messageBuilder.toString()) {
                    @Override
                    public void onSubmit() {
                        productRecordController.deleteProduct(product);
                    }
                }.setVisible(true);
            }
        };

        initializeUI();
    }

    public void initializeUI() {

        setLayout(new BorderLayout());
        int padding = 40;

        // top panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel viewLabel = new JLabel("Product Record");
        row1.add(viewLabel);
        topPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel filterLabel = new JLabel("Filter by: ");
        String[] categories = {"All", "Locomotive", "Controller", "Rolling Stock", "Track", "Train Set", "Track Pack"};
        filterComboBox = new JComboBox<>(categories);
        JButton addRecordButton = new JButton("Create New Record");
        addRecordButton.addActionListener(new AuthorisationActionListener(this) {
            @Override
            public void action(ActionEvent e) {
                productRecordController.getNavigation().navigate(Navigation.PRODUCTFORM);
            }
        });

        filterComboBox.setToolTipText("Select a category to filter the products");
        row2.add(filterLabel);
        row2.add(filterComboBox);
        row2.add(addRecordButton);
        row2.setBorder(BorderFactory.createEmptyBorder(0, padding, 0, 0));
        topPanel.add(row2);

        add(topPanel, BorderLayout.NORTH);

        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

        LinkedList<CustomColumn> columns = new LinkedList<>() {{
            add(new CustomColumn(0.4, "Product Name"));
            add(new CustomColumn(0.2, "Category"));
            add(new CustomColumn(0.2, "Quantity"));
            add(new CustomColumn(0.2, null));
        }};
        customTable = new CustomTable<>(columns, productRecordController.getNavigation().getFrame());

        JScrollPane scrollPane = new JScrollPane(customTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Dimension dimension = productRecordController.getNavigation().getFrame().getPreferredSize();
        dimension.width = dimension.width - 100;
        scrollPane.setPreferredSize(new Dimension(dimension.width, 700));
        productPanel.add(scrollPane);
        productPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(productPanel);


        // Add an ActionListener to the filter combo box
        filterComboBox.addActionListener(e -> {
            String selectedCategory = (String) filterComboBox.getSelectedItem();
            // Get the initial letter based on the selected category
            String initialLetter = getInitialLetter(selectedCategory);
            // Call the filter method based on the selected starting letter
            productRecordController.setCurrentFilter(initialLetter);
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
        } else if ("Rolling Stock".equals(selectedCategory)) {
            return "S";
        }else if ("Track".equals(selectedCategory)) {
            return "R";
        }else if ("Train Set".equals(selectedCategory)) {
            return "M";
        }else if ("Track Pack".equals(selectedCategory)) {
            return "P";
        } else {
            return "";
        }
    }
}
