package uk.ac.sheffield.com2008.controller;

import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.View;

public abstract class ViewController {
    protected final NavigationManager navigation;
    protected View view;

    public ViewController(NavigationManager navigationManager, Navigation id) {
        this.navigation = navigationManager;
        navigation.registerController(id, this);
    }

    public View getView() {
        return view;
    }

    public NavigationManager getNavigation() {
        return navigation;
    }

    /**
     * This function is called whenever a controller is navigated to
     */
    public void onNavigateTo() {
    }

    public void onNavigateLeave() {
    }
}
