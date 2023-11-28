package uk.ac.sheffield.com2008.view.staff;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class StaffView extends View {


    @Override
    public void updateNavigation() {
        Map<Navigation, JButton> navigationMap = new LinkedHashMap<>();

        User user = AppSessionCache.getInstance().getUserLoggedIn();

        if(user != null){
            if(user.getRoles().contains(User.Role.STAFF)){
                navigationMap.put(Navigation.STAFF, new JButton("Home"));
                navigationMap.put(Navigation.PRODUCT_RECORD, new JButton("Product Record"));
                navigationMap.put(Navigation.MANAGE_ORDER, new JButton("Manage Orders"));
                navigationMap.put(Navigation.FULFILLED_ORDERS, new JButton("Fulfilled Orders"));
                navigationMap.put(Navigation.SALES, new JButton("Sales"));}

            else if (user.getRoles().contains(User.Role.MANAGER)) {
                navigationMap.put(Navigation.MANAGER, new JButton("Manager Area"));
            }

        }
        navigationMap.put(Navigation.CUSTOMER, new JButton("Customer Area"));


        setNavigation(navigationMap);
    }
}
