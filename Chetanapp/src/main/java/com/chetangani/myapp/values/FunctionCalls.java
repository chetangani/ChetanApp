package com.chetangani.myapp.values;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chetan Gani on 4/14/2017.
 */

public class FunctionCalls {

    public String getcurrentdate() {
        String currentdate = "";
        Calendar c = Calendar.getInstance();
        currentdate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return currentdate;
    }

    public String convertdate(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String currdate = date.substring(8, 10);
        String currmonth = "";
        switch (month) {
            case "01":
                currmonth = "JAN";
                break;
            case "02":
                currmonth = "FEB";
                break;
            case "03":
                currmonth = "MAR";
                break;
            case "04":
                currmonth = "APR";
                break;
            case "05":
                currmonth = "MAY";
                break;
            case "06":
                currmonth = "JUN";
                break;
            case "07":
                currmonth = "JUL";
                break;
            case "08":
                currmonth = "AUG";
                break;
            case "09":
                currmonth = "SEP";
                break;
            case "10":
                currmonth = "OCT";
                break;
            case "11":
                currmonth = "NOV";
                break;
            case "12":
                currmonth = "DEC";
                break;
        }
        return currdate+"-"+currmonth+"-"+year;
    }

    public String getreturndate(String date) {
        String curdate = date.substring(0, 2);
        String month = date.substring(3, 6);
        String year = date.substring(7, 11);
        String curmonth = "";
        switch (month) {
            case "JAN":
                curmonth = "01";
                break;

            case "FEB":
                curmonth = "02";
                break;

            case "MAR":
                curmonth = "03";
                break;

            case "APR":
                curmonth = "04";
                break;

            case "MAY":
                curmonth = "05";
                break;

            case "JUN":
                curmonth = "06";
                break;

            case "JUL":
                curmonth = "07";
                break;

            case "AUG":
                curmonth = "08";
                break;

            case "SEP":
                curmonth = "09";
                break;

            case "OCT":
                curmonth = "10";
                break;

            case "NOV":
                curmonth = "11";
                break;

            case "DEC":
                curmonth = "12";
                break;
        }
        return year+"-"+curmonth+"-"+curdate;
    }

    public int getDuration(String curdate, String lastdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(lastdate);
            date2 = sdf.parse(curdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public String getcardnumber(String cardnumber) {
        return cardnumber.substring(0, 4) + cardnumber.substring(5, 9) + cardnumber.substring(10, 14) + cardnumber.substring(15, 19);
    }

    public String showcardnumber(String cardnumber) {
        return cardnumber.substring(0, 4) + " " + cardnumber.substring(4, 8) + " " + cardnumber.substring(8, 12) + " " +
                cardnumber.substring(12, 16);
    }
}
