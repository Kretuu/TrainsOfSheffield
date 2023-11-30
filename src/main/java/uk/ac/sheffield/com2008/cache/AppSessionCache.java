package uk.ac.sheffield.com2008.cache;

import uk.ac.sheffield.com2008.model.entities.User;

public class AppSessionCache {
    private static AppSessionCache appSessionCache;
    private User userLoggedIn;

    private AppSessionCache() {
    }

    public static AppSessionCache getInstance() {
        if (appSessionCache == null) {
            appSessionCache = new AppSessionCache();
        }
        return appSessionCache;
    }

    public User getUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(User userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public void logoutUser() {
        this.userLoggedIn = null;
    }
}
