package uk.ac.sheffield.com2008.view;

import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.components.Panel;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class View extends Panel {
    private Map<Navigation, JButton> navigation = new LinkedHashMap<>();

    public Map<Navigation, JButton> getNavigation() {
        updateNavigation();
        return navigation;
    }

    protected void setNavigation(Map<Navigation, JButton> navigationMap) {
        this.navigation = navigationMap;
    }

    abstract public void updateNavigation();
}
