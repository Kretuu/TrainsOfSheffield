package uk.ac.sheffield.com2008.view.components.customTable.config;

public record CustomColumn(double weight, String columnName) {
    public CustomColumn(double weight, String columnName) {
        this.weight = weight;
        this.columnName = columnName == null ? " " : columnName;
    }
}
