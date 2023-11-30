package uk.ac.sheffield.com2008.navigation;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.controller.auth.LoginController;
import uk.ac.sheffield.com2008.controller.auth.ProvideAddressController;
import uk.ac.sheffield.com2008.controller.auth.SignupController;
import uk.ac.sheffield.com2008.controller.customer.BasketViewController;
import uk.ac.sheffield.com2008.controller.customer.BrowseItemsController;
import uk.ac.sheffield.com2008.controller.customer.ManageProfileController;
import uk.ac.sheffield.com2008.controller.customer.OrderHistoryController;
import uk.ac.sheffield.com2008.controller.manager.ManageUserRolesController;
import uk.ac.sheffield.com2008.controller.staff.*;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.util.listeners.NavigationFrameWindowListener;
import uk.ac.sheffield.com2008.view.View;
import uk.ac.sheffield.com2008.view.auth.AuthView;
import uk.ac.sheffield.com2008.view.components.MainLayout;
import uk.ac.sheffield.com2008.view.customer.CustomerView;
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
        frame.setBackground(Colors.BACKGROUND);

        controllers = new HashMap<>();
        registerControllers();
        //First screen after the app starts
        navigate(currentView);
    }

    /**
     * Before letting user go to given view this function should be called to validate if user has appropriate
     * role to do it
     *
     * @param view View which user wants to navigate to
     * @return true if user can access the view or false otherwise
     */
    public static boolean permissionsValid(View view) {
        if (view instanceof AuthView) return true;
        User user = AppSessionCache.getInstance().getUserLoggedIn();
        if (user == null) return false;

        List<User.Role> userRoles = user.getRoles();
        if (view instanceof StaffView && !userRoles.contains(User.Role.STAFF)) return false;
        if (view instanceof ManagerView && !userRoles.contains(User.Role.MANAGER)) return false;
        if (view instanceof CustomerView && !userRoles.contains(User.Role.CUSTOMER)) return false;
        return true;
    }

    public JFrame getFrame() {
        return frame;
    }

    private void registerControllers() {
        //All controllers are registered
        new LoginController(this, Navigation.LOGIN);
        new SignupController(this, Navigation.SIGNUP);
        new BrowseItemsController(this, Navigation.CUSTOMER);
        new StaffController(this, Navigation.STAFF);
        new BasketViewController(this, Navigation.BASKET);
        new ManageOrderController(this, Navigation.MANAGE_ORDER);
        new OrderHistoryController(this, Navigation.ORDER_HISTORY);
        new ProductRecordController(this, Navigation.PRODUCT_RECORD);
        new EditFormController(this, Navigation.EDIT_PRODUCT_RECORD);
        new FormController(this, Navigation.PRODUCTFORM);
        new FulfilledOrdersController(this, Navigation.FULFILLED_ORDERS);
        new SalesController(this, Navigation.SALES);
        new ManageProfileController(this, Navigation.MANAGE_PROFILE);
        new ProvideAddressController(this, Navigation.PROVIDE_ADDRESS);
        new ManageUserRolesController(this, Navigation.MANAGER);
    }

    public void registerController(Navigation id, ViewController controller) {
        controllers.put(id, controller);
    }

    public void navigate(Navigation id) {
        controllers.get(currentView).onNavigateLeave();

        ViewController newController = controllers.get(id);
        View view = newController.getView();
        newController.onNavigateTo();
        //Check if View is Auth view. If it isn't, put the view into layout
        if (view instanceof AuthView) {
            frame.setContentPane(view);
            layout.purgeContent();
        } else if (permissionsValid(view)) {
            if (!frame.getContentPane().equals(layout)) frame.setContentPane(layout);
            layout.setContent(view);
        }
        currentView = id;
        frame.revalidate();
        frame.repaint();
    }

    public void refreshNavigation() {
        layout.setNavigationBar(getCurrentController().getView());
    }

    public ViewController getCurrentController() {
        return controllers.get(currentView);
    }

    public void setLayoutMessage(String header, String message, boolean isError) {
        layout.updateMessage(header, message, isError);
    }
}
