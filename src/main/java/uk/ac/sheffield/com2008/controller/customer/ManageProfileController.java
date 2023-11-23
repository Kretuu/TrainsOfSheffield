package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.customer.ManageProfileView;

public class ManageProfileController extends ViewController {
    public ManageProfileController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new ManageProfileView(this);
    }

}
