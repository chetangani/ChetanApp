package com.chetangani.myapp.fragments.fueltracker;

/**
 * Created by Chetan Gani on 4/16/2017.
 */

public class Fueldetails {
    String startreading="", endreading="", fuelprice="", fuelfilled="", fueldate="", fuelamount="", fuellastdate="", fuelid="";

    public Fueldetails(String fuelid, String startreading, String endreading, String fuelprice, String fuelfilled, String fueldate,
                       String fuelamount, String fuellastdate) {
        this.fuelid = fuelid;
        this.startreading = startreading;
        this.endreading = endreading;
        this.fuelprice = fuelprice;
        this.fuelfilled = fuelfilled;
        this.fueldate = fueldate;
        this.fuelamount = fuelamount;
        this.fuellastdate = fuellastdate;
    }

    public String getFuelid() {
        return fuelid;
    }

    public String getStartreading() {
        return startreading;
    }

    public String getEndreading() {
        return endreading;
    }

    public String getFuelprice() {
        return fuelprice;
    }

    public String getFuelfilled() {
        return fuelfilled;
    }

    public String getFueldate() {
        return fueldate;
    }

    public String getFuelamount() {
        return fuelamount;
    }

    public String getFuellastdate() {
        return fuellastdate;
    }
}
