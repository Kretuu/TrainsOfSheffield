package uk.ac.sheffield.com2008.view.components.customTable.mappers;

import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.util.ProductCategoryHelper;
import uk.ac.sheffield.com2008.view.components.Button;

import javax.swing.*;
import java.util.LinkedList;


public abstract class ProductTableMapper implements TableMapper<Product> {
    private final String actionName;
    public ProductTableMapper(String actionName) {
        this.actionName = actionName;
    }
    @Override
    public LinkedList<Object> constructColumns(Product object) {
        LinkedList<Object> list = new LinkedList<>();
        list.add(object.printName());
        list.add(ProductCategoryHelper.deriveCategory(object.getProductCode()));
        list.add(object.getStock());

        JButton editButton = new Button(actionName);
        editButton.addActionListener(e -> onClick(object));
        list.add(editButton);
        return list;
    }

    public abstract void onClick(Product product);
}
