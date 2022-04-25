package com.example.monikasfrisoersalon;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class Orders {

    private LocalTime startTime;
    private Duration duration;
    private ArrayList<Treatments> treatments;
    private Worker worker;
    private LocalDate date;
    private String bookingName;
    private String bookingPhoneNumber;
    private String bookingEmail;
    private int bookingID;

    public Orders(LocalTime startTime, Duration duration, ArrayList<Treatments> treatments, Worker worker) {
        this.startTime = startTime;
        this.duration = duration;
        this.treatments = treatments;
        this.worker = worker;
    }

    public Orders(LocalTime startTime, Duration duration, ArrayList<Treatments> treatments, Worker worker, LocalDate date, String bookingName, String bookingPhoneNumber, String bookingEmail) {
        this.startTime = startTime;
        this.duration = duration;
        this.treatments = treatments;
        this.worker = worker;
        this.date = date;
        this.bookingName = bookingName;
        this.bookingPhoneNumber = bookingPhoneNumber;
        this.bookingEmail = bookingEmail;
    }

    public Orders(ArrayList<Treatments> treatments, LocalDate date, String bookingName, String bookingPhoneNumber, int bookingID) {
        this.treatments = treatments;
        this.date = date;
        this.bookingName = bookingName;
        this.bookingPhoneNumber = bookingPhoneNumber;
        this.bookingID = bookingID;
    }

    public int getBookingID() {
        return bookingID;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "startTime=" + startTime +
                ", duration=" + duration +
                ", treatments=" + treatments +
                ", worker='" + worker + '\'' +
                '}';
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getBookingName() {
        return bookingName;
    }

    public String getBookingPhoneNumber() {
        return bookingPhoneNumber;
    }

    public String getBookingEmail() {
        return bookingEmail;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ArrayList<Treatments> getTreatments() {
        return treatments;
    }

    public void setTreatments(ArrayList<Treatments> treatments) {
        this.treatments = treatments;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
