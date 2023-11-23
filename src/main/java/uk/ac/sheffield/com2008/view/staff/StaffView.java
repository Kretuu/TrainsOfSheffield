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
        if(user != null && user.getRoles().contains(User.Role.MANAGER))
            navigationMap.put(Navigation.MANAGER, new JButton("Manager Area"));
        navigationMap.put(Navigation.CUSTOMER, new JButton("Customer Area"));

        setNavigation(navigationMap);
    }
}
