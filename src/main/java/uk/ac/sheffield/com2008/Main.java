package uk.ac.sheffield.com2008;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.entities.User;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        DatabaseConnectionHandler.connectToDatabase();

        Scanner scanner = new Scanner(System.in);
        int userId = scanner.nextInt();

        UserDAO dao = new UserDAO();
        User user = dao.getUserById(userId);
        System.out.println(user.getPersonalDetails().getForename());
        System.out.println(user.getPersonalDetails().getSurname());
        System.out.println(user.getPersonalDetails().getBankingCard().getHolderName());

        String userEmail = scanner.next();
        User user2 = UserDAO.getUserByEmail(userEmail);
        System.out.println(user2.getPersonalDetails().getForename());
        System.out.println(user2.getPersonalDetails().getSurname());
        System.out.println(user2.getPersonalDetails().getBankingCard().getHolderName());
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