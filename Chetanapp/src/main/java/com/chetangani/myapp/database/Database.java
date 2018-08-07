package com.chetangani.myapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.chetangani.myapp.values.FunctionCalls;

import java.io.File;

import static com.chetangani.myapp.values.Constants.BANKS_TABLE;
import static com.chetangani.myapp.values.Constants.CARDS_TABLE;
import static com.chetangani.myapp.values.Constants.CATEGORY_TABLE;
import static com.chetangani.myapp.values.Constants.EXPENSE_TABLE;
import static com.chetangani.myapp.values.Constants.FUEL_TRACKER_TABLE;
import static com.chetangani.myapp.values.Constants.SERVICES_TABLE;

public class Database {
    private MyHelper mh;
    private SQLiteDatabase sdb;
    private FunctionCalls functionCalls = new FunctionCalls();

    public Database(Context context) {
        try {
            String DATABASE_NAME = "myapp.db";
            String DATABASE_PATH = filepath() + File.separator + DATABASE_NAME;
            int DATABASE_VERSION = 1;
            mh = new MyHelper(context, DATABASE_PATH, null, DATABASE_VERSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() {
        sdb = mh.getWritableDatabase();
    }

    public void close() {
        sdb.close();
    }

    private class MyHelper extends SQLiteOpenHelper {

        MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("Create table FUEL_TRACKER(_id integer primary key, start_reading text, end_reading text, " +
                    "fuel_price text, fuel_amount text, fuel_filled text, fuel_date text, fuel_last_date text, fuel_brand text);");
            db.execSQL("Create table SERVICES(_id integer primary key, service_date text, cur_reading text, " +
                    "particular text, description text, service_amount text, last_reading text);");
            db.execSQL("Create table CARDS(_id integer primary key, card_type text, card_number text, " +
                    "card_expiry text, card_cvv text, card_name text, bank_code text, login_id text, " +
                    "login_password text, trans_password text);");
            db.execSQL("Create table BANKS(_id integer primary key, bank_name text, bank_code text, bank_image text);");
            db.execSQL("Create table WEBSITES(_id integer primary key, sites_name text, login_id text, login_password text);");
            db.execSQL("Create table ONLINE_BANKING(_id integer primary key, bank_code text, login_id text, login_password text, " +
                    "trans_password text);");
            db.execSQL("Create table EXPENSES(_id integer primary key, exp_date text, exp_month text, exp_year text, " +
                    "exp_time text, exp_mode text, exp_credit text, exp_debit text, exp_description text, exp_card text, exp_category text);");
            db.execSQL("Create table CATEGORY(_id integer primary key, categories text, category_code text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String filepath() {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "MyApp"
                + File.separator + "Database");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return "" + dir;
    }

    /*-----------------------------------Fuel Trackers Details in Database-----------------------------------*/
    public void insertfueldetails(String reading, String fuelprice, String fuelamount, String fuelfilled, String fueldate, String fuel_brand) {
        ContentValues cv = new ContentValues();
        cv.put("start_reading", reading);
        cv.put("end_reading", "");
        cv.put("fuel_price", fuelprice);
        cv.put("fuel_amount", fuelamount);
        cv.put("fuel_filled", fuelfilled);
        cv.put("fuel_date", fueldate);
        cv.put("fuel_last_date", "");
        cv.put("fuel_brand", fuel_brand);
        sdb.insert(FUEL_TRACKER_TABLE, null, cv);
    }

    public Cursor updatefueldetails(String reading, String date, String id) {
        return sdb.rawQuery("UPDATE FUEL_TRACKER SET end_reading = '"+reading+"', fuel_last_date = '"+date+"' " +
                "WHERE _id = '"+id+"'", null);
    }

    public Cursor fueldetails() {
        return sdb.rawQuery("SELECT * FROM FUEL_TRACKER ORDER BY _id DESC", null);
    }

    public Cursor getlastfueldetails() {
        return sdb.rawQuery("select * from FUEL_TRACKER where _id = (select (MAX(_id))_id FROM FUEL_TRACKER)", null);
    }

    public Cursor deletefueldetails(String id) {
        return sdb.rawQuery("DELETE FROM FUEL_TRACKER WHERE _id = '"+id+"'", null);
    }

    /*-----------------------------------Service Details in Database-----------------------------------*/
    public void insertservicedetails(String date, String curreading, String particular, String description, String amount, String lastreading) {
        ContentValues cv = new ContentValues();
        cv.put("service_date", date);
        cv.put("cur_reading", curreading);
        cv.put("description", description);
        cv.put("particular", particular);
        cv.put("service_amount", amount);
        cv.put("last_reading", lastreading);
        sdb.insert(SERVICES_TABLE, null, cv);
    }

    public Cursor updateservicedetails(String servicedate, String reading, String description, String amount, String id) {
        return sdb.rawQuery("UPDATE SERVICES SET service_date = '"+servicedate+"', cur_reading = '"+reading+"', description = '"+description+"', " +
                "service_amount = '"+amount+"' WHERE _id = '"+id+"'", null);
    }

    public Cursor getservicedetails() {
        return sdb.rawQuery("SELECT * FROM SERVICES", null);
    }

    public Cursor getparticularservicedetails(String id) {
        return sdb.rawQuery("SELECT * FROM SERVICES WHERE _id = '"+id+"'", null);
    }

    public Cursor getlastserviceid() {
        return sdb.rawQuery("SELECT (max(_id))id FROM SERVICES", null);
    }

    public Cursor getservicelastreading() {
        return sdb.rawQuery("SELECT cur_reading FROM SERVICES WHERE _id = (SELECT (max(_id))id FROM SERVICES)", null);
    }

    /*-----------------------------------Cards Details in Database-----------------------------------*/
    public void insertcarddetails(String cardtype, String cardnumber, String cardexpiry, String cardcvv, String cardname,
                                  String bankcode, String loginid, String loginpassword, String transpassword) {
        ContentValues cv = new ContentValues();
        cv.put("card_type", cardtype);
        cv.put("card_number", cardnumber);
        cv.put("card_expiry", cardexpiry);
        cv.put("card_cvv", cardcvv);
        cv.put("card_name", cardname);
        cv.put("bank_code", bankcode);
        cv.put("login_id", loginid);
        cv.put("login_password", loginpassword);
        cv.put("trans_password", transpassword);
        sdb.insert(CARDS_TABLE, null, cv);
    }

    public Cursor updatecarddetails(String cardtype, String cardnumber, String cardexpiry, String cardcvv, String cardname,
                                    String bankcode, String loginid, String loginpassword, String transpassword, String id) {
        return sdb.rawQuery("UPDATE CARDS SET card_type = '"+cardtype+"', card_number = '"+cardnumber+"', card_expiry = '"+cardexpiry+"', " +
                "card_cvv = '"+cardcvv+"', card_name = '"+cardname+"', bank_code = '"+bankcode+"', login_id = '"+loginid+"', " +
                "login_password = '"+loginpassword+"', trans_password = '"+transpassword+"' WHERE _id = '"+id+"'", null);
    }

    public Cursor getcardsdetails() {
        return sdb.rawQuery("SELECT * FROM CARDS", null);
    }

    public String getCardname(String cardnumber) {
        String card_name;
        Cursor data = sdb.rawQuery("SELECT * FROM CARDS WHERE card_number = '"+cardnumber+"'", null);
        data.moveToNext();
        String name = functionCalls.getCursorValue(data, "card_name");
        String number = functionCalls.getCursorValue(data, "card_number");
        card_name = name + " X" + number.substring(number.length()-4);
        data.close();
        return card_name;
    }

    public Cursor getparticularcarddetails(String id) {
        return sdb.rawQuery("SELECT * FROM CARDS WHERE _id = '"+id+"'", null);
    }

    public Cursor deletecarddetails(String id) {
        return sdb.rawQuery("DELETE FROM CARDS WHERE _id = '"+id+"'", null);
    }

    public Cursor checkcarddetails(String cardnumber) {
        return sdb.rawQuery("SELECT * FROM CARDS WHERE card_number = '"+cardnumber+"'", null);
    }

    public Cursor getbankcodelist() {
        return sdb.rawQuery("SELECT distinct(bank_code)bank_code FROM CARDS ORDER BY bank_code", null);
    }

    public Cursor sorting_cards(int card_type_position, String bank_code) {
        String card_type = null;
        switch (card_type_position) {
            case 0:
                card_type = "'0','1'";
                break;

            case 1:
                card_type = "'1'";
                break;

            case 2:
                card_type = "'0'";
                break;
        }
        if (TextUtils.isEmpty(bank_code)) {
            return sdb.rawQuery("SELECT * FROM CARDS WHERE card_type in("+card_type+")", null);
        } else {
            return sdb.rawQuery("SELECT * FROM CARDS WHERE card_type in("+card_type+") and bank_code = '"+bank_code+"'", null);
        }
    }

    /*-----------------------------------Banks details in Database-----------------------------------*/
    public void insertbankdetails(String bankname, String bankcode, String bankimages) {
        ContentValues cv = new ContentValues();
        cv.put("bank_name", bankname);
        cv.put("bank_code", bankcode);
        cv.put("bank_image", bankimages);
        sdb.insert(BANKS_TABLE, null, cv);
    }

    public Cursor getbanks() {
        return sdb.rawQuery("SELECT * FROM BANKS", null);
    }

    public Cursor getbankdetailsbyname(String bankname) {
        return sdb.rawQuery("SELECT * FROM BANKS WHERE bank_name = '"+bankname+"'", null);
    }

    public Cursor getbankdetailsbycode(String bankcode) {
        return sdb.rawQuery("SELECT * FROM BANKS WHERE bank_code = '"+bankcode+"'", null);
    }

    /*-----------------------------------Expenses Details in Database-----------------------------------*/
    public Cursor getCategorydetails() {
        return sdb.rawQuery("SELECT * FROM CATEGORY", null);
    }

    public void insertcategorydetails(String category, String code) {
        ContentValues cv = new ContentValues();
        cv.put("categories", category);
        cv.put("category_code", code);
        sdb.insertOrThrow(CATEGORY_TABLE, null, cv);
    }

    public Cursor getCategorylist() {
        return sdb.rawQuery("SELECT categories FROM CATEGORY ORDER BY category_code ASC", null);
    }

    public String getCategory(String code) {
        String category;
        Cursor data = sdb.rawQuery("SELECT categories FROM CATEGORY WHERE category_code ='"+code+"'", null);
        data.moveToNext();
        category = functionCalls.getCursorValue(data, "categories");
        data.close();
        return category;
    }

    public Cursor getCategory_code(String category) {
        return sdb.rawQuery("SELECT category_code FROM CATEGORY WHERE categories ='"+category+"'", null);
    }

    public String insert_new_category(String category) {
        Cursor data_count = sdb.rawQuery("SELECT COUNT(category_code)COUNT FROM CATEGORY", null);
        data_count.moveToNext();
        int count = data_count.getInt(data_count.getColumnIndex("COUNT"));
        data_count.close();
        count = count - 1;
        String data = String.valueOf(count);
        if (data.length() == 1)
            data = "0"+data;
        insertcategorydetails(category, data);
        return data;
    }

    public boolean insert_Expense_details(String exp_date, String exp_month, String exp_year, String exp_time, String exp_mode,
                                       String exp_credit, String exp_debit, String exp_description, String exp_card, String exp_category) {
        ContentValues cv = new ContentValues();
        cv.put("exp_date", exp_date);
        cv.put("exp_month", exp_month);
        cv.put("exp_year", exp_year);
        cv.put("exp_time", exp_time);
        cv.put("exp_mode", exp_mode);
        cv.put("exp_credit", exp_credit);
        cv.put("exp_debit", exp_debit);
        cv.put("exp_description", exp_description);
        cv.put("exp_card", exp_card);
        cv.put("exp_category", exp_category);
        long result = sdb.insertOrThrow(EXPENSE_TABLE, null, cv);
        return result != -1;
    }

    public Cursor getBalances(String month) {
        return sdb.rawQuery("SELECT SUM(exp_debit)DEBIT, SUM(exp_credit)CREDIT FROM EXPENSES WHERE exp_month = '"+month+"'", null);
    }

    public Cursor getTotalExpenses() {
        return sdb.rawQuery("SELECT exp_date, exp_month, SUM(exp_debit)AMOUNT, COUNT(exp_date)ENTRY FROM EXPENSES WHERE exp_debit != '0' GROUP BY exp_date ORDER BY exp_date DESC", null);
    }

    public Cursor getTotalMonthExpenses() {
        return sdb.rawQuery("SELECT exp_month, SUM(exp_debit)AMOUNT FROM EXPENSES GROUP BY exp_month ORDER BY exp_month DESC", null);
    }

    public Cursor getDaydetails(String value) {
        return sdb.rawQuery("SELECT * FROM EXPENSES where exp_date = '"+value+"'", null);
    }
}
