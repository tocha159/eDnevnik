package com.examples.your.ednevnik.Model;

/**
 * Created by PINJUH on 28.5.2017..
 */

public class StudentAPI {
    int id;
    String username;
    String avatarURL;

    public StudentAPI() {
    }

    public StudentAPI(int id, String username, String avatarURL) {
        this.id = id;
        this.username = username;
        this.avatarURL = avatarURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
}
