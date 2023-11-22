package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ManageOrdersView;


public class ManageOrderController extends ViewController {

    private final ManageOrdersView manageOrdersView;

    public ManageOrderController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ManageOrdersView(this);
        manageOrdersView = (ManageOrdersView) view;
    }

}
