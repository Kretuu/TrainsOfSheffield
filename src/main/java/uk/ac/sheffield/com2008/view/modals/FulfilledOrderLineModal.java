package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.controller.staff.FulfilledOrdersController;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FulfilledOrderLineModal extends JDialog {
    public FulfilledOrderLineModal (FulfilledOrdersController fulfilledOrdersController, JFrame parentFrame, Order order) {

        super(parentFrame, "", true); // Set modal dialog with no title and bound to parent frame

        // Create a panel to hold the content
        JPanel panel = new JPanel(new BorderLayout());
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section with GridLayout to stack labels vertically
        JPanel topPanel = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column

        int orderNum = order.getOrderNumber(); // Retrieve the order number

        // Display order number label
        JLabel titleLabel = new JLabel("Fulfilled Orders For Order Number :  " + orderNum);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        topPanel.add(titleLabel);


        panel.add(topPanel, BorderLayout.NORTH); // Add topPanel to the main pane

        // Display order line based on order number
        LinkedHashMap<String, String> fieldsMap = new LinkedHashMap<>();
        fieldsMap.put("Orders.orderNumber", String.valueOf(orderNum)); // Filter by order number

        List<Order> orders = new ArrayList<>();
        try {
            orders = OrderDAO.getOrderListByFields(fieldsMap);
        } catch (SQLException e) {
            //TODO Error message
            System.out.println("Could not connect to database. Order list was not loaded");
        }

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

        // Set panel to the content pane of the dialog
        setContentPane(panel);
        setMinimumSize(new Dimension(500, 300));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        setResizable(false);
        setLocationRelativeTo(parentFrame);
        //Refresh the UI
        fulfilledOrdersController.onNavigateTo();

    }
}
