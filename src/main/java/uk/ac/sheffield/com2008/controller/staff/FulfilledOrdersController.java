package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.FulfilledOrdersView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FulfilledOrdersController extends ViewController {

    public FulfilledOrdersView fulfilledOrdersView;

    private List<Order> fulfilledOrders = new ArrayList<>();

    public FulfilledOrdersController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new FulfilledOrdersView(this);
       fulfilledOrdersView = (FulfilledOrdersView) view;
    }

    public void onNavigateTo(){
        try {
            fulfilledOrders = OrderDAO.getFulfilledOrders();
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Fulfilled Orders Error",
                    "Could not connect to database. Fulfilled orders were not fetched.", true);
        }
        fulfilledOrdersView.populateOrdersInTable();
    }

    public List<Order> getFulfilledOrders() {
        return fulfilledOrders;
    }
}
