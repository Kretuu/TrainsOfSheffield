package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.exceptions.*;
import uk.ac.sheffield.com2008.model.dao.OrderDAO;
import uk.ac.sheffield.com2008.model.domain.managers.OrderManager;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.modals.OrderLineModal;
import uk.ac.sheffield.com2008.view.staff.ManageOrdersView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ManageOrderController extends ViewController {

    public ManageOrdersView manageOrdersView;

    private List<Order> allOrders = new ArrayList<>();

    public ManageOrderController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ManageOrdersView(this);
        manageOrdersView = (ManageOrdersView) view;
    }

    public void onNavigateTo(){
        try {
            allOrders = OrderDAO.getAllOrders();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        manageOrdersView.populateOrdersInTable();
    }

    public void deleteOrder (Order order){
        // Delete the order and the orderlines associated with the order number in the database
        try {
            OrderDAO.deleteOrder(order);
        } catch (SQLException e) {
            navigation.setLayoutMessage(
                    "Manage Orders Error",
                    "Could not connect to database. Order was not deleted.", true);
        }
    }

    public List<Order> getAllOrders() {
        return allOrders;
    }

    public void fulfillOrder(Order order, OrderLineModal modal) {
        String errorMessage = null;
        try {
            OrderManager.fulfilOrder(order);
            manageOrdersView.populateOrdersInTable();
            JOptionPane.showMessageDialog(modal, "Order has been fulfilled");
            modal.dispose();
        } catch (InvalidOrderStateException | OrderQuantitiesInvalidException | BankDetailsNotValidException |
                 OrderHasNoOwnerException e) {
            errorMessage = e.getMessage();
        } catch (SQLException e) {
            errorMessage = "Cannot connect to database.";
        } catch (OrderOutdatedException e) {
            onNavigateTo();
            JOptionPane.showMessageDialog(modal, "Error: " + e.getMessage());
            modal.dispose();
        }
        if(errorMessage != null) {
            JOptionPane.showMessageDialog(modal, "Error: " + errorMessage);
            modal.unselectFulfilledCheckbox();
        }
    }

}
