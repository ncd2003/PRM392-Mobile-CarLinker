package com.example.prm392_mobile_carlinker.data.model.category;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CategoryResponse {

    private int status;
    private String message;
    private List<Data> data; // Trường này chứa danh sách Category

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public List<Data> getData() { return data; }

    // XÓA BỎ các getter/setter dư thừa cho id, name, description,... ở đây.

    public static class Data {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("description")
        private String description;
        @SerializedName("image")
        private String image;
        @SerializedName("isActive")
        private boolean isActive;

        // BẮT BUỘC PHẢI THÊM CÁC GETTER SAU:
        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        // Tùy chọn thêm các getter khác nếu cần
        public String getDescription() { return description; }
        public String getImage() { return image; }
        public boolean isActive() { return isActive; }
    }
}