package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ManageStockView;

import javax.swing.table.DefaultTableModel;
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

    public void onNavigateTo(){
        allProducts = ProductDAO.getAllProducts();
        manageStockView.onRefresh();
    }

    public List<Product> getAllProducts(){
        if(allProducts != null){
            return allProducts;
        }
        return new ArrayList<>();
    }

    public void updateProductQuantity (Product product, int quantity){
        product.setStock(quantity);
        // Update the product in the database
        ProductDAO.updateProductStocks(product, quantity);
    }

    // Method to repopulate the table with updated data
    public List<Product> repopulateTable() {
        DefaultTableModel tableModel = manageStockView.getTableModel();

        if (tableModel != null) {
            List<Product> updatedProducts = ProductDAO.getAllProducts(); // Fetch updated data from the database

            // Clear existing rows in the table model
            tableModel.setRowCount(0);

            // Update the table with the fetched data
            for (Product product : updatedProducts) {
                Object[] rowData = {product.getProductCode(), product.getName(), determineCustomCategory(product.getProductCode()), product.getStock(), "Edit"};
                tableModel.addRow(rowData);
            }
            return updatedProducts; // Return the updated product list if needed
        } else {
            // Handle the case where tableModel is null
            // Possibly throw an exception or log an error
            return new ArrayList<>(); // Return an empty list or handle as needed
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
