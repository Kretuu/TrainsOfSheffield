package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;
import uk.ac.sheffield.com2008.view.components.Button;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class StaffView extends View {


    @Override
    public void updateNavigation() {
        Map<Navigation, JButton> navigationMap = new LinkedHashMap<>();

        User user = AppSessionCache.getInstance().getUserLoggedIn();

        if (user != null) {
            if (user.getRoles().contains(User.Role.STAFF)) {
                navigationMap.put(Navigation.STAFF, new Button("Home"));
                navigationMap.put(Navigation.PRODUCT_RECORD, new Button("Product Record"));
                navigationMap.put(Navigation.MANAGE_ORDER, new Button("Manage Orders"));
                navigationMap.put(Navigation.FULFILLED_ORDERS, new Button("Fulfilled Orders"));
                navigationMap.put(Navigation.SALES, new Button("Sales"));
            }

            if (user.getRoles().contains(User.Role.MANAGER)) {
                navigationMap.put(Navigation.MANAGER, new Button("Manager Area"));
            }

        }
        navigationMap.put(Navigation.CUSTOMER, new Button("Customer Area"));


        setNavigation(navigationMap);
    }
}
