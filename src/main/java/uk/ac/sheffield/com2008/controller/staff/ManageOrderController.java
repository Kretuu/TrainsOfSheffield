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
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ManageOrderController extends ViewController {

    public ManageOrdersView manageOrdersView;

//    private List<Order> allOrders;

    public ManageOrderController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ManageOrdersView(this);
        manageOrdersView = (ManageOrdersView) view;
    }

    public void onNavigateTo(){
//        allOrders = OrderDAO.getAllOrders();
        manageOrdersView.onRefresh();
        repopulateTable();
    }

//    public void updateOrderStatus (Order order){
//        order.setStatus(Order.Status.FULFILLED);
//        // Update the order status to "FULFILLED" in the database
//        OrderDAO.updateOrderStatus(order);
//    }

    public void deleteOrder (Order order){
        // Delete the order and the orderlines associated with the order number in the database
        OrderDAO.deleteOrder(order);
    }

    public List<Order> repopulateTable() {
        DefaultTableModel tableModel = manageOrdersView.getTableModel();

        if (tableModel != null) {
            // Fetch updated data from the database
            List<Order> updatedOrder = OrderDAO.getAllOrders();

            // Clear existing rows in the table model
            tableModel.setRowCount(0);

            // Update the table with the fetched data
            for (Order order : updatedOrder) {
                Object[] rowData = {order.getOrderNumber(),order.getDateOrdered(), order.getStatus(),order.getTotalPrice(),"View"};
                tableModel.addRow(rowData);
            }
            return updatedOrder; // Return the updated product list if needed
        } else {
            // Handle the case where tableModel is null
            // Possibly throw an exception or log an error
            return new ArrayList<>(); // Return an empty list or handle as needed
        }

    }

    public void fulfillOrder(Order order, OrderLineModal modal) {
        String errorMessage = null;
        try {
            OrderManager.fulfilOrder(order);
            repopulateTable();
            JOptionPane.showMessageDialog(modal, "Order has been fulfilled");
            modal.dispose();
        } catch (InvalidOrderStateException | OrderQuantitiesInvalidException | BankDetailsNotValidException |
                 OrderHasNoOwnerException e) {
            errorMessage = e.getMessage();
        } catch (SQLException e) {
            errorMessage = "Cannot connect to database.";
        } catch (OrderOutdatedException e) {
            repopulateTable();
            JOptionPane.showMessageDialog(modal, "Error: " + e.getMessage());
            modal.dispose();
        }
        if(errorMessage != null) {
            JOptionPane.showMessageDialog(modal, "Error: " + errorMessage);
            modal.unselectFulfilledCheckbox();
        }
    }

}
