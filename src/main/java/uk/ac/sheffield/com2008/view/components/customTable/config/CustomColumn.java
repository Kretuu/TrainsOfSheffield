package uk.ac.sheffield.com2008.view.components.customTable.config;

public class CustomColumn {
    private final double weight;
    private final String columnName;

    public CustomColumn(double weight, String columnName) {
        this.weight = weight;
        this.columnName = columnName;
    }

    public double getWeight() {
        return weight;
    }

    public String getColumnName() {
        return columnName;
    }
}
