package uk.ac.sheffield.com2008;

import uk.ac.sheffield.com2008.controller.LoginController;
import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.view.FrameManager;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        DatabaseConnectionHandler.connectToDatabase();

        JFrame frame = FrameManager.getMainFrame();
        LoginController loginController = new LoginController();
    }

    private static void onDisable() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nApp Shutdown");

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }, "Shutdown-thread"));
    }

}