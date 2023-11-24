package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ManageStockView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for Staff Screens
 */
public class StaffController extends ViewController {
    private final ManageStockView manageStockView;
    private List<Product> allProducts;

    public StaffController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ManageStockView(this);
        manageStockView = (ManageStockView) view;
    }

    public List<Product> getAllProducts(){
        if(allProducts != null){
            return allProducts;
        }
        return new ArrayList<>();
    }

    public void onNavigateTo(){
        allProducts = ProductDAO.getAllProducts();
        manageStockView.onRefresh();
    }

    public void updateProductQuantity (Product product, int quantity){
        product.setStock(quantity);
        // Update the product in the database
        ProductDAO.updateProductStocks(product, quantity);
    }

}
