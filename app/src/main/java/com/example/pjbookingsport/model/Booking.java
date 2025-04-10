package com.example.pjbookingsport.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Booking implements Serializable {
    private Long bookingId;
    private User user;

    private LocalDate bookingDate;

    private BigDecimal totalPrice;
    private int totalHour ;
    private List<BookingInfo> bookingInfos;
    private String note;

    public boolean isEmpty() {
        return (
                bookingInfos == null || bookingInfos.isEmpty()
                );
    }
    public String getNote() {
        return note;
    }
    public Booking() {
    }
    public void setNote(String note) {
        this.note = note;
    }

    public List<BookingInfo> getBookingInfos() {
        return bookingInfos;
    }

    public void setBookingInfos(List<BookingInfo> bookingInfos) {
        this.bookingInfos = bookingInfos;
    }


    public int getTotalHour() {
        return totalHour;
    }
    public void setTotalHour(int totalHour) {
        this.totalHour = totalHour;
    }
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void Caculate(Price price){
        totalPrice=BigDecimal.valueOf(0);
        totalHour =0 ;
        for (BookingInfo bookingInfo: bookingInfos) {
            BigDecimal infoPrice =BigDecimal.valueOf(0);
            int hourStart = bookingInfo.getStartTime().getHour();
            int hourEnd = bookingInfo.getEndTime().getHour();
            totalHour+=hourEnd-hourStart;
            DayOfWeek dayOfWeek = bookingDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY){
                infoPrice = BigDecimal.valueOf((hourEnd-hourStart)).multiply(price.getWeekTime());

            }
            else {
                for (int i = hourStart; i < hourEnd; i++) {
                    if (i < 7) {
                        //Tiền sáng sớm
                        infoPrice = infoPrice.add(price.getEarlyTime());
                    } else if (i < 17) {
                        //Tiền ban ngày
                        infoPrice = infoPrice.add(price.getDayTime());
                    } else {
                        //Tiền buổi tối
                        infoPrice = infoPrice.add(price.getNightTime());
                    }
                }
            }
            bookingInfo.setTotalPrice(infoPrice);
            totalPrice=totalPrice.add(infoPrice);
        }

    }


}
