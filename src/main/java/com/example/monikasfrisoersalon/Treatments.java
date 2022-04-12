package com.example.monikasfrisoersalon;

import java.time.LocalTime;

public class Treatments {

    int id;
    String name;
    int Price;
    LocalTime time;
    String backgroundColor;

    public Treatments(int id, String name, int price, LocalTime time, String backgroundColor) {
        this.id = id;
        this.name = name;
        Price = price;
        this.time = time;
        this.backgroundColor = backgroundColor;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return Price;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}

