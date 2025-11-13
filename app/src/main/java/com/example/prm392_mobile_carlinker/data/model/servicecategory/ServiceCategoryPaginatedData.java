package com.example.prm392_mobile_carlinker.data.model.servicecategory;

import java.util.List;

public class ServiceCategoryPaginatedData {
    private int size;
    private int page;
    private int total;
    private int totalPages;
    private List<ServiceCategory> items;

    public ServiceCategoryPaginatedData() {
    }

    public ServiceCategoryPaginatedData(int size, int page, int total, int totalPages, List<ServiceCategory> items) {
        this.size = size;
        this.page = page;
        this.total = total;
        this.totalPages = totalPages;
        this.items = items;
    }

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

    public List<ServiceCategory> getItems() {
        return items;
    }

    public void setItems(List<ServiceCategory> items) {
        this.items = items;
    }
}

