package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.FulfilledOrdersView;
import uk.ac.sheffield.com2008.view.staff.ManageOrdersView;

import java.util.List;

public class FulfilledOrdersController extends ViewController {

    public FulfilledOrdersView fulfilledOrdersView;

    private List<Order> allOrders;

    public FulfilledOrdersController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new FulfilledOrdersView(this);
       fulfilledOrdersView = (FulfilledOrdersView) view;
    }

    public void onNavigateTo(){
        allOrders = OrderDAO.getFulfilledOrders();
        fulfilledOrdersView.onRefresh();
    }
}
