package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class StaffView extends View {
    public StaffView(){
        Map<Navigation, JButton> navigationMap = new LinkedHashMap<>();

        JButton staffAreaButton = new JButton("Customer Area");
        navigationMap.put(Navigation.CUSTOMER, staffAreaButton);

        setNavigation(navigationMap);
    }
}
