package uk.ac.sheffield.com2008.model.domain.data;

import uk.ac.sheffield.com2008.model.entities.Product;


public class OrderLine {
    private final Product product;
    private float price;
    private int quantity;


    public OrderLine(float price, int quantity, Product product) {
        this.price = price;
        this.quantity = quantity;
        this.product = product;
    }

    public OrderLine(int quantity, Product product) {
        this(product.getPrice() * quantity, quantity, product);
    }

    public boolean hasProduct(Product product) {
        return this.product.getProductCode().equals(product.getProductCode());
    }

    public Product getProduct() {
        return product;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculatePrice();
    }

    public void calculatePrice(){
        this.price = product.getPrice() * quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof OrderLine ol)) return false;
        return ol.getPrice() == this.price && ol.getQuantity() == this.quantity && ol.getProduct().equals(this.product);
    }

}
