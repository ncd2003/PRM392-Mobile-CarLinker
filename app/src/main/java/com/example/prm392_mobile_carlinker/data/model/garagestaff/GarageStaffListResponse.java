package com.example.prm392_mobile_carlinker.data.model.garagestaff;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GarageStaffListResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private PaginatedData data;

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

    public PaginatedData getData() {
        return data;
    }

    public void setData(PaginatedData data) {
        this.data = data;
    }

    public static class PaginatedData {
        @SerializedName("items")
        private List<GarageStaffDto> items;

        @SerializedName("totalItems")
        private int totalItems;

        @SerializedName("currentPage")
        private int currentPage;

        @SerializedName("pageSize")
        private int pageSize;

        @SerializedName("totalPages")
        private int totalPages;

        public List<GarageStaffDto> getItems() {
            return items;
        }

        public void setItems(List<GarageStaffDto> items) {
            this.items = items;
        }

        public int getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(int totalItems) {
            this.totalItems = totalItems;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }
    }
}

