package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.ManageStockView;

/**
 * Controller responsible for Staff Screens
 */
public class StaffController extends ViewController {
    private final ManageStockView manageStockView;
    public StaffController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new ManageStockView(this);
        manageStockView = (ManageStockView) view;
//        setFrameContent(manageStockView);
    }


}
