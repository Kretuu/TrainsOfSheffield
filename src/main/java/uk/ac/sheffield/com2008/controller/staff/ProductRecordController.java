package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ProductRecordView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRecordController extends ViewController {
    private final ProductRecordView productRecordView;

    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();

    public ProductRecordController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ProductRecordView(this);
        productRecordView = (ProductRecordView) view;
        onNavigateTo();
        setCurrentFilter("All");
    }

    public void onNavigateTo(){
        try {
            allProducts = ProductDAO.getAllProducts();
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Product Record Error",
                    "Could not connect to database. Latest products list was not fetched", true);
        }
        productRecordView.populateTable(filteredProducts);
//        productRecordView.resetFilterState();
    }

    public void setCurrentFilter(String initialLetter) {
        if(initialLetter.isEmpty() || initialLetter.equals("All")) {
            filteredProducts = allProducts;
        } else {
            try {
                filteredProducts = ProductDAO.getProductsByCategory(initialLetter);
            } catch (SQLException e) {
                navigation.setLayoutMessage(
                        "Product Record Error",
                        "Cannot connect to database. Product list was not filtered.", true);
            }
        }
        productRecordView.populateTable(filteredProducts);
    }

    public List<Product> getDisplayedProducts() {
        return filteredProducts;
    }

    public String determineCustomCategory(String productCode) {
        // Check if the productCode starts with the letter 'L'
        if (productCode.startsWith("L")) {
            return "Locomotive";
        } else if (productCode.startsWith("C")) {
            return "Controller";
        } else if (productCode.startsWith("R")) {
            return "Track";
        } else if (productCode.startsWith("S")) {
            return "Rolling Stock";
        } else if (productCode.startsWith("M")) {
            return "Train Set";
        } else if (productCode.startsWith("P")) {
            return "Track Pack";
        } else {
            return "Other Category";
        }
    }
}
