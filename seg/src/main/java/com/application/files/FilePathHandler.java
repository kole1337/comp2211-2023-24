package com.application.files;

public class FilePathHandler {
    private String csvPath;
    private String impressionPath;
    private String serverPath;

    public void setCsvPath(String csvPath) {
        this.csvPath = csvPath;
    }

    public void setImpressionPath(String impressionPath) {
        this.impressionPath = impressionPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getCsvPath() {
        return csvPath;
    }

    public String getImpressionPath() {
        return impressionPath;
    }

    public String getServerPath() {
        return serverPath;
    }
}

