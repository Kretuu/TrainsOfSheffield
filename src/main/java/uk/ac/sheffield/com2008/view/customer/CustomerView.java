package uk.ac.sheffield.com2008.view.customer;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CustomerView extends View {
    @Override
    public void updateNavigation() {
        Map<Navigation, JButton> navigationMap = new LinkedHashMap<>();

        User user = AppSessionCache.getInstance().getUserLoggedIn();
        if(user != null && user.getRoles().contains(User.Role.STAFF))
            navigationMap.put(Navigation.STAFF, new JButton("Staff Area"));

        navigationMap.put(Navigation.ORDER_HISTORY, new JButton("Order History"));
        navigationMap.put(Navigation.CUSTOMER, new JButton("Browse Items"));
        navigationMap.put(Navigation.BASKET, new JButton("Basket"));

        setNavigation(navigationMap);
    }
}
