package com.example.prm392_mobile_carlinker.data.model.servicerecord;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for service record list
 */
public class ServiceRecordListResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private PaginatedData data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public PaginatedData getData() {
        return data;
    }

    public static class PaginatedData {
        @SerializedName("size")
        private int size;

        @SerializedName("page")
        private int page;

        @SerializedName("total")
        private int total;

        @SerializedName("totalPages")
        private int totalPages;

        @SerializedName("items")
        private List<ServiceRecordDto> items;

        public int getSize() {
            return size;
        }

        public int getPage() {
            return page;
        }

        public int getTotal() {
            return total;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public List<ServiceRecordDto> getItems() {
            return items;
        }
    }
}
