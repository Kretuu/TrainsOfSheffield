package uk.ac.sheffield.com2008;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;


public class Main {
    public static void main(String[] args) {
        DatabaseConnectionHandler.connectToDatabase();
        onDisable();
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