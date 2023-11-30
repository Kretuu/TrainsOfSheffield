package uk.ac.sheffield.com2008.view.components.customTable.mappers;

import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.util.ProductCategoryHelper;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public abstract class ProductRecordTableMapper implements TableMapper<Product> {
    @Override
    public LinkedList<Object> constructColumns(Product object) {
        LinkedList<Object> list = new LinkedList<>();
        list.add(object.printName());
        list.add(ProductCategoryHelper.deriveCategory(object.getProductCode()));
        list.add(object.getStock());

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonsPanel.setBackground(Colors.TABLE_CONTENT);
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> onClick(object));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> onDelete(object));

        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        list.add(buttonsPanel);
        return list;
    }

    public abstract void onClick(Product product);

    public abstract void onDelete(Product product);
}
