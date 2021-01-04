package com.example.demo.model;

import java.io.File;
import java.util.Date;

public class Message {
    String sender;
    String receiver;
    String content;
    boolean isFile;
    File file;


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    Date time;

    public Message(String sender, String receiver, String content, Date time) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
    }
}
