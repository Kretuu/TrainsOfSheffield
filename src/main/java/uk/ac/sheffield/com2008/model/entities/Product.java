package uk.ac.sheffield.com2008.model.entities;

public class Product {
    public enum Gauge {OO, TT, N};
    private String productCode;
    private String name;
    private float price;
    private Gauge gauge;
    private String brand;
    private boolean isSet;
    private int stock;

    public Product(String productCode, String name, float price, Gauge gauge, String brand, boolean isSet, int stock){
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.gauge = gauge;
        this.brand = brand;
        this.isSet = isSet;
        this.stock = stock;
    }

    @Override
    public String toString(){
        return productCode + " '" + name + "' " + gauge.toString() + " Gauge - " + brand + " " + price + " QTY: " + stock;
    }

    public String printName(){
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
}
