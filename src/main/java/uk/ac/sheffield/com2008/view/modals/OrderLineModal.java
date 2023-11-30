package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.controller.staff.ManageOrderController;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.view.components.Button;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class OrderLineModal extends JDialog {
    //private JTextArea orderLinesTextArea;
    private final JCheckBox fulfilledCheckBox;

    public OrderLineModal(ManageOrderController manageOrderController, JFrame parentFrame, Order order) {

        super(parentFrame, "", true); // Set modal dialog with no title and bound to parent frame

        // Create a panel to hold the content
        JPanel panel = new JPanel(new BorderLayout());
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section with GridLayout to stack labels vertically
        JPanel topPanel = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column

        int orderNum = order.getOrderNumber(); // Retrieve the order number

        // Display order number label
        JLabel titleLabel = new JLabel("Items Ordered For Order Number : " + orderNum);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        topPanel.add(titleLabel);


        panel.add(topPanel, BorderLayout.NORTH); // Add topPanel to the main pane

        JTextArea orderLinesTextArea = new JTextArea(10, 30);
        orderLinesTextArea.setEditable(false);


        StringBuilder orderLinesText = new StringBuilder();
        orderLinesText.append("Order Number: ").append(order.getOrderNumber()).append("\n");
        orderLinesText.append("Date Ordered: ").append(order.getDateOrdered()).append("\n");
        orderLinesText.append("Status: ").append(order.getStatus()).append("\n\n");

        List<OrderLine> orderLines = order.getOrderLines();
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
        orderLinesTextArea.setText(orderLinesText.toString());

        // Add orderLinesTextArea to the main panel
        panel.add(new JScrollPane(orderLinesTextArea), BorderLayout.CENTER);

        // Create a panel for Delete button and Fulfill checkbox
        JButton deleteButton = new Button("Delete Order");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(deleteButton);

        // Delete the order when click on yes button
        deleteButton.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Delete Order?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                // Call method to delete the order
                manageOrderController.deleteOrder(order);
                // Display a message and close the dialog
                JOptionPane.showMessageDialog(this, "The order has been deleted");
                dispose(); // Close the dialog
                //Refresh the UI
                manageOrderController.onNavigateTo();
            }
        });

        fulfilledCheckBox = new JCheckBox("Fulfill");
        // Check if the order status is CONFIRMED to display the Fulfill checkbox
        if (order.getStatus() == Order.Status.CONFIRMED) {
            bottomPanel.add(fulfilledCheckBox);

            // When checkbox is ticked, update the order status to "FULFILLED"
            fulfilledCheckBox.addActionListener(e -> {
                if (fulfilledCheckBox.isSelected()) manageOrderController.fulfillOrder(order, this);
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

    public void unselectFulfilledCheckbox() {
        fulfilledCheckBox.setSelected(false);
    }

}


