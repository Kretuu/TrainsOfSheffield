package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CustomerView extends View {
    public CustomerView(){
        Map<Navigation, JButton> navigationMap = new LinkedHashMap<>();

        navigationMap.put(Navigation.STAFF, new JButton("Staff Area"));
        navigationMap.put(Navigation.ORDER_HISTORY, new JButton("Order History"));

        JButton browseButton = new JButton("Browse Items");
        navigationMap.put(Navigation.CUSTOMER, browseButton);

        JButton basketButton = new JButton("Basket");
        navigationMap.put(Navigation.BASKET, basketButton);

        setNavigation(navigationMap);
    }
}
