package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.view.BrowseItemsView;

import java.util.ArrayList;

public class BrowseItemsController extends ViewController{

    public BrowseItemsView browseItemsView;
    private ArrayList<Product> allProducts;
    public BrowseItemsController(){
        allProducts = ProductDAO.getAllProducts();
        browseItemsView = new BrowseItemsView(this);
        setFrameContent(browseItemsView);

        System.out.println(AppSessionCache.getInstance().getUserLoggedIn().getBasket().toString());
    }
    public ArrayList<Product> getAllProducts(){
        return allProducts;
    }


}
