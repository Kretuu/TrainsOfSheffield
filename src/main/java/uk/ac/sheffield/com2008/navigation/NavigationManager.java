package uk.ac.sheffield.com2008.navigation;

import uk.ac.sheffield.com2008.controller.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class NavigationManager {
    private final JFrame frame;
    private final Map<Navigation, ViewController> controllers;

    public NavigationManager() {
        frame = new JFrame("Trains of Sheffield");
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
    }

    public void registerController(Navigation id, ViewController controller) {
        controllers.put(id, controller);
    }

    public void navigate(Navigation id) {
        ViewController controller = controllers.get(id);
//        frame.add(controller.getView());
        frame.setContentPane(controller.getView());
        frame.revalidate();
        frame.repaint();

    }
}
