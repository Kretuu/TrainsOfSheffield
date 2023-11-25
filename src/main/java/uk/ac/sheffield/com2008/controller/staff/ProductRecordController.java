package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ProductRecordView;

import java.util.ArrayList;
import java.util.List;

public class ProductRecordController extends ViewController {
    private final ProductRecordView productRecordView;

    private List<Product> allProducts;

    public ProductRecordController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ProductRecordView(this);
        productRecordView = (ProductRecordView) view;
    }

    public void onNavigateTo(){
        allProducts = ProductDAO.getAllProducts();
        productRecordView.onRefresh();
    }

    public List<Product> getAllProducts(){
        if(allProducts != null){
            return allProducts;
        }
        return new ArrayList<>();
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
