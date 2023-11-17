package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CustomerView extends View {
    public CustomerView(){
        Map<Navigation, JButton> navigationMap = new LinkedHashMap<>();

        JButton staffAreaButton = new JButton("Staff Area");
        navigationMap.put(Navigation.STAFF, staffAreaButton);

        setNavigation(navigationMap);
    }
}
