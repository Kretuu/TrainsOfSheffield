package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.controller.staff.FulfilledOrdersController;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.view.components.Panel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class FulfilledOrderLineModal extends JDialog {
    public FulfilledOrderLineModal(FulfilledOrdersController fulfilledOrdersController, JFrame parentFrame, Order order) {
        super(parentFrame, "", true); // Set modal dialog with no title and bound to parent frame

        User user = fulfilledOrdersController.getOrderUser(order);
        if(user == null) {
            dispose();
            return;
        }
        PersonalDetails personalDetails = user.getPersonalDetails();
        // Create a panel to hold the content
        JPanel panel = new Panel(new BorderLayout());
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section with GridLayout to stack labels vertically
        JPanel topPanel = new Panel(new GridLayout(2, 1)); // 2 rows, 1 column

        int orderNum = order.getOrderNumber(); // Retrieve the order number

        // Display order number label
        JLabel titleLabel = new JLabel("Fulfilled Orders For Order Number :  " + orderNum);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        topPanel.add(titleLabel);


        panel.add(topPanel, BorderLayout.NORTH); // Add topPanel to the main pane

        JTextArea orderLinesTextArea = new JTextArea(10, 30);
        orderLinesTextArea.setEditable(false);


        StringBuilder orderLinesText = new StringBuilder();
        orderLinesText.append("User's name: ").append(personalDetails.getForename()).append(" ")
                .append(personalDetails.getSurname()).append("\n");
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
