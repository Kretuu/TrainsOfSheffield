package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.OrderListView;


public class OrderListController extends ViewController {
    public OrderListView orderListView;

    public OrderListController(NavigationManager navigationManager, Navigation id){
        //initialise view link
        super(navigationManager, id);
        view = new OrderListView(this);
        orderListView = (OrderListView) view;
    }

}
