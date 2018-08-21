package com.chetangani.myapp.values;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.chetangani.myapp.R;
import com.chetangani.myapp.database.Database;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FunctionCalls {

    public void showtoast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showsnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public String getCursorValue(Cursor data, String column_name) {
        return data.getString(data.getColumnIndexOrThrow(column_name));
    }

    public String getAmount(Context context, double value) {
        return Objects.requireNonNull(context).getResources().getString(R.string.rs) + " " + getRupeeformat(value) + " /-";
    }

//    public void logstatus(String value) {
//        Log.d("debug", value);
//    }

    private String getRupeeformat(double value) {
        DecimalFormat num = new DecimalFormat("##,##,##,##0.00");
        return String.valueOf(num.format(value));
    }

    public String getcurrentdate() {
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
    }

    /*public String getcurrentMonth() {
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(c.getTime());
    }

    public String getcurrentYear() {
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("yyyy", Locale.getDefault()).format(c.getTime());
    }*/

    public String getcurrentTime() {
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(c.getTime());
    }

    public String convertdate(String value) {
        String outDate = "";
        SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outSDF = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        if (value != null) {
            try {
                Date date = inSDF.parse(value);
                outDate = outSDF.format(date);
            } catch (ParseException ex){
                ex.printStackTrace();
            }
        }
        return outDate;
    }

    public String getreturndate(String value) {
        String outDate = "";
        SimpleDateFormat inSDF = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (value != null) {
            try {
                Date date = inSDF.parse(value);
                outDate = outSDF.format(date);
            } catch (ParseException ex){
                ex.printStackTrace();
            }
        }
        return outDate;
    }

    public String monthview(String date) {
        String s1 = date.substring(5, 7);
        String[] months = new String[]{"January", "February", "March",
                "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int m1 = Integer.parseInt(s1);
        return months[m1 - 1];
    }

    public String expenses_month_view(String date) {
        String s1 = date.substring(0, 4);
        return monthview(date) + " " + s1;
    }

    public String expenses_date_view(String value) {
        String outDate = "";
        SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outSDF = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        if (value != null) {
            try {
                Date date = inSDF.parse(value);
                outDate = outSDF.format(date);
            } catch (ParseException ex){
                ex.printStackTrace();
            }
        }
        return outDate;
    }

    public String expenses_day_view(String value) {
        String outDate = "";
        SimpleDateFormat inSDF = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (value != null) {
            try {
                Date date = inSDF.parse(value);
                outDate = outSDF.format(date);
            } catch (ParseException ex){
                ex.printStackTrace();
            }
        }
        return outDate;
    }

    public int getDuration(String curdate, String lastdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(lastdate);
            date2 = sdf.parse(curdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date2 != null;
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public String getcardnumber(String cardnumber) {
        return cardnumber.substring(0, 4) + cardnumber.substring(5, 9) + cardnumber.substring(10, 14) + cardnumber.substring(15,
                cardnumber.length());
    }

    public String showcardnumber(String cardnumber) {
        return cardnumber.substring(0, 4) + " " + cardnumber.substring(4, 8) + " " + cardnumber.substring(8, 12) + " " +
                cardnumber.substring(12, cardnumber.length());
    }

    public String hidecardnumber(String cardnumber) {
        String card = null, param;
        switch (cardnumber.length()) {
            case 16:
                card = cardnumber.substring(0, 4) + " " + "XXXX" + " " + "XXXX" + " " + cardnumber.substring(12, cardnumber.length());
                break;

            case 15:
                param = cardnumber.substring(11);
                param = param.substring(0, 1);
                card = cardnumber.substring(0, 4) + " " + "XXXX" + " " + "XXX" + param + " " + cardnumber.substring(12, cardnumber.length());
                break;

            case 14:
                param = cardnumber.substring(10);
                param = param.substring(0, 2);
                card = cardnumber.substring(0, 4) + " " + "XXXX" + " " + "XX" + param + " " + cardnumber.substring(12, cardnumber.length());
                break;
        }
        return card;
    }

    public String hideexpiry(String expiry) {
        return expiry.substring(0, 3) + "XX";
    }

    public int getBank_logo(String bank_code) {
        int result;
        switch (bank_code) {
            case "B001":
                result = R.drawable.allahabad_bank;
                break;

            case "B002":
                result = R.drawable.andhra_bank;
                break;

            case "B003":
                result = R.drawable.axis_bank;
                break;

            case "B004":
                result = R.drawable.baroda_bank;
                break;

            case "B005":
                result = R.drawable.bank_of_india;
                break;

            case "B006":
                result = R.drawable.canara_bank;
                break;

            case "B007":
                result = R.drawable.central_bank;
                break;

            case "B008":
                result = R.drawable.citi_bank;
                break;

            case "B009":
                result = R.drawable.corporation_bank;
                break;

            case "B010":
                result = R.drawable.dena_bank;
                break;

            case "B011":
                result = R.drawable.digi_bank;
                break;

            case "B012":
                result = R.drawable.federal_bank;
                break;

            case "B013":
                result = R.drawable.hdfc_bank;
                break;

            case "B014":
                result = R.drawable.hsbc_bank;
                break;

            case "B015":
                result = R.drawable.icici_bank;
                break;

            case "B016":
                result = R.drawable.idbi_bank;
                break;

            case "B017":
                result = R.drawable.idfc_bank;
                break;

            case "B018":
                result = R.drawable.indian_bank;
                break;

            case "B019":
                result = R.drawable.indian_overseas_bank;
                break;

            case "B020":
                result = R.drawable.indusind_bank;
                break;

            case "B021":
                result = R.drawable.karnataka_bank;
                break;

            case "B022":
                result = R.drawable.karur_vysya_bank;
                break;

            case "B023":
                result = R.drawable.kotak_bank;
                break;

            case "B024":
                result = R.drawable.lakshmi_vilas_bank;
                break;

            case "B025":
                result = R.drawable.punjab_national_bank;
                break;

            case "B026":
                result = R.drawable.rbl_bank;
                break;

            case "B027":
                result = R.drawable.south_indian_bank;
                break;

            case "B028":
                result = R.drawable.standard_chartered_bank;
                break;

            case "B029":
                result = R.drawable.sbi_bank;
                break;

            case "B030":
                result = R.drawable.syndicate_bank;
                break;

            case "B031":
                result = R.drawable.uco_bank;
                break;

            case "B032":
                result = R.drawable.union_bank;
                break;

            case "B033":
                result = R.drawable.united_bank;
                break;

            case "B034":
                result = R.drawable.vijaya_bank;
                break;

            case "B035":
                result = R.drawable.yes_bank;
                break;

            default:
                result = R.drawable.ic_menu_gallery;
                break;
        }
        return result;
    }

    public int getCard_logo(String card_number) {
        String digit1 = card_number.substring(0,1);
        String digit2 = card_number.substring(0,2);
        int result = 0;
        if (digit1.equals("4")) {
            result = R.drawable.visacard_logo;
        } else if (digit2.compareTo("51")>=0 && digit2.compareTo("55")<=0) {
            result = R.drawable.mastercard_logo;
        } else if (digit2.equals("34") || digit2.equals("37")) {
            result = R.drawable.american_express_logo;
        } else if (digit2.equals("36")) {
            result = R.drawable.diners_club_logo;
        }
        return result;
    }

    public String getCard_type(String card_type) {
        if (card_type.equals("0"))
            return "Debit";
        else return "Credit";
    }

    public String getCardName(Cursor data) {
        String name = getCursorValue(data, "card_name");
        String number = getCursorValue(data, "card_number");
        return name + " X" + number.substring(number.length()-4);
    }

    public String getSelected_card(Database database, int position) {
        Cursor data = database.getparticularcarddetails(String.valueOf(position));
        data.moveToNext();
        String number = data.getString(data.getColumnIndex("card_number"));
        data.close();
        return number;
    }

    public String getSelected_category(Database database, String category) {
        Cursor data = database.getCategory_code(category);
        data.moveToNext();
        String code = data.getString(data.getColumnIndex("category_code"));
        data.close();
        return code;
    }
}
