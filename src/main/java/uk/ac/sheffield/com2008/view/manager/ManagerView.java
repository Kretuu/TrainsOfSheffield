package uk.ac.sheffield.com2008.view.manager;

import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;
import uk.ac.sheffield.com2008.view.components.Button;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ManagerView extends View {

    @Override
    public void updateNavigation() {
        Map<Navigation, JButton> navigationMap = new LinkedHashMap<>();

        navigationMap.put(Navigation.STAFF, new Button("Staff Area"));
        navigationMap.put(Navigation.CUSTOMER, new Button("Customer Area"));

        setNavigation(navigationMap);
    }
}
