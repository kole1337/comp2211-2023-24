package com.application.files;

public class FilePathHandler {
    private String clickPath;
    private String impressionPath;
    private String serverPath;

    public void setClickPath(String csvPath) {
        this.clickPath = csvPath;
    }

    public void setImpressionPath(String impressionPath) {
        this.impressionPath = impressionPath;
    }

    public void setServerPath(String serverPath) {
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

