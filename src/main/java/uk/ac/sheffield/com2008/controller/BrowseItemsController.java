package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.view.BrowseItemsView;

public class BrowseItemsController extends ViewController{

    public BrowseItemsView browseItemsView;
    public BrowseItemsController(){
        browseItemsView = new BrowseItemsView(this);
        setFrameContent(browseItemsView);

        System.out.println(AppSessionCache.getInstance().getUserLoggedIn().getBasket().toString());
    }


}
