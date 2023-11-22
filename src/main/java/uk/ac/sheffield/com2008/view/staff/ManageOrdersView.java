package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.ManageOrderController;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.navigation.Navigation;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;



public class ManageOrdersView extends StaffView {

    private JTable table;

    ManageOrderController manageOrderController;

    public ManageOrdersView(ManageOrderController manageOrderController) {
        this.manageOrderController = manageOrderController;
        InitializeUI();
    }

    public void InitializeUI() {

        setLayout(new BorderLayout());
        int padding = 40;

        // top panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //Create a label for Manage Orders
        JLabel viewLabel = new JLabel("Manage Orders");
        // Add the top panel to the top of the frame
        row1.add(viewLabel);
        topPanel.add(row1);
        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        // Create a Fulfilled Orders button
        JButton fulfilledOrdersButton = new JButton("Fulfilled Orders");
        bottomPanel.add(fulfilledOrdersButton);

        // Create a Sales button
        JButton salesButton = new JButton("Sales");
        bottomPanel.add(salesButton);

        JButton navigationButton = new JButton("Home");
        bottomPanel.add(navigationButton);


        // Add indentation between buttons using EmptyBorder
        int buttonIndentation = 40;
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, buttonIndentation, 0, 0));

        // Add the bottom panel to the bottom of the frame
        add(bottomPanel, BorderLayout.SOUTH);
        salesButton.addActionListener(e -> manageOrderController.getNavigation().navigate(Navigation.SALES));
        navigationButton.addActionListener(e -> manageOrderController.getNavigation().navigate(Navigation.STAFF));


        // Create a JPanel for the scroll panel with product labels
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] columnNames = {"Order Number", "Date Ordered", "Status", "Total Price", "Action"};

        // Create a DefaultTableModel with column names and no data initially
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Create the JTable using the DefaultTableModel
        table = new JTable(tableModel);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane);
        this.add(productPanel);
        productPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        // Populate orders into the table
        populateOrdersInTable();

    }

    private void populateOrdersInTable() {
        // Fetch all orders using OrderDAO
        List<Order> orders = OrderDAO.getAllOrders(); // You need to implement this method in your OrderDAO

        // Populate orders and order lines into the JTable
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // Populate orders into the JTable
        for (Order order : orders) {
            Object[] rowData = {
                    order.getOrderNumber(),
                    order.getDateOrdered(),
                    order.getStatus(),
                    order.getTotalPrice(),
                    "View"
            };
            tableModel.addRow(rowData);
        }

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
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Add a mouse listener to the "Edit" label
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                // Check if the click is in the "Edit" column
                if (col == 4) {
                    // Define the action to take when the link is clicked
                    manageOrderController.getNavigation().navigate(Navigation.ORDER_LIST);
                }
            }
        });

    }




}