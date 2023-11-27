package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.modals.EditProductStockModal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class ManageStockView extends StaffView {

    StaffController staffController;
    private JTable table;

    public ManageStockView(StaffController staffController) {
        super();
        this.staffController = staffController;
    }

    public void onRefresh() {
        removeAll();
        initializeUI(); //Reinitialize UI
        revalidate();
        repaint();
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


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JButton productRecordButton = new JButton("Product Record");
        bottomPanel.add(productRecordButton);

        // Create a Manage Order button
        JButton manageOrderButton = new JButton("Manage Order");
        bottomPanel.add(manageOrderButton);


        // Add indentation between buttons using EmptyBorder
        int buttonIndentation = 10;
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, buttonIndentation, 0, 0));


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
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));


        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Product Code", "Product Name", "Category", "Quantity", "Action"};

        // Create a DefaultTableModel with column names and no data initially
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Set all cells to be non-editable
                return false;
            }
        };

        // Get products from the DAO
        List<Product> products = ProductDAO.getAllProducts();

        // Add each product to the tableModel
        for (Product product : staffController.getAllProducts()) {
            // Customize the category based on the productCode
            Object[] rowData = {product.getProductCode(), product.getName(), staffController.determineCustomCategory(product.getProductCode()), product.getStock(), "Edit"};
            tableModel.addRow(rowData);
        }

        table = new JTable(tableModel);
        table.setEnabled(false);


        // Center the content in "Quantity" and "Action" column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane);
        this.add(productPanel);
        productPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        productRecordButton.addActionListener(e -> staffController.getNavigation().navigate(Navigation.PRODUCT_RECORD));

        manageOrderButton.addActionListener(e -> {
            staffController.getNavigation().navigate(Navigation.MANAGE_ORDER);
            // Repopulate the table upon returning to the ManageStockView
            staffController.repopulateTable();
        });

        // Add an ActionListener to the filter combo box
        filterComboBox.addActionListener(e -> {
            String selectedCategory = (String) filterComboBox.getSelectedItem();
            // Get the initial letter based on the selected category
            String initialLetter = getInitialLetter(selectedCategory);
            // Call the filter method based on the selected starting letter
            filterTableByCategory(tableModel, initialLetter);
        });

        // Disable column dragging
        table.getTableHeader().setReorderingAllowed(false);


        // Create a custom renderer for the view hyperlink column
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setOpaque(true);  // Ensure opaque is set to true
                if (value instanceof Component) {
                    return (Component) value;
                }
                if (value instanceof String) {
                    JLabel label = new JLabel((String) value);
                    label.setForeground(Color.BLUE.darker()); // Set the text color to blue
                    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    return label;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        // Set the column alignment to center
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Add a mouse listener to the "Edit" label
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                // Check if the click is in the "Edit" column
                if (col == 4) {
                    Product product = products.get(row); // Retrieve the order from the list
                    // Create an instance of the OrderLineModal class
                    EditProductStockModal modal = new EditProductStockModal(staffController, (JFrame) SwingUtilities.getWindowAncestor(productPanel), product, ManageStockView.this);
                    // Show the dialog
                    modal.setVisible(true);
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


    private void filterTableByCategory(DefaultTableModel tableModel, String initialLetter) {
        // Clear the existing rows in the table
        tableModel.setRowCount(0);

        // Get products based on the selected category from the DAO
        List<Product> filteredProducts;
        if ("All".equals(initialLetter)) {
            // If "All" is selected, get all products
            filteredProducts = staffController.getAllProducts();
        } else {
            // Otherwise, get products for the selected category
            filteredProducts = ProductDAO.getProductsByCategory(initialLetter);
        }

        // Add each filtered product to the tableModel
        for (Product product : filteredProducts) {

            Object[] rowData = {product.getProductCode(), product.getName(), staffController.determineCustomCategory(product.getProductCode()), product.getStock(), "Edit"};
            tableModel.addRow(rowData);
            //System.out.println("Number of Rows in Table Model: " + tableModel.getRowCount());
        }

    }

    // Getter method to access the table model
    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) table.getModel();
    }


}

