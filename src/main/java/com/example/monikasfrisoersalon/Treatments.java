package com.example.monikasfrisoersalon;

import java.time.LocalTime;

public class Treatments {

    int id;
    String name;
    int price;
    LocalTime time;
    String backgroundColor;

    public Treatments(int id, String name, int price, LocalTime time, String backgroundColor) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.time = time;
        this.backgroundColor = backgroundColor;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }


    @Override
    public String toString() {
        return "Treatments{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", time=" + time +
                ", backgroundColor='" + backgroundColor + '\'' +
                '}';
    }
}



// new git test