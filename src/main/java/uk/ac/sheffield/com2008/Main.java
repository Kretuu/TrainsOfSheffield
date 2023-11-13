package uk.ac.sheffield.com2008;

import uk.ac.sheffield.com2008.controller.LoginController;
import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.exceptions.EmailAlreadyInUseException;
import uk.ac.sheffield.com2008.model.domain.AuthenticationManager;
import uk.ac.sheffield.com2008.util.HashedPasswordGenerator;
import uk.ac.sheffield.com2008.view.FrameManager;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        DatabaseConnectionHandler.connectToDatabase();

//        char[] password = "P4$$word".toCharArray();
//        try {
//            new AuthenticationManager()
//                    .registerUser(   "test@test.eu", password, "Jakub", "Kreczetowski");
//        } catch (EmailAlreadyInUseException e) {
//            throw new RuntimeException(e);
//        }

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