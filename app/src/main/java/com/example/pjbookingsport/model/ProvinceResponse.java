package com.example.pjbookingsport.model;

import java.util.List;

public class ProvinceResponse {
    private Integer error;
    private String error_text;
    private String data_name;
    private List<Province> data;

    public Integer getError() {
        return error;
    }

    public String getError_text() {
        return error_text;
    }

    public String getData_name() {
        return data_name;
    }

    public List<Province> getData() {
        return data;
    }
}
