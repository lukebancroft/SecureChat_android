package fr.mbds.org.securechat.database.entities;

public class Contact {
    public String uid, username, email;

    public Contact(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }
}
