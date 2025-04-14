package com.example.pjbookingsport.model;

public class SubFacility {
    private Long subFacilityId;
    private String name;
    private Long facilityId;
    private FacilityType facilityType;

    private SportFacility sportFacility;


    public SubFacility(Long subFacilityId, String name, Long facilityId, FacilityType facilityType, SportFacility sportFacility) {
        this.subFacilityId = subFacilityId;
        this.name = name;
        this.facilityId = facilityId;
        this.facilityType = facilityType;
        this.sportFacility = sportFacility;
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
    public SportFacility getSportFacility() {
        return sportFacility;
    }

    public void setSportFacility(SportFacility sportFacility) {
        this.sportFacility = sportFacility;
    }
}
