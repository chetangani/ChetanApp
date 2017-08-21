package com.chetangani.myapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Chetan Gani on 4/15/2017.
 */

public class Database {
    String DATABASE_NAME = "myapp.db", DATABASE_PATH;
    int DATABASE_VERSION = 1;
    MyHelper mh;
    SQLiteDatabase sdb;
    File databasefile = null;

    public Database(Context context) {
        try {
            databasefile = filestorepath(DATABASE_NAME);
            DATABASE_PATH = filepath() + File.separator + DATABASE_NAME;
            mh = new MyHelper(context, DATABASE_PATH, null, DATABASE_VERSION);
        } catch (Exception e) {
        }
    }

    public void open() {
        sdb = mh.getWritableDatabase();
    }

    public void close() {
        sdb.close();
    }

    //delete database
    public void db_delete() {
        File file = new File(DATABASE_PATH);
        if(file.exists()) {
            file.delete();
        }
    }

    private class MyHelper extends SQLiteOpenHelper {

        public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("Create table FUEL_TRACKER(_id integer primary key, start_reading text, end_reading text, " +
                    "fuel_price text, fuel_amount text, fuel_filled text, fuel_date text, fuel_last_date text);");
            db.execSQL("Create table SERVICES(_id integer primary key, service_date text, cur_reading text, " +
                    "description text, service_amount text, last_reading text);");
            db.execSQL("Create table CARDS(_id integer primary key, card_type text, card_number text, " +
                    "card_expiry text, card_cvv text, card_name text, bank_code text, login_id text, " +
                    "login_password text, trans_password text);");
            db.execSQL("Create table BANKS(_id integer primary key, bank_name text, bank_code text, bank_image text);");
            db.execSQL("Create table WEBSITES(_id integer primary key, sites_name text, login_id text, login_password text);");
            db.execSQL("Create table ONLINE_BANKING(_id integer primary key, bank_code text, login_id text, login_password text, " +
                    "trans_password text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public String filepath() {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "MyApp"
                + File.separator + "Database");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String pathname = "" + dir;
        return pathname;
    }

    public File filestorepath(String file) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "MyApp"
                + File.separator + "Database");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dir1 = new File(dir, File.separator + file);
        return dir1;
    }

    /*
    * Fuel Trackers Details in Database
    */
    public void insertfueldetails(String reading, String fuelprice, String fuelamount, String fuelfilled, String fueldate) {
        ContentValues cv = new ContentValues();
        cv.put("start_reading", reading);
        cv.put("end_reading", "");
        cv.put("fuel_price", fuelprice);
        cv.put("fuel_amount", fuelamount);
        cv.put("fuel_filled", fuelfilled);
        cv.put("fuel_date", fueldate);
        cv.put("fuel_last_date", "");
        sdb.insert("FUEL_TRACKER", null, cv);
    }

    public Cursor updatefueldetails(String reading, String date, String id) {
        Cursor c = null;
        c = sdb.rawQuery("UPDATE FUEL_TRACKER SET end_reading = '"+reading+"', fuel_last_date = '"+date+"' WHERE _id = '"+id+"'", null);
        return c;
    }

    public Cursor fueldetails() {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM FUEL_TRACKER ORDER BY _id DESC", null);
        return c;
    }

    public Cursor getlastid() {
        Cursor c = null;
        c = sdb.rawQuery("SELECT (max(_id))id FROM FUEL_TRACKER", null);
        return c;
    }

    public Cursor getlastfueldetails() {
        Cursor c = null;
        c = sdb.rawQuery("select * from FUEL_TRACKER where _id = (select (MAX(_id))_id FROM FUEL_TRACKER)", null);
        return c;
    }

    public Cursor getparticularfueldetails(String id) {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM FUEL_TRACKER WHERE _id = '"+id+"'", null);
        return c;
    }

    public Cursor deletefueldetails(String id) {
        Cursor c = null;
        c = sdb.rawQuery("DELETE FROM FUEL_TRACKER WHERE _id = '"+id+"'", null);
        return c;
    }

    /*
    * Service Details in Database
    */
    public void insertservicedetails(String date, String curreading, String description, String amount, String lastreading) {
        ContentValues cv = new ContentValues();
        cv.put("service_date", date);
        cv.put("cur_reading", curreading);
        cv.put("description", description);
        cv.put("service_amount", amount);
        cv.put("last_reading", lastreading);
        sdb.insert("SERVICES", null, cv);
    }

    public Cursor updateservicedetails(String servicedate, String reading, String description, String amount, String id) {
        Cursor c = null;
        c = sdb.rawQuery("UPDATE SERVICES SET service_date = '"+servicedate+"', cur_reading = '"+reading+"', description = '"+description+"', " +
                "service_amount = '"+amount+"' WHERE _id = '"+id+"'", null);
        return c;
    }

    public Cursor getservicedetails() {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM SERVICES", null);
        return c;
    }

    public Cursor getparticularservicedetails(String id) {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM SERVICES WHERE _id = '"+id+"'", null);
        return c;
    }

    public Cursor getlastserviceid() {
        Cursor c = null;
        c = sdb.rawQuery("SELECT (max(_id))id FROM SERVICES", null);
        return c;
    }

    public Cursor getservicelastreading() {
        Cursor c = null;
        c = sdb.rawQuery("SELECT cur_reading FROM SERVICES WHERE _id = (SELECT (max(_id))id FROM SERVICES)", null);
        return c;
    }

    /*
    * Cards Details in Database
    */
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
        sdb.insert("CARDS", null, cv);
    }

    public Cursor updatecarddetails(String cardtype, String cardnumber, String cardexpiry, String cardcvv, String cardname,
                                    String bankcode, String loginid, String loginpassword, String transpassword, String id) {
        Cursor c = null;
        c = sdb.rawQuery("UPDATE CARDS SET card_type = '"+cardtype+"', card_number = '"+cardnumber+"', card_expiry = '"+cardexpiry+"', " +
                "card_cvv = '"+cardcvv+"', card_name = '"+cardname+"', bank_code = '"+bankcode+"', login_id = '"+loginid+"', " +
                "login_password = '"+loginpassword+"', trans_password = '"+transpassword+"' WHERE _id = '"+id+"'", null);
        return c;
    }

    public Cursor getcardsdetails() {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM CARDS", null);
        return c;
    }

    public Cursor getparticularcarddetails(String id) {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM CARDS WHERE _id = '"+id+"'", null);
        return c;
    }

    public Cursor deletecarddetails(String id) {
        Cursor c = null;
        c = sdb.rawQuery("DELETE FROM CARDS WHERE _id = '"+id+"'", null);
        return c;
    }

    public Cursor checkcarddetails(String cardnumber) {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM CARDS WHERE card_number = '"+cardnumber+"'", null);
        return c;
    }

    /**
     * Banks details in Database
     */

    public void insertbankdetails(String bankname, String bankcode, String bankimages) {
        ContentValues cv = new ContentValues();
        cv.put("bank_name", bankname);
        cv.put("bank_code", bankcode);
        cv.put("bank_image", bankimages);
        sdb.insert("BANKS", null, cv);
    }

    public Cursor getbanks() {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM BANKS", null);
        return c;
    }

    public Cursor getbankdetailsbyname(String bankname) {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM BANKS WHERE bank_name = '"+bankname+"'", null);
        return c;
    }

    public Cursor getbankdetailsbycode(String bankcode) {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM BANKS WHERE bank_code = '"+bankcode+"'", null);
        return c;
    }
}
