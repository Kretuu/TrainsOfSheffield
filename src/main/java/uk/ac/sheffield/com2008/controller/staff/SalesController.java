package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.SalesView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for Sales screen
 */
public class SalesController extends ViewController {

    private final SalesView salesView;
    private List<Order> orders = new ArrayList<>();

    public SalesController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new SalesView(this);
        salesView = (SalesView) view;
    }

    @Override
    public void onNavigateTo() {
        try {
            orders = OrderDAO.getFulfilledOrders();
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Sales Error",
                    "Could not connect to database. Data was not loaded.", true);
        }
        salesView.onRefresh();
    }

    public float getTotalSales() {
        float sum = 0;
        for (Order order : orders) {
            sum += order.getTotalPrice();
        }
        return sum;
    }

    public int getNumberOfSales() {
        return orders.size();
    }


}
