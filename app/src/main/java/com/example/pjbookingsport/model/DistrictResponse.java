package com.example.pjbookingsport.model;

import java.util.List;

public class DistrictResponse {
    private Integer error;
    private String error_text;
    private String data_name;
    private List<District> data;

    public Integer getError() {
        return error;
    }

    public String getError_text() {
        return error_text;
    }

    public String getData_name() {
        return data_name;
    }

    public List<District> getData() {
        return data;
    }
}
