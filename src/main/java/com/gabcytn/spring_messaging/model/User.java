package com.gabcytn.spring_messaging.model;

import java.util.UUID;

public class User {
    private UUID id = UUID.randomUUID();
    private String username;
    private String password;
    private String profilePic;
    private String role;

    public User () {}

    public User(UUID id, String username, String password, String profilePic, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profilePic = profilePic;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
