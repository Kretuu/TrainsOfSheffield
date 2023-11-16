package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.customer.BrowseItemsView;

import java.util.ArrayList;

public class BrowseItemsController extends ViewController {

    public BrowseItemsView browseItemsView;
    private ArrayList<Product> allProducts;
    public BrowseItemsController(NavigationManager navigationManager, Navigation id){
        //initialise view link
        super(navigationManager, id);
        view = new BrowseItemsView(this);
        browseItemsView = (BrowseItemsView) view;

    }

    public void onNavigateTo(){
        System.out.println("navigated to BrowseItems");
        allProducts = ProductDAO.getAllProducts();
        browseItemsView.onRefresh();
    }

    public ArrayList<Product> getAllProducts(){
        if(allProducts != null){
            return allProducts;
        }
        return new ArrayList<>();
    }
}
