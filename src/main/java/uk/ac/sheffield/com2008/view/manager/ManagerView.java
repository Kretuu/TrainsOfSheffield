package uk.ac.sheffield.com2008.view.manager;

import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ManagerView extends View {

    @Override
    public void updateNavigation() {
        Map<Navigation, JButton> navigationMap = new LinkedHashMap<>();

        navigationMap.put(Navigation.STAFF, new JButton("Staff Area"));
        navigationMap.put(Navigation.CUSTOMER, new JButton("Customer Area"));

        setNavigation(navigationMap);
    }
}
