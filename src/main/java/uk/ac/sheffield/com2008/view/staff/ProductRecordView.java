package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.ProductRecordController;

import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.util.listeners.AuthorisationActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ProductRecordView extends StaffView {

    ProductRecordController productRecordController;

    private JTable table;

    public ProductRecordView(ProductRecordController productRecordController) {
        this.productRecordController = productRecordController;
    }

    public void onRefresh(){
        removeAll();
        initializeUI();
        revalidate();
        repaint();
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
        JComboBox<String> filterComboBox = new JComboBox<>(categories);
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
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] columnNames = {"Product Code", "Product Name", "Category", "Quantity", "Action"};

        // Create a DefaultTableModel with column names and no data initially
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Fetch all products from ProductDAO
        List<Product> products = new ArrayList<>();
        try {
            products = ProductDAO.getAllProducts();
        } catch (SQLException e) {
            //TODO Error message
            System.out.println("Cannot connect to database. Product list is not loaded");
        }

        // Add each product to the tableModel
        for (Product product : products) {
            Object[] rowData = {
                    product.getProductCode(),
                    product.getName(),
                    productRecordController.determineCustomCategory(product.getProductCode()),
                    product.getStock(),
                    "Edit"};
            tableModel.addRow(rowData);
        }

        table = new JTable(tableModel); // Assigning to the class-level variable
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane);
        this.add(productPanel);
        productPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        // Center the content in "Quantity" and "Action" column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Create a JPanel for the bottom section with BoxLayout in Y_AXIS
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JButton navigationButton = new JButton("Home");
        bottomPanel.add(navigationButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
        navigationButton.addActionListener(e -> productRecordController.getNavigation().navigate(Navigation.STAFF));
        add(bottomPanel, BorderLayout.SOUTH);

        // Add an ActionListener to the filter combo box
        filterComboBox.addActionListener(e -> {
            String selectedCategory = (String) filterComboBox.getSelectedItem();
            // Get the initial letter based on the selected category
            String initialLetter = getInitialLetter(selectedCategory);
            // Call the filter method based on the selected starting letter
            filterTableByCategory(tableModel, initialLetter);
        });

        //custom renderer to edit records
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setOpaque(true);  // Ensure opaque is set to true
                if (value instanceof Component) {
                    return (Component) value;
                }
                if (value instanceof String) {
                    JLabel label = new JLabel((String) value);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setForeground(Color.BLUE.darker());
                    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    return label;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });


        // Add a mouse listener to the "Edit" label
        List<Product> finalProducts = products;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                // Check if the click is in the "Edit" column
                if (col == 4) {
                    // Define the action to take when the link is clicked
                    Product product = finalProducts.get(row);
                    productRecordController.getNavigation().navigate(Navigation.EDIT_PRODUCT_RECORD);
                }
            }
        });

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

    private void filterTableByCategory(DefaultTableModel tableModel, String initialLetter) {
        try {
            // Clear the existing rows in the table
            tableModel.setRowCount(0);

            // Get products based on the selected category from the DAO
            List<Product> filteredProducts;
            if ("All".equals(initialLetter)) {
                // If "All" is selected, get all products
                filteredProducts = ProductDAO.getAllProducts();
            } else {
                // Otherwise, get products for the selected category
                filteredProducts = ProductDAO.getProductsByCategory(initialLetter);
            }

            // Add each filtered product to the tableModel
            for (Product product : filteredProducts) {

                Object[] rowData = {product.getProductCode(), product.getName(), productRecordController.determineCustomCategory(product.getProductCode()), product.getStock(), "Edit"};
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            //TODO Error message
            System.out.println("Cannot connect to database. Product list was not filtered.");
        }


    }

}
