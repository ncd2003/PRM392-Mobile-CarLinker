package com.example.prm392_mobile_carlinker.data.model.user;

public class UserResponse {
    private int status;
    private String message;
    private User data;

    public UserResponse() {
    }

    public UserResponse(int status, String message, User data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}

