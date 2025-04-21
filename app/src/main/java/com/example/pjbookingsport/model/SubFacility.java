package com.example.pjbookingsport.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubFacility implements Serializable {
    private Long subFacilityId;
    private String name;
    private SportFacility sportsFacility;
    private FacilityType facilityType;

    public SubFacility(Long subFacilityId, String name, SportFacility sportsFacility, FacilityType facilityType) {
        this.subFacilityId = subFacilityId;
        this.name = name;
        this.sportsFacility = sportsFacility;
        this.facilityType = facilityType;
    }

    public SportFacility getSportsFacility() {
        return sportsFacility;
    }

    public void setSportsFacility(SportFacility sportsFacility) {
        this.sportsFacility = sportsFacility;
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


    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }
}
