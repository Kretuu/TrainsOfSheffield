package uk.ac.sheffield.com2008.view.components.customTable.mappers;

import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.util.ProductCategoryHelper;
import uk.ac.sheffield.com2008.view.components.Button;

import javax.swing.*;
import java.util.LinkedList;

public abstract class ProductCustomerTableMapper implements TableMapper<Product> {
    @Override
    public LinkedList<Object> constructColumns(Product object) {
        LinkedList<Object> list = new LinkedList<>();
        list.add(object.printName());
        list.add(ProductCategoryHelper.deriveCategory(object.getProductCode()));

        JButton viewButton = new Button("Add to Cart", 25);
        viewButton.addActionListener(e -> onClick(object));
        list.add(viewButton);

        return list;
    }

    public abstract void onClick(Product product);
}
