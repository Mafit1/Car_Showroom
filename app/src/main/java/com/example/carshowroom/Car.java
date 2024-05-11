package com.example.carshowroom;


import java.io.Serializable;

public class Car implements Serializable {
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
    /*
    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", maker='" + maker + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", configuration='" + configuration + '\'' +
                ", year='" + year + '\'' +
                '}';
    }

     */

    public String getId() {
        return id;
    }

    public String getMaker() {
        return maker;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getYear() {
        return year;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return maker + " " + model;
    }
}
