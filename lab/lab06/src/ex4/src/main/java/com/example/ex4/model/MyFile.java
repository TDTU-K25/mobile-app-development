package com.example.ex4.model;

public class MyFile {
    private String fileName;
    private boolean isChecked;

    private String type;
    private String path;

    public MyFile() {
    }

    public MyFile(String fileName, String type, String path) {
        this.fileName = fileName;
        this.isChecked = false;
        this.type = type;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
