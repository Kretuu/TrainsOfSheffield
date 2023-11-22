package uk.ac.sheffield.com2008.view.components.customTable.mappers;

import uk.ac.sheffield.com2008.model.entities.Order;

import javax.swing.*;
import java.util.Date;
import java.util.LinkedList;

public class OrderTableMapper implements TableMapper<Order> {
    @Override
    public LinkedList<Object> constructColumns(Order object) {
        LinkedList<Object> list = new LinkedList<>();
        list.add(object.getOrderNumber());

        Date date = object.getDateOrdered();
        list.add(date == null ? "N/A" : date.toString());
        list.add(object.getStatus().toString());
        list.add(object.getTotalPrice());

        JButton button = new JButton("View Details");
        button.addActionListener(e -> {
            System.out.println("view details clicked: " + object.getOrderNumber());
        });
        list.add(button);
        return list;
    }
}
