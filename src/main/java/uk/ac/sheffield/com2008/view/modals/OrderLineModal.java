/*
package uk.ac.sheffield.com2008.view.modals;


import uk.ac.sheffield.com2008.controller.staff.ManageOrderController;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class OrderLineModal extends JDialog {
    private JTextArea orderLinesTextArea;

    public OrderLineModal (ManageOrderController manageOrderController, JFrame parentFrame, int orderNumber) {

        super(parentFrame, "", true); // Set modal dialog with no title and bound to parent frame
        // Create a panel to hold the content
        JPanel panel = new JPanel(new BorderLayout());

        // Give the panel some padding
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(emptyBorder);

        // Create a panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());

        int orderNum = order.getOrderNumber(); // Retrieve the order number

        // Create "Order Number" label at the top
        JLabel titleLabel = new JLabel("Order Number:" + orderNum);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        List<OrderLine> orderLines = OrderDAO.getOrderLinesByOrderNumber(orderNumber);
        orderLinesTextArea = new JTextArea(10, 30); // rows x columns
        orderLinesTextArea.setEditable(false);

        if (orderLines != null && !orderLines.isEmpty()) {
            StringBuilder orderLinesText = new StringBuilder();
            for (OrderLine line : orderLines) {
                orderLinesText.append("Product: ").append(line.getProduct().getName())
                        .append(", Quantity: ").append(line.getQuantity())
                        .append(", Price: ").append(line.getPrice()).append("\n");
            }
            orderLinesTextArea.setText(orderLinesText.toString());
        } else {
            orderLinesTextArea.setText("No order lines available");
        }

        JScrollPane orderLinesScrollPane = new JScrollPane(orderLinesTextArea);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(orderLinesScrollPane, BorderLayout.CENTER);







        // Create a panel for quantity and add to cart button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create "Quantity:" label
        JLabel quantityLabel = new JLabel("Quantity:");
        buttonPanel.add(quantityLabel);

        // Set panel to the content pane of the dialog
        setContentPane(panel);
        setMinimumSize(new Dimension(300, 200));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        setResizable(false);
        setLocationRelativeTo(parentFrame);


    }

}

*/

