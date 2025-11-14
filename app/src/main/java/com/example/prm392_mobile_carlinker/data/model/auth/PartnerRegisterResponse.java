package com.example.prm392_mobile_carlinker.data.model.auth;

import com.google.gson.annotations.SerializedName;

public class PartnerRegisterResponse {

    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private PartnerData data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PartnerData getData() {
        return data;
    }

    public void setData(PartnerData data) {
        this.data = data;
    }

    public static class PartnerData {
        @SerializedName("userId")
        private int userId;

        @SerializedName("userEmail")
        private String userEmail;

        @SerializedName("userRole")
        private int userRole; // Changed from String to int - BE returns enum as number (1 = GARAGE)

        @SerializedName("garageId")
        private int garageId;

        @SerializedName("garageName")
        private String garageName;

        @SerializedName("garageEmail")
        private String garageEmail;

        @SerializedName("accessToken")
        private String accessToken;

        @SerializedName("refreshToken")
        private String refreshToken;

        // Getters and Setters
        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public int getUserRole() {
            return userRole;
        }

        public void setUserRole(int userRole) {
            this.userRole = userRole;
        }

        /**
         * Get user role as string
         * 0 = CUSTOMER, 1 = GARAGE, 2 = DEALER, 3 = STAFF, 4 = ADMIN
         */
        public String getUserRoleString() {
            switch (userRole) {
                case 0:
                    return "CUSTOMER";
                case 1:
                    return "GARAGE";
                case 2:
                    return "DEALER";
                case 3:
                    return "STAFF";
                case 4:
                    return "ADMIN";
                default:
                    return "UNKNOWN";
            }
        }

        public int getGarageId() {
            return garageId;
        }

        public void setGarageId(int garageId) {
            this.garageId = garageId;
        }

        public String getGarageName() {
            return garageName;
        }

        public void setGarageName(String garageName) {
            this.garageName = garageName;
        }

        public String getGarageEmail() {
            return garageEmail;
        }

        public void setGarageEmail(String garageEmail) {
            this.garageEmail = garageEmail;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
