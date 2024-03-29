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
import java.util.stream.Collectors;

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

    /**
     * Retrieves the user's order history, filters out pending orders, sorts them in descending
     * order based on the date ordered, and populates the order history view.
     *
     * get the user's orders from the database, filters out any orders with a status of PENDING, and then sorts the
     * remaining orders based on the date they were ordered in descending order.
     * Finally, it populates the order history view with the sorted list of orders.
     *
     */
    private void updateOrderHistory() {
        User currentUser = AppSessionCache.getInstance().getUserLoggedIn();
        try {
            List<Order> orders = OrderDAO.getUserOrdersHistory(currentUser)
                    .stream().filter(order -> order.getStatus() != Order.Status.PENDING)
                    .sorted((o1, o2) -> o2.getDateOrdered().compareTo(o1.getDateOrdered()))
                    .collect(Collectors.toList());
            orderHistoryView.populateList(orders);
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Order History Error",
                    "Could not connect to database. Latest order history was not loaded.", true);
        }
    }
}
