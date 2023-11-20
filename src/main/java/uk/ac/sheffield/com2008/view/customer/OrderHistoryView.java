package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.controller.customer.OrderHistoryController;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.view.components.customTable.CustomTable;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.OrderTableMapper;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class OrderHistoryView extends CustomerView {
    private final OrderHistoryController controller;
    private CustomTable<Order> customTable;

    public OrderHistoryView(OrderHistoryController controller) {
        super();
        this.controller = controller;

        initialiseUI();
    }

    private void initialiseUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        LinkedList<CustomColumn> columns = new LinkedList<>() {{
           add(new CustomColumn(0.2, "ID"));
           add(new CustomColumn(0.2, "Date"));
           add(new CustomColumn(0.2, "Status"));
           add(new CustomColumn(0.2, "Total price"));
           add(new CustomColumn(0.1, " "));
        }};
        customTable = new CustomTable<>(columns);
        add(customTable);
    }


    public void populateList(List<Order> orders) {
        OrderTableMapper mapper = new OrderTableMapper();
        customTable.populateTable(orders, mapper);
    }
}
