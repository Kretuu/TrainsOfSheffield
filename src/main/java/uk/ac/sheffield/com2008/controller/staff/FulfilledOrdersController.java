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
import java.util.stream.Collectors;

public class FulfilledOrdersController extends ViewController {

    private final FulfilledOrdersView fulfilledOrdersView;

    private List<Order> fulfilledOrders = new ArrayList<>();

    public FulfilledOrdersController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new FulfilledOrdersView(this);
        fulfilledOrdersView = (FulfilledOrdersView) view;
    }

    public void onNavigateTo() {
        try {
            fulfilledOrders = OrderDAO.getFulfilledOrders()
                    .stream().sorted((o1, o2) -> o2.getDateOrdered().compareTo(o1.getDateOrdered()))
                    .collect(Collectors.toList());
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
