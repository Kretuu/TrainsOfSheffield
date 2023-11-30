package uk.ac.sheffield.com2008.model.domain.data;

import uk.ac.sheffield.com2008.model.entities.Product;

public class ProductSetItem {
    private final Product product;
    private int quantity;

    public ProductSetItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public boolean hasProduct(Product product) {
        return this.product.getProductCode().equals(product.getProductCode());
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProductSetItem psi)) return false;
        return psi.getQuantity() == this.quantity && psi.getProduct().equals(this.product);
    }
}
