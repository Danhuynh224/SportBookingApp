package com.example.pjbookingsport.model;

public class SubFacility {
    private Long subFacilityId;
    private String name;
    private Long facilityId;
    private FacilityType facilityType;

    public SubFacility(Long subFacilityId, String name, Long facilityId, FacilityType facilityType) {
        this.subFacilityId = subFacilityId;
        this.name = name;
        this.facilityId = facilityId;
        this.facilityType = facilityType;
    }

    public Long getSubFacilityId() {
        return subFacilityId;
    }

    public void setSubFacilityId(Long subFacilityId) {
        this.subFacilityId = subFacilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }
}
