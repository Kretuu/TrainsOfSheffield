package uk.ac.sheffield.com2008.controller.staff;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.staff.ManageStockView;

/**
 * Controller responsible for Staff Screens
 */
public class StaffController extends ViewController {
    private final ManageStockView manageStockView;
    public StaffController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ManageStockView(this);
        manageStockView = (ManageStockView) view;
    }

}
