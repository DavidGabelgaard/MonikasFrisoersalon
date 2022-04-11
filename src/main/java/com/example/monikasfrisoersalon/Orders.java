package com.example.monikasfrisoersalon;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;


public class Orders {

    private LocalTime startTime;
    private Duration duration;
    private ArrayList<Treatments> treatments;
    private Worker worker;

    public Orders(LocalTime startTime, Duration duration, ArrayList<Treatments> treatments, Worker worker) {
        this.startTime = startTime;
        this.duration = duration;
        this.treatments = treatments;
        this.worker = worker;
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

    public LocalTime getStartTime() {
        return startTime;
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
