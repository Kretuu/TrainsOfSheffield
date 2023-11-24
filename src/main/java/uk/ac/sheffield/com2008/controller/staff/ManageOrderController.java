package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ManageOrdersView;

import java.util.List;


public class ManageOrderController extends ViewController {

    private List<OrderLine> allOrderLine;
    public ManageOrdersView manageOrdersView;

    public ManageOrderController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ManageOrdersView(this);
        manageOrdersView = (ManageOrdersView) view;
    }

}
