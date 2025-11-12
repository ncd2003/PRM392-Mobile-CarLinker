package com.example.prm392_mobile_carlinker.data.model.auth;

public class LoginResponse {
    private int status;
    private String message;
    private Data data;

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public Data getData() { return data; }

    public static class Data {
        private String accessToken;
        private String refreshToken;
        private User user;

        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public User getUser() { return user; }
    }

    public static class User {
        private int id;
        private String email;
        private String role;

        public int getId() { return id; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }
}
