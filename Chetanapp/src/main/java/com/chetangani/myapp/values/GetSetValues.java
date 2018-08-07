package com.chetangani.myapp.values;

public class GetSetValues {
    private String mileage="", distance="";
    private int last_reading=0;

    public GetSetValues() {
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getLast_reading() {
        return last_reading;
    }

    public void setLast_reading(int last_reading) {
        this.last_reading = last_reading;
    }
}
