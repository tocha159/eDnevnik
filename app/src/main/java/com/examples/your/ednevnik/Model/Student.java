package com.examples.your.ednevnik.Model;

import com.orm.SugarRecord;

/**
 * Created by PINJUH on 28.5.2017..
 */

public class Student extends SugarRecord {
    String name;
    String surname;
    String picture;
    String username;
    String password;

    public Student() {
    }

    public Student(String name, String surname, String picture, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.picture = picture;
        this.username = username;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
