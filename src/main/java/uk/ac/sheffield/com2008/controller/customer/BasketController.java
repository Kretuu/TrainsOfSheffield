package uk.ac.sheffield.com2008.controller.customer;

import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.customer.BasketView;

public class BasketController extends ViewController {

    BasketView basketView;
    public BasketController(NavigationManager navigationManager, Navigation id) {
        super(navigationManager, id);
        view = new BasketView(this);
        basketView = (BasketView) view;
    }

    public void onNavigateTo(){
        basketView.onRefresh();
    }
}
