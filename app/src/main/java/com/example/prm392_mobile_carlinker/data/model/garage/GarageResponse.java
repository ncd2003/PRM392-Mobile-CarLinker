package com.example.prm392_mobile_carlinker.data.model.garage;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response class cho API lấy danh sách garage
 */
public class GarageResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private GarageData data;

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

    public GarageData getData() {
        return data;
    }

    public void setData(GarageData data) {
        this.data = data;
    }

    /**
     * Inner class chứa dữ liệu garage với phân trang
     */
    public static class GarageData {
        @SerializedName("size")
        private int size;

        @SerializedName("page")
        private int page;

        @SerializedName("total")
        private int total;

        @SerializedName("totalPages")
        private int totalPages;

        @SerializedName("items")
        private List<Garage> items;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public List<Garage> getItems() {
            return items;
        }

        public void setItems(List<Garage> items) {
            this.items = items;
        }
    }
}

