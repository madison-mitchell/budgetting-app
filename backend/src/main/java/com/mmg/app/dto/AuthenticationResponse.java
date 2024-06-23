package com.mmg.app.dto;

// AuthenticationResponse.java

public class AuthenticationResponse {
    private String jwt;
    private Long userId;
    private String username;

    // Add constructor, getters, and setters
    public AuthenticationResponse(String jwt, Long userId, String username) {
        this.jwt = jwt;
        this.userId = userId;
        this.username = username;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
