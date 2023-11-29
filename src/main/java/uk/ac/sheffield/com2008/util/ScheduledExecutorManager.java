package uk.ac.sheffield.com2008.util;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.model.dao.UserDAO;
import uk.ac.sheffield.com2008.model.domain.managers.OrderManager;
import uk.ac.sheffield.com2008.model.entities.User;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorManager {
    public static ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);

    public static void startSessionUpdater() {
        exec.scheduleWithFixedDelay(() -> {
            User user = AppSessionCache.getInstance().getUserLoggedIn();
            if(user != null) {
                try {
                    User refreshedUser = UserDAO.getUserByUuid(user.getUuid());
                    if(refreshedUser != null) AppSessionCache.getInstance().setUserLoggedIn(refreshedUser);
                    OrderManager.updateUserBasket(user);
                } catch (SQLException e) {
                    System.out.println("Could not connect to database. User session was not updated");
                }
            }
        }, 1, 2, TimeUnit.MINUTES);
    }
}
