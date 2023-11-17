package uk.ac.sheffield.com2008.navigation;

import uk.ac.sheffield.com2008.controller.*;
import uk.ac.sheffield.com2008.controller.auth.LoginController;
import uk.ac.sheffield.com2008.controller.auth.SignupController;
import uk.ac.sheffield.com2008.controller.customer.BrowseItemsController;
import uk.ac.sheffield.com2008.controller.staff.StaffController;
import uk.ac.sheffield.com2008.util.ui.MainLayout;
import uk.ac.sheffield.com2008.view.View;
import uk.ac.sheffield.com2008.view.auth.AuthView;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class NavigationManager {
    private final JFrame frame;
    private final MainLayout layout;
    private final Map<Navigation, ViewController> controllers;

    public NavigationManager() {
        frame = new JFrame("Trains of Sheffield");
        layout = new MainLayout();
        // Set the frame to fullscreen windowed mode
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);

        controllers = new HashMap<>();
        registerControllers();
        //First screen after the app starts
        navigate(Navigation.LOGIN);
    }

    private void registerControllers() {
        //All controllers are registered
        new LoginController(this, Navigation.LOGIN);
        new SignupController(this, Navigation.SIGNUP);
        new BrowseItemsController(this, Navigation.CUSTOMER);
        new StaffController(this, Navigation.STAFF);
        new StaffController(this,Navigation.PRODUCTRECORD);
    }

    public void registerController(Navigation id, ViewController controller) {
        controllers.put(id, controller);
    }

    public void navigate(Navigation id) {
        ViewController controller = controllers.get(id);
        View view = controller.getView();
        controller.onNavigateTo();
        //Check if View is Auth view. If it isn't, put the view into layout
        if(view instanceof AuthView) {
            frame.setContentPane(view);
        } else {
            if(!frame.getContentPane().equals(layout)) frame.setContentPane(layout);
            layout.setContent(view);
        }
        frame.revalidate();
        frame.repaint();

    }
}
