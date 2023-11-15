package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.BrowseItemsView;
import uk.ac.sheffield.com2008.cache.AppSessionCache;

public class BrowseItemsController extends ViewController {
    public BrowseItemsController(NavigationManager navigationManager, Navigation id){
        super(navigationManager, id);
        view = new BrowseItemsView(this);
//        System.out.println(AppSessionCache.getInstance().getUserLoggedIn().getBasket().toString());
    }


}
