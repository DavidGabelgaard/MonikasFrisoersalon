package com.example.monikasfrisoersalon;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class WorkDay {

    private LocalDate date;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private ArrayList<Orders> orders;
    private ArrayList<ShiftTime> shiftTimes;

    public WorkDay(LocalDate date, LocalTime openingHour, LocalTime closingHour, ArrayList<Orders> orders, ArrayList<ShiftTime> shiftTimes) {
        this.date = date;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.orders = orders;
        this.shiftTimes = shiftTimes;
    }

    public WorkDay(LocalDate date, LocalTime openingHour, LocalTime closingHour) {
        this.date = date;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.orders = new ArrayList<>();
        this.shiftTimes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "WorkDay{" +
                "date=" + date +
                ", openingHour=" + openingHour +
                ", closingHour=" + closingHour +
                ", orders=" + orders +
                ", shiftTimes=" + shiftTimes +
                '}';
    }

    public LocalDate getDate() {
        return date;
    }

    public void addShiftTime(ShiftTime shiftTime) {
        this.shiftTimes.add(shiftTime);
    }

    public void addOrders(Orders orders) {this.orders.add(orders);}

    public ArrayList<Orders> getOrders() {
        return orders;
    }

    public ArrayList<ShiftTime> getShiftTimes() {
        return shiftTimes;
    }
}
