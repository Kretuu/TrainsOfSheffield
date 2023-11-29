package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.modals.EditProductStockModal;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ManageStockView extends StaffView {

    StaffController staffController;
    private JTable table;

    public ManageStockView(StaffController staffController) {
        super();
        this.staffController = staffController;

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
//            filterTableByCategory(tableModel, initialLetter);
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
                    Product product = staffController.getCurrentDisplayedProducts().get(row); // Retrieve the order from the list
                    // Create an instance of the OrderLineModal class
                    EditProductStockModal modal = new EditProductStockModal(
                            staffController, staffController.getNavigation().getFrame(),
                            product, ManageStockView.this
                    );
                    // Show the dialog
                    modal.setVisible(true);
                }
            }
        });
    }

    public void populateTable(List<Product> products) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        if (tableModel != null) {

            // Clear existing rows in the table model
            tableModel.setRowCount(0);

            // Update the table with the fetched data
            for (Product product : products) {
                Object[] rowData = {product.getProductCode(), product.getName(), staffController.determineCustomCategory(product.getProductCode()), product.getStock(), "Edit"};
                tableModel.addRow(rowData);
            }

        }
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

