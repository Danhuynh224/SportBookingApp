package com.example.pjbookingsport.model;

public class FacilityType {
    private Long facilityTypeId;
    private String name;

    public Long getFacilityTypeId() {
        return facilityTypeId;
    }

    public void setFacilityTypeId(Long facilityTypeId) {
        this.facilityTypeId = facilityTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FacilityType(Long facilityTypeId, String name) {
        this.facilityTypeId = facilityTypeId;
        this.name = name;
    }
}
