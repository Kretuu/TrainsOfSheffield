package uk.ac.sheffield.com2008;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.util.ScheduledExecutorManager;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectionHandler.connectToDatabase();
        new NavigationManager();
        ScheduledExecutorManager.startSessionUpdater();
        onDisable();
    }

    private static void onDisable() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nApp Shutdown");

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Shutdown-thread"));
    }
}