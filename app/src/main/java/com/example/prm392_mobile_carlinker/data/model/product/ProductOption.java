package com.example.prm392_mobile_carlinker.data.model.product;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductOption {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("optionValues")
    private List<OptionValue> optionValues;

    // Constructors
    public ProductOption() {
    }

    public ProductOption(int id, String name, List<OptionValue> optionValues) {
        this.id = id;
        this.name = name;
        this.optionValues = optionValues;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OptionValue> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(List<OptionValue> optionValues) {
        this.optionValues = optionValues;
    }
}

