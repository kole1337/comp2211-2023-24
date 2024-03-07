package com.application.files;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FilePathHandler {
    private String clickPath;
    private String impressionPath;
    private String serverPath;
    private Logger logger = Logger.getLogger(getClass().getName());


    public void setClickPath(String csvPath) {
        logger.log(Level.INFO, "Setting click_log path.");
        this.clickPath = csvPath;
    }

    public void setImpressionPath(String impressionPath) {
        logger.log(Level.INFO, "Setting impression_log path.");
        this.impressionPath = impressionPath;
    }

    public void setServerPath(String serverPath) {
        logger.log(Level.INFO, "Setting server_log path.");
        this.serverPath = serverPath;
    }

    public String getClickPath() {
        return clickPath;
    }

    public String getImpressionPath() {
        return impressionPath;
    }

    public String getServerPath() {
        return serverPath;
    }
}

