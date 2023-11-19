package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.domain.managers.OrderManager;
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

    public void changeOrderlineQuantity(OrderLine orderline, int newQty){
        orderline.setQuantity(newQty);
        userBasket.calculateTotalPrice();
    }

    public void deleteOrderline(OrderLine orderline){
        System.out.println("deleting: " + orderline.getProduct().getName());
        OrderManager.deleteOrderline(userBasket, orderline);
        basketView.onRefresh();
    }

    /**
     * confirm the basket order
     * FOR NOW JUST SAVES THE ORDER
     */
    public void confirmOrder(){
        System.out.println("PRESSED CONFIRM");
        userBasket.PrintFullOrder();
        OrderManager.saveFullOrderState(userBasket);
    }

}
