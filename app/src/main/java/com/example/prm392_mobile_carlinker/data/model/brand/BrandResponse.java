package com.example.prm392_mobile_carlinker.data.model.brand;

import com.google.gson.annotations.SerializedName;
import java.util.List;
public class BrandResponse {

    private int status;
    private String message;
    private List<Data> data;

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public List<Data> getData() { return data; }

    public static class Data {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("country")
        private String country;
        @SerializedName("image")
        private String image;
        @SerializedName("isActive")
        private boolean isActive;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getCountry() { return country; }
        public String getImage() { return image; }
        public boolean isActive() { return isActive; }
    }
}
