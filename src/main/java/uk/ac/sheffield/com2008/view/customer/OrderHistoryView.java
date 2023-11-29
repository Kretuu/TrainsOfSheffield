package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.controller.customer.OrderHistoryController;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.OrderTableMapper;
import uk.ac.sheffield.com2008.view.modals.OrderModal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class OrderHistoryView extends CustomerView {
    private final OrderHistoryController controller;
    private final OrderTableMapper mapper;
    private CustomTable<Order> customTable;

    public OrderHistoryView(OrderHistoryController controller) {
        this.controller = controller;
        mapper = new OrderTableMapper() {
            @Override
            public void onClick(Order order) {
                new OrderModal(controller.getNavigation().getFrame(), order, "Order details").setVisible(true);
            }
        };

        initialiseUI();
    }

    private void initialiseUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER));

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
        add(scrollPane);
    }


    public void populateList(List<Order> orders) {
        customTable.populateTable(orders, mapper);
    }
}
