package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.domain.managers.OrderManager;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.customer.BrowseItemsView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrowseItemsController extends ViewController {

    public BrowseItemsView browseItemsView;
    private List<Product> allProducts;
    public BrowseItemsController(NavigationManager navigationManager, Navigation id){
        //initialise view link
        super(navigationManager, id);
        view = new BrowseItemsView(this);
        browseItemsView = (BrowseItemsView) view;

    }

    public void onNavigateTo(){
        try {
            allProducts = ProductDAO.getAllProducts();
        } catch (SQLException e) {
            allProducts = new ArrayList<>();
            //TODO Display error message
            System.out.println("Cannot load list of products from database");
        }
        browseItemsView.onRefresh();
    }

    public List<Product> getAllProducts(){
        if(allProducts != null){
            return allProducts;
        }
        return new ArrayList<>();
    }

    /**
     * Adds an orderline consisting of a product and quantity to the order.
     * Will update the quantity if this product already exists.
     * @param product
     * @param quantity
     */
    public void addProductToBasket(Product product, int quantity){
        Order userBasket = AppSessionCache.getInstance().getUserLoggedIn().getBasket();
        try {
            //if product already exists
            if(!userBasket.hasProduct(product)){
                System.out.println("Adding " + product.getProductCode() + " to order");

//            OrderLine newOrderLine = new OrderLine()
                OrderManager.addProductToOrder(userBasket, product, quantity);
            }else{
                System.out.println("Modifying " + product.getProductCode() + " quantity in order");
                OrderManager.modifyProductQuantity(userBasket, product, quantity);
            }
            userBasket.PrintFullOrder();
        } catch (SQLException e) {
            //TODO Create error message
            System.out.println("Cannot connect to database");
        }

    }

}
