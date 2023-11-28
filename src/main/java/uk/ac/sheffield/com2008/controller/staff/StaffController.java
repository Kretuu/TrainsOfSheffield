package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ManageStockView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for Staff Screens
 */
public class StaffController extends ViewController {
    private final ManageStockView manageStockView;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();

    public StaffController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new ManageStockView(this);
        manageStockView = (ManageStockView) view;
        onNavigateTo();
        setCurrentFilter("All");
    }

    public void onNavigateTo() {
        try {
            allProducts = ProductDAO.getAllProducts();
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Product Record Error",
                    "Cannot connect to database. Product list was not updated", true);
        }
        manageStockView.populateTable(filteredProducts);
    }

    public List<Product> getCurrentDisplayedProducts() {
        return filteredProducts;
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
        manageStockView.populateTable(filteredProducts);
    }

    public void updateProductQuantity(Product product, int quantity) {
        product.setStock(quantity);
        // Update the product in the database
        try {
            ProductDAO.updateProductStocks(product, quantity);
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Product Record Error",
                    "Could not connect to database. Product quantity was not updated.", true);
        }
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
            return "Rolling Stocks";
        } else if (productCode.startsWith("M")) {
            return "Train Sets";
        } else if (productCode.startsWith("P")) {
            return "Train Packs";
        } else {
            return "Other Category";
        }
    }

}
