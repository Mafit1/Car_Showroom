package com.example.carshowroom;

import android.os.Parcel;

public class Car {
    public String id, maker, model, color, configuration, year;

    public Car() {
    }

    public Car(String id, String maker, String model, String color, String configuration, String year) {
        this.id = id;
        this.maker = maker;
        this.model = model;
        this.color = color;
        this.configuration = configuration;
        this.year = year;
    }

    private Car(Parcel in) {
        id = in.readString();
        maker = in.readString();
        model = in.readString();
        color = in.readString();
        configuration = in.readString();
        year = in.readString();
    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", maker='" + maker + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", configuration='" + configuration + '\'' +
                ", year=" + year +
                '}';
    }
}
