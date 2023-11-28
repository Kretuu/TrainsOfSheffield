package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.FulfilledOrdersView;

import java.sql.SQLException;
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
        try {
            allOrders = OrderDAO.getFulfilledOrders();
            fulfilledOrdersView.onRefresh();
        } catch (SQLException e) {
            //TODO Error message
            System.out.println("Could not connect to database. Fulfilled orders were not fetched.");
        }

    }
}
