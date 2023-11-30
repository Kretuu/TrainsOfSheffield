package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.ProductNotExistException;
import uk.ac.sheffield.com2008.model.domain.managers.ProductManager;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ProductRecordView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRecordController extends ViewController {
    private final ProductRecordView productRecordView;
    private String filterInitialLetter = "";

    private List<Product> allProducts = new ArrayList<>();

    public ProductRecordController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new ProductRecordView(this);
        productRecordView = (ProductRecordView) view;
    }

    public void onNavigateTo() {
        try {
            allProducts = ProductManager.getProductsByCategory(filterInitialLetter);
        } catch (SQLException e) {
            e.printStackTrace();
            navigation.setLayoutMessage(
                    "Product Record Error",
                    "Could not connect to database. Latest products list was not fetched", true);
        }
        productRecordView.populateTable(allProducts);
    }

    public void setCurrentFilter(String initialLetter) {
        if (initialLetter.equals("All")) {
            this.filterInitialLetter = "";
        } else {
            this.filterInitialLetter = initialLetter;
        }
        onNavigateTo();
    }


    public void deleteProduct(Product product) {
        try {
            ProductManager.deleteProduct(product);
            navigation.setLayoutMessage(
                    "Product Delete",
                    "Product was deleted successfully", false);
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Product Delete Error",
                    "Could not connect to database. The product was not deleted", true);
        } catch (ProductNotExistException e) {
            navigation.setLayoutMessage(
                    "Product Delete Error",
                    e.getMessage(), true);
        }
    }
}
