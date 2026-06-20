package com.system.paperflow.domain.entity;

public abstract class User {

    private String username;
    private String email;
    private String password;
    private String institution;

    public User(String username, String email, String password, String institution) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.institution = institution;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getInstitution() {
        return institution;
    }

}
