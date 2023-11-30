package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.domain.managers.OrderManager;
import uk.ac.sheffield.com2008.model.domain.managers.ProductManager;
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

    public BrowseItemsController(NavigationManager navigationManager, Navigation id) {
        //initialise view link
        super(navigationManager, id);
        view = new BrowseItemsView(this);
        browseItemsView = (BrowseItemsView) view;

    }

    public void onNavigateTo() {
        List<Product> allProducts;
        try {
            allProducts = ProductManager.getAllProducts();
        } catch (SQLException e) {
            allProducts = new ArrayList<>();
            navigation.setLayoutMessage(
                    "Browse Items Error", "Cannot load list of products from database", true);
        }
        browseItemsView.refreshTable(allProducts);
    }

    /**
     * Adds an orderline consisting of a product and quantity to the order.
     * Will update the quantity if this product already exists.
     *
     * @param product
     * @param quantity
     */
    public void addProductToBasket(Product product, int quantity) {
        Order userBasket = AppSessionCache.getInstance().getUserLoggedIn().getBasket();
        try {
            //if product already exists
            if (!userBasket.hasProduct(product)) {
                System.out.println("Adding " + product.getProductCode() + " to order");
                OrderManager.addProductToOrder(userBasket, product, quantity);
            } else {
                System.out.println("Modifying " + product.getProductCode() + " quantity in order");
                OrderManager.modifyProductQuantity(userBasket, product, quantity);
            }
            userBasket.PrintFullOrder();
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Browse Items Error",
                    "Could not connect to database. Product was not added to the basket", true);
        }

    }

}
