package uk.ac.sheffield.com2008.model.entities;

public abstract class Product {
    private String productCode;
    private String name;
    private float price;
    private Gauge gauge;
    private String brand;
    private boolean isSet;
    private int stock;
    public Product(String productCode, String name, float price, Gauge gauge, String brand, boolean isSet, int stock) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.gauge = gauge;
        this.brand = brand;
        this.isSet = isSet;
        this.stock = stock;
    }

    public Product(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return productCode + " '" + name + "' " + gauge.toString() + " Gauge - " + brand + " " + price + " QTY: " + stock;
    }

    /**
     * Returns the products presentable name, not how the name is in the database
     *
     * @return
     */
    public String printName() {
        return productCode + " '" + name + "' " + gauge.toString() + " Gauge - " + brand;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Gauge getGauge() {
        return gauge;
    }

    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Product object)) return false;
        return this.name.equals(object.getName())
                && this.isSet == object.isSet()
                && this.brand.equals(object.getBrand())
                && this.gauge.equals(object.getGauge())
                && this.price == object.getPrice()
                && this.productCode.equals(object.getProductCode());
    }

    public abstract String deriveName();

    public enum Gauge {OO, TT, N}
}