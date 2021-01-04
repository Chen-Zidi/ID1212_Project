package com.example.demo.model;

public class User {
    public User(String name, String password, String email, int id) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.id = id;
    }

    String name;
    String password;
    String email;
    int id;
}
