package com.example.safevision.models;

public class LoginResponse {
    private String message;
    private String token;
    private int user_id;
    private String username;
    private String email;
    private String fullName;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public int getUserId() { return user_id; }
    public void setUserId(int userId) { this.user_id = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}