package uk.ac.sheffield.com2008.view.components.customTable.mappers;

import java.util.LinkedList;

public interface TableMapper<Model> {
    LinkedList<Object> constructColumns(Model object);
}
