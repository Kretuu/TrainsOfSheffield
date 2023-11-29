package uk.ac.sheffield.com2008.view;

import uk.ac.sheffield.com2008.navigation.Navigation;

import javax.swing.*;
import java.util.*;

public abstract class View extends JPanel {
    private Map<Navigation, JButton> navigation = new LinkedHashMap<>();

    protected void setNavigation(Map<Navigation, JButton> navigationMap){
        this.navigation = navigationMap;
    }

    public Map<Navigation,JButton> getNavigation() {
        updateNavigation();
        return navigation;
    }

    abstract public void updateNavigation();
}
