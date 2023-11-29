package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.ManageOrderController;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.OrderTableMapper;
import uk.ac.sheffield.com2008.view.modals.OrderLineModal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;


public class ManageOrdersView extends StaffView {

    ManageOrderController manageOrderController;
    private CustomTable<Order> customTable;
    private final OrderTableMapper mapper;

    public ManageOrdersView(ManageOrderController manageOrderController) {
        this.manageOrderController = manageOrderController;
        this.mapper = new OrderTableMapper() {
            @Override
            public void onClick(Order order) {
                new OrderLineModal(
                            manageOrderController, manageOrderController.getNavigation().getFrame(), order
                    ).setVisible(true);
            }
        };

        initializeUI();
    }

    public void initializeUI() {

        setLayout(new BorderLayout());
        int padding = 40;

        // top panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //Create a label for Manage Orders
        JLabel viewLabel = new JLabel("Confirm Orders");
        // Add the top panel to the top of the frame
        row1.add(viewLabel);
        topPanel.add(row1);
        add(topPanel, BorderLayout.NORTH);

        /*JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        // Create a Fulfilled Orders button
        JButton fulfilledOrdersButton = new JButton("Fulfilled Orders");
        bottomPanel.add(fulfilledOrdersButton);


        // Add indentation between buttons using EmptyBorder
        int buttonIndentation = 40;
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, buttonIndentation, 0, 0));

        // Add the bottom panel to the bottom of the frame
        add(bottomPanel, BorderLayout.SOUTH);
        fulfilledOrdersButton.addActionListener(e -> manageOrderController.getNavigation().navigate(Navigation.FULFILLED_ORDERS));*/


        final JPanel panel = new JPanel(); // Making 'panel' final


        // Create a JPanel for the scroll panel with table

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        LinkedList<CustomColumn> columns = new LinkedList<>() {{
            add(new CustomColumn(0.2, "ID"));
            add(new CustomColumn(0.2, "Date"));
            add(new CustomColumn(0.2, "Status"));
            add(new CustomColumn(0.2, "Total price"));
            add(new CustomColumn(0.1, null));
        }};
        customTable = new CustomTable<>(columns);

        JScrollPane scrollPane = new JScrollPane(customTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1300, 700));
        panel.add(scrollPane);
        panel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        add(panel);
    }

    public void populateOrdersInTable() {
        List<Order> orders = manageOrderController.getAllOrders();
        customTable.populateTable(orders, mapper);
    }
}
