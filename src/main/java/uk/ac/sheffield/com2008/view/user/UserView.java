package uk.ac.sheffield.com2008.view.user;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class UserView extends View {
    @Override
    public void updateNavigation() {
        Map<Navigation, JButton> navigationMap = new LinkedHashMap<>();

        User user = AppSessionCache.getInstance().getUserLoggedIn();
        if(user != null){
            if(user.getRoles().contains(User.Role.STAFF))
                navigationMap.put(Navigation.STAFF, new JButton("Staff Area"));

            if(user.getRoles().contains(User.Role.CUSTOMER))
                navigationMap.put(Navigation.ORDER_HISTORY, new JButton("Order History"));
        }

        navigationMap.put(Navigation.CUSTOMER, new JButton("Browse Items"));
        navigationMap.put(Navigation.BASKET, new JButton("Basket"));
        navigationMap.put(Navigation.MANAGE_PROFILE, new JButton("Manage Profile"));

        setNavigation(navigationMap);
    }
}