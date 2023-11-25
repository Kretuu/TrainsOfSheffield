package uk.ac.sheffield.com2008.navigation;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.controller.auth.*;
import uk.ac.sheffield.com2008.controller.customer.*;
import uk.ac.sheffield.com2008.controller.staff.*;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.util.listeners.NavigationFrameWindowListener;
import uk.ac.sheffield.com2008.view.components.MainLayout;
import uk.ac.sheffield.com2008.view.View;
import uk.ac.sheffield.com2008.view.auth.AuthView;
import uk.ac.sheffield.com2008.view.manager.ManagerView;
import uk.ac.sheffield.com2008.view.staff.StaffView;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class NavigationManager {
    private final JFrame frame;
    private final MainLayout layout;
    private final Map<Navigation, ViewController> controllers;
    private Navigation currentView = Navigation.LOGIN;

    public NavigationManager() {
        frame = new JFrame("Trains of Sheffield");
        layout = new MainLayout(this);
        // Set the frame to fullscreen windowed mode
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.addWindowListener(new NavigationFrameWindowListener(this));
        frame.setVisible(true);

        controllers = new HashMap<>();
        registerControllers();
        //First screen after the app starts
        navigate(currentView);
    }

    private void registerControllers() {
        //All controllers are registered
        new LoginController(this, Navigation.LOGIN);
        new SignupController(this, Navigation.SIGNUP);
        new BrowseItemsController(this, Navigation.CUSTOMER);
        new StaffController(this, Navigation.STAFF);
        new ProductRecordController(this,Navigation.PRODUCT_RECORD);
        new OrderHistoryController(this, Navigation.ORDER_HISTORY);
        new BasketViewController(this, Navigation.BASKET);
        new ManageOrderController(this,Navigation.MANAGE_ORDER);
        new SalesController(this,Navigation.SALES);
        new FormController(this, Navigation.PRODUCTFORM);

    }

    public void registerController(Navigation id, ViewController controller) {
        controllers.put(id, controller);
    }

    public void navigate(Navigation id) {
        controllers.get(currentView).onNavigateLeave();

        ViewController newController = controllers.get(id);
        View view = newController.getView();
        view.updateNavigation();
        newController.onNavigateTo();
        //Check if View is Auth view. If it isn't, put the view into layout
        if(view instanceof AuthView) {
            frame.setContentPane(view);
            layout.purgeContent();
        } else if(permissionsValid(view)) {
            if(!frame.getContentPane().equals(layout)) frame.setContentPane(layout);
            layout.setContent(view);
        }
        currentView = id;
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Before letting user go to given view this function should be called to validate if user has appropriate
     * role to do it
     * @param view View which user wants to navigate to
     * @return true if user can access the view or false otherwise
     */
    public static boolean permissionsValid(View view) {
        List<User.Role> userRoles = AppSessionCache.getInstance().getUserLoggedIn().getRoles();
        if(view instanceof StaffView && !userRoles.contains(User.Role.STAFF)) return false;
        if(view instanceof ManagerView && !userRoles.contains(User.Role.MANAGER)) return false;
        return true;
    }

    public ViewController getCurrentController() {
        return controllers.get(currentView);
    }
}
