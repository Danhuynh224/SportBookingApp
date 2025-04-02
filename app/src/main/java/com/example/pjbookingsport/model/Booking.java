package com.example.pjbookingsport.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private Long bookingId;
//    private User user;
//    private SubFacility subFacility;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal totalPrice;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void Caculate(Price price){
        totalPrice=BigDecimal.valueOf(0);
        int hourStart = startTime.getHour();
        int hourEnd = endTime.getHour();
        DayOfWeek dayOfWeek = bookingDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY){
            totalPrice=BigDecimal.valueOf((hourEnd-hourStart)).multiply(price.getWeekTime());
            return;
        }

        for (int i = hourStart;i < hourEnd;i++){
            if(i<7){
                //Tiền sáng sớm
                totalPrice = totalPrice.add(price.getEarlyTime());
            } else if (i<17) {
                //Tiền ban ngày
                totalPrice = totalPrice.add(price.getDayTime());
            }
            else {
                //Tiền buổi tối
                totalPrice = totalPrice.add(price.getNightTime());
            }
        }
    }

    public Booking(Long bookingId, LocalTime startTime, LocalDate bookingDate, LocalTime endTime, BigDecimal totalPrice) {
        this.bookingId = bookingId;
        this.startTime = startTime;
        this.bookingDate = bookingDate;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
    }
}
