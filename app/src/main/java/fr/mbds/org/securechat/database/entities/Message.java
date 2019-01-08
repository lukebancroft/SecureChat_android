package fr.mbds.org.securechat.database.entities;

import java.util.Date;

public class Message {
    public String body, sender;
    public Date timestamp;

    public Message(String body, String sender, Date timestamp) {
        this.body = body;
        this.sender = sender;
        this.timestamp = timestamp;
    }
}
