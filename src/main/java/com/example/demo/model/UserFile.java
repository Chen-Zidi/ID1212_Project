package com.example.demo.model;

import java.sql.Date;

public class UserFile {

    private Long id;

    private String type;
    private String name;
    private Long owner;
    private Long room;
    private Date updateTime;

    public UserFile(){

    }

    public UserFile(Long id, String type, String name, Long owner, Long room, Date updateTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.owner = owner;
        this.room = room;
        this.updateTime = updateTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Long getRoom() {
        return room;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
