package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.controller.staff.ManageOrderController;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.entities.Order;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderLineModal extends JDialog {
    private JTextArea orderLinesTextArea;

    public OrderLineModal (ManageOrderController manageOrderController, JFrame parentFrame, Order order) {

        super(parentFrame, "", true); // Set modal dialog with no title and bound to parent frame

        // Create a panel to hold the content
        JPanel panel = new JPanel(new BorderLayout());
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section with GridLayout to stack labels vertically
        JPanel topPanel = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column

        int orderNum = order.getOrderNumber(); // Retrieve the order number

        // Display order number label
        JLabel titleLabel = new JLabel("Items Ordered For Order Number " + orderNum);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        topPanel.add(titleLabel);

        // Display items ordered label
        //JLabel itemsOrderedLabel = new JLabel("Items Ordered: ");
        //itemsOrderedLabel.setFont(itemsOrderedLabel.getFont().deriveFont(Font.BOLD, 16));
        //topPanel.add(itemsOrderedLabel);

        panel.add(topPanel, BorderLayout.NORTH); // Add topPanel to the main pane

        // Display order line based on order number
        LinkedHashMap<String, String> fieldsMap = new LinkedHashMap<>();
        fieldsMap.put("Orders.orderNumber", String.valueOf(orderNum)); // Filter by order number

        List<Order> orders = OrderDAO.getOrderListByFields(fieldsMap);

        JTextArea orderLinesTextArea = new JTextArea(10, 30);
        orderLinesTextArea.setEditable(false);


        if (!orders.isEmpty()) {
            StringBuilder orderLinesText = new StringBuilder();
            for (Order fetchedOrder : orders) {
                orderLinesText.append("Order Number: ").append(fetchedOrder.getOrderNumber()).append("\n");
                orderLinesText.append("Date Ordered: ").append(fetchedOrder.getDateOrdered()).append("\n");
                orderLinesText.append("Status: ").append(fetchedOrder.getStatus()).append("\n\n");

                List<OrderLine> orderLines = fetchedOrder.getOrderLines();
                if (!orderLines.isEmpty()) {
                    orderLinesText.append("Items Ordered: \n");
                    for (OrderLine orderLine : orderLines) {
                        orderLinesText.append("Product: ").append(orderLine.getProduct().getName())
                                .append(", Quantity: ").append(orderLine.getQuantity())
                                .append(", Price: ").append(orderLine.getPrice()).append("\n");
                    }
                } else {
                    orderLinesText.append("No order lines available for this order");
                }
            }
            orderLinesTextArea.setText(orderLinesText.toString());
        } else {
            orderLinesTextArea.setText("No order found for this order number");
        }

        // Add orderLinesTextArea to the main panel
        panel.add(new JScrollPane(orderLinesTextArea), BorderLayout.CENTER);

        // Create a panel for Delete button and Fulfill checkbox
        JButton deleteButton = new JButton("Delete Order");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(deleteButton);

        // Delete the order when click on yes button
        deleteButton.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this order?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                // Call method to delete the order
                manageOrderController.deleteOrder(order);
                // Display a message and close the dialog
                JOptionPane.showMessageDialog(this, "The order has been deleted");
                dispose(); // Close the dialog
                manageOrderController.repopulateTable();
                //Refresh the UI
                manageOrderController.onNavigateTo();
            }
        });

       // Check if the order status is CONFIRMED to display the Fulfill checkbox
        if (order.getStatus() == Order.Status.CONFIRMED) {
            JCheckBox fulfilledCheckBox = new JCheckBox("Fulfill");
            bottomPanel.add(fulfilledCheckBox);

            // When checkbox is ticked, update the order status to "FULFILLED"
            fulfilledCheckBox.addActionListener(e -> {
                if (fulfilledCheckBox.isSelected()) {
                    order.setAsFulfilled();
                    // Update the status in the backend
                    manageOrderController.updateOrderStatus(order);
                    // Display a message and close the dialog
                    JOptionPane.showMessageDialog(this, "The order is now fulfilled");
                    dispose(); // Close the dialog
                    manageOrderController.repopulateTable();
                    //Refresh the UI
                    manageOrderController.onNavigateTo();
                }
            });
        }

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Set panel to the content pane of the dialog
        setContentPane(panel);
        setMinimumSize(new Dimension(500, 300));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        setResizable(false);
        setLocationRelativeTo(parentFrame);

    }

}


