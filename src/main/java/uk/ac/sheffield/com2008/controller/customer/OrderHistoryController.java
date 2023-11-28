package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.customer.OrderHistoryView;

import java.sql.SQLException;
import java.util.List;

public class OrderHistoryController extends ViewController {
    private final OrderHistoryView orderHistoryView;

    public OrderHistoryController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new OrderHistoryView(this);
        orderHistoryView = (OrderHistoryView) view;
    }

    @Override
    public void onNavigateTo() {
        updateOrderHistory();
    }

    private void updateOrderHistory() {
        User currentUser = AppSessionCache.getInstance().getUserLoggedIn();
        try {
            List<Order> orders = OrderDAO.getUserOrdersHistory(currentUser);
            orderHistoryView.populateList(orders);
        } catch (SQLException e) {
            //TODO Error message
            System.out.println("Could not connect to database. Latest order history was not loaded.");
        }
    }
}
