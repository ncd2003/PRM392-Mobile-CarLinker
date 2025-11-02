package com.example.prm392_mobile_carlinker.data.model.product;

import com.google.gson.annotations.SerializedName;

public class OptionValue {
    @SerializedName("id")
    private int id;

    @SerializedName("value")
    private String value;

    // Constructors
    public OptionValue() {
    }

    public OptionValue(int id, String value) {
        this.id = id;
        this.value = value;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

