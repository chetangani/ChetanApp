package com.chetangani.myapp.fragments.services;

/**
 * Created by Chetan Gani on 4/18/2017.
 */

public class Servicedetails {
    String service_date="", description="", reading="", amount="", lastreading=""/*, distance=""*/;

    public Servicedetails(String service_date, String description, String reading, String amount, String lastreading/*, String distance*/) {
        this.service_date = service_date;
        this.description = description;
        this.reading = reading;
        this.amount = amount;
        this.lastreading = lastreading;
        /*this.distance = distance;*/
    }

    public String getService_date() {
        return service_date;
    }

    public String getDescription() {
        return description;
    }

    public String getReading() {
        return reading;
    }

    public String getAmount() {
        return amount;
    }

    public String getLastreading() {
        return lastreading;
    }

    /*public String getDistance() {
        return distance;
    }*/
}
