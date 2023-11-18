package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.customer.BasketView;
import uk.ac.sheffield.com2008.view.customer.BrowseItemsView;

public class BasketViewController extends ViewController {
    public BasketView basketView;
    private Order userBasket;
    public BasketViewController(NavigationManager navigationManager, Navigation id){
        //initialise view link
        super(navigationManager, id);
        view = new BasketView(this);
        basketView = (BasketView) view;

    }

    public void onNavigateTo(){
        userBasket = AppSessionCache.getInstance().getUserLoggedIn().getBasket();
        userBasket.PrintFullOrder();
        basketView.onRefresh();
    }

    public Order getBasket(){
        return userBasket;
    }

}
