package com.example.carshowroom;


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
