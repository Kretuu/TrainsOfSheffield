package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.domain.managers.ProductManager;
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
    private String initialLetter = "";
    private List<Product> allProducts = new ArrayList<>();

    public StaffController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new ManageStockView(this);
        manageStockView = (ManageStockView) view;
    }

    public void onNavigateTo() {
        try {
            allProducts = ProductManager.getProductsByCategory(initialLetter);
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Product Record Error",
                    "Cannot connect to database. Product list was not updated", true);
        }
        manageStockView.populateTable(allProducts);
    }

    public void setCurrentFilter(String initialLetter) {
        if(initialLetter.equals("All")) {
            this.initialLetter = "";
        } else {
            this.initialLetter = initialLetter;
        }
        onNavigateTo();
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
