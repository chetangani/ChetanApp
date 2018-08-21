package com.chetangani.myapp.fragments.fueltracker;

import java.io.Serializable;

public class GetSet_Fueldetails implements Serializable {
    private String startreading, endreading, fuelprice, fuelfilled, fueldate, fuelamount, fuellastdate, fuelid, fuel_brand,
            test_mileage, test_distance;

    public GetSet_Fueldetails() {
    }

    public GetSet_Fueldetails(String fuelid, String startreading, String endreading, String fuelprice, String fuelfilled, String fueldate,
                              String fuelamount, String fuellastdate, String fuelbrand) {
        this.fuelid = fuelid;
        this.startreading = startreading;
        this.endreading = endreading;
        this.fuelprice = fuelprice;
        this.fuelfilled = fuelfilled;
        this.fueldate = fueldate;
        this.fuelamount = fuelamount;
        this.fuellastdate = fuellastdate;
        this.fuel_brand = fuelbrand;
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

    public String getFuel_brand() {
        return fuel_brand;
    }

    public String getTest_mileage() {
        return test_mileage;
    }

    public void setTest_mileage(String test_mileage) {
        this.test_mileage = test_mileage;
    }

    public String getTest_distance() {
        return test_distance;
    }

    public void setTest_distance(String test_distance) {
        this.test_distance = test_distance;
    }
}
