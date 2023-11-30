package uk.ac.sheffield.com2008.view.modals;

import uk.ac.sheffield.com2008.config.Symbols;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.view.components.Button;
import uk.ac.sheffield.com2008.view.components.Panel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OrderModal extends JDialog {
    private final Order order;
    private final JPanel content;

    public OrderModal(JFrame frame, Order order, String header, String additionalText) {
        super(frame, header, true);
        this.order = order;
        this.content = new Panel();

        initialiseUI(additionalText);

        setContentPane(content);
        setMinimumSize(new Dimension(500, 300));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 700));
        setResizable(false);
        setLocationRelativeTo(frame);
    }

    public OrderModal(JFrame frame, Order order, String header) {
        this(frame, order, header, "");
    }

    private void initialiseUI(String additionalText) {
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.setBorder(new EmptyBorder(15, 15, 15, 15));

        if (!additionalText.isEmpty()) {
            JPanel headingPanel = new Panel(new BorderLayout());
            JLabel headingLabel = new JLabel("<html>" + additionalText + "</html>");
            headingLabel.setFont(new Font(null, Font.BOLD, 14));
            headingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            headingPanel.add(headingLabel);
            headingPanel.setBorder(new EmptyBorder(0, 50, 0, 50));
            content.add(headingPanel);
        }

        JPanel mainContent = generateOrderContents();

        JPanel bottomPanel = new Panel();
        JButton closeButton = new Button("Close");
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);

        content.add(mainContent);
        content.add(bottomPanel);
        pack();
    }

    private JPanel generateOrderContents() {
        JPanel mainContent = new Panel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        JLabel header = new JLabel("Order contents:");
        header.setFont(new Font(null, Font.BOLD, 14));
        mainContent.add(header, c);

        int rowIndex = 2;
        for (OrderLine orderLine : order.getOrderLines()) {
            c.gridx = 0;
            c.gridy = rowIndex;
            c.anchor = GridBagConstraints.WEST;
            JLabel label1 = new JLabel(
                    new StringBuilder()
                            .append("<html><b>> ")
                            .append(orderLine.getProduct().getName())
                            .append("</b></html>").toString()
            );
            label1.setBorder(new EmptyBorder(0, 0, 0, 30));
            mainContent.add(label1, c);

            c.gridx = 1;
            c.anchor = GridBagConstraints.CENTER;
            JLabel label2 = new JLabel(
                    new StringBuilder().append("<html>")
                            .append(Symbols.getChar("£"))
                            .append(orderLine.getProduct().getPrice())
                            .append(" <b>x").append(orderLine.getQuantity())
                            .append("</b></html>").toString()
            );
            label2.setBorder(new EmptyBorder(0, 0, 0, 30));
            mainContent.add(label2, c);

            c.gridx = 2;
            c.anchor = GridBagConstraints.EAST;
            mainContent.add(new JLabel(Symbols.getChar("£") + orderLine.getPrice()), c);

            rowIndex++;
        }
        return mainContent;
    }
}
