package uk.ac.sheffield.com2008.cache;

public class AppSessionCache {
    private static AppSessionCache appSessionCache;

    private AppSessionCache() {}

    public static AppSessionCache getInstance() {
        if(appSessionCache == null) {
            appSessionCache = new AppSessionCache();
        }
        return appSessionCache;
    }
}
