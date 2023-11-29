package uk.ac.sheffield.com2008.view.components.customTable.mappers;

import uk.ac.sheffield.com2008.model.entities.Product;

import javax.swing.*;
import java.util.LinkedList;

public abstract class ProductCustomerTableMapper implements TableMapper<Product> {
    @Override
    public LinkedList<Object> constructColumns(Product object) {
        LinkedList<Object> list = new LinkedList<>();
        list.add(object.printName());
        list.add(deriveCategory(object.getProductCode()));

        JButton viewButton = new JButton("Add to Cart");
        viewButton.addActionListener(e -> onClick(object));
        list.add(viewButton);

        return list;
    }

    private String deriveCategory(String productCode) {
        // Check if the productCode starts with the letter 'L'
        if (productCode.startsWith("L")) {
            return "Locomotive";
        } else if (productCode.startsWith("C")) {
            return "Controller";
        } else if (productCode.startsWith("R")) {
            return "Track";
        } else if (productCode.startsWith("S")) {
            return "Rolling Stocks";
        } else if (productCode.startsWith("M")) {
            return "Train Sets";
        } else if (productCode.startsWith("P")) {
            return "Train Packs";
        } else {
            return "Other Category";
        }
    }

    public abstract void onClick(Product product);
}
