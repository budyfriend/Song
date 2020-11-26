package com.budyfriend_code.song.model;

public class dataLogin {
    private String key;
    private String username;
    private String password;
    private String level;

    public dataLogin() {
    }

    public dataLogin(String username, String password, String level) {
        this.username = username;
        this.password = password;
        this.level = level;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLevel() {
        return level;
    }
}
