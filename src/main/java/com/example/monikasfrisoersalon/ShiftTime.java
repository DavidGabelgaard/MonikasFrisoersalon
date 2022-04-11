package com.example.monikasfrisoersalon;


import java.time.LocalTime;

public class ShiftTime {

    private Worker worker;
    private LocalTime startTime;
    private LocalTime endTime;

    public ShiftTime(Worker worker, LocalTime startTime, LocalTime endTime) {
        this.worker = worker;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ShiftTime{" +
                "worker='" + worker + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
