package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.controller.staff.FulfilledOrdersController;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.OrderTableMapper;
import uk.ac.sheffield.com2008.view.modals.FulfilledOrderLineModal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class FulfilledOrdersView extends StaffView {

    FulfilledOrdersController fulfilledOrdersController;
    private CustomTable<Order> customTable;
    private final OrderTableMapper mapper;

    public FulfilledOrdersView(FulfilledOrdersController fulfilledOrdersController) {
        this.fulfilledOrdersController = fulfilledOrdersController;
        this.mapper = new OrderTableMapper() {
            @Override
            public void onClick(Order order) {
                new FulfilledOrderLineModal(
                            fulfilledOrdersController, fulfilledOrdersController.getNavigation().getFrame(), order
                    ).setVisible(true);
            }
        };

        initializeUI();
    }

    public void initializeUI() {
        final JPanel panel = new JPanel(); // Making 'panel' final

        setLayout(new BorderLayout());
        int padding = 40;

        // top panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //Create a label for Fulfilled Orders
        JLabel viewLabel = new JLabel("Fulfilled Orders");
        // Add the top panel to the top of the frame
        row1.add(viewLabel);
        topPanel.add(row1);
        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        /*// Create a home button
        JButton navigationButton = new JButton("Home");
        bottomPanel.add(navigationButton);


        // Add indentation between buttons using EmptyBorder
        int buttonIndentation = 40;
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, buttonIndentation, 0, 0));

        // Add the bottom panel to the bottom of the frame
        add(bottomPanel, BorderLayout.SOUTH);
        navigationButton.addActionListener(e -> fulfilledOrdersController.getNavigation().navigate(Navigation.STAFF));*/


        // Create a JPanel for the scroll panel with table
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        LinkedList<CustomColumn> columns = new LinkedList<>() {{
            add(new CustomColumn(0.2, "ID"));
            add(new CustomColumn(0.2, "Date"));
            add(new CustomColumn(0.2, "Status"));
            add(new CustomColumn(0.2, "Total price"));
            add(new CustomColumn(0.1, null));
        }};
        customTable = new CustomTable<>(columns, fulfilledOrdersController);

        panel.add(customTable);
        panel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        add(panel);
    }

    public void populateOrdersInTable() {
        List<Order> orders = fulfilledOrdersController.getFulfilledOrders();
        customTable.updateDimension(fulfilledOrdersController, 700);
        customTable.populateTable(orders, mapper);
    }

}
