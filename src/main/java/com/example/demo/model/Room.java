package com.example.demo.model;

public class Room {
    private Long id;

    private String type;
    private String topic;
    private String password;

    public Room(){

    }

    public Room(Long id, String type, String topic, String password) {
        this.id = id;
        this.type = type;
        this.topic = topic;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
