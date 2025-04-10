package com.example.pjbookingsport.model;

import java.math.BigDecimal;
import java.time.LocalTime;

public class BookingInfo {

    private SubFacility subFacility;

    @Override
    public String toString() {
        return "BookingInfo{" +
                "subFacility=" + subFacility +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    private LocalTime startTime;

    private LocalTime endTime;

//    private Booking booking;
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public SubFacility getSubFacility() {
        return subFacility;
    }

    public void setSubFacility(SubFacility subFacility) {
        this.subFacility = subFacility;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }



    public BookingInfo( SubFacility subFacility, LocalTime startTime, LocalTime endTime) {
        this.subFacility = subFacility;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
