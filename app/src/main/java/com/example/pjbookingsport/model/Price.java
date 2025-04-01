package com.example.pjbookingsport.model;

import java.math.BigDecimal;

public class Price {
    private  Long priceID;
    private BigDecimal earlyTime;
    private BigDecimal dayTime ;
    private BigDecimal nightTime ;
    private BigDecimal weekTime ;
    private FacilityType facilityType;

    public Price(Long priceID, BigDecimal dayTime, BigDecimal earlyTime, BigDecimal nightTime, BigDecimal weekTime, FacilityType facilityType) {
        this.priceID = priceID;
        this.dayTime = dayTime;
        this.earlyTime = earlyTime;
        this.nightTime = nightTime;
        this.weekTime = weekTime;
        this.facilityType = facilityType;
    }

    public Long getPriceID() {
        return priceID;
    }

    public void setPriceID(Long priceID) {
        this.priceID = priceID;
    }

    public BigDecimal getEarlyTime() {
        return earlyTime;
    }

    public void setEarlyTime(BigDecimal earlyTime) {
        this.earlyTime = earlyTime;
    }

    public BigDecimal getDayTime() {
        return dayTime;
    }

    public void setDayTime(BigDecimal dayTime) {
        this.dayTime = dayTime;
    }

    public BigDecimal getNightTime() {
        return nightTime;
    }

    public void setNightTime(BigDecimal nightTime) {
        this.nightTime = nightTime;
    }

    public BigDecimal getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(BigDecimal weekTime) {
        this.weekTime = weekTime;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

}
