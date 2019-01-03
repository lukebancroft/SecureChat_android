package fr.mbds.org.securechat.database.entities;

public class User {
    public String username, email, password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
