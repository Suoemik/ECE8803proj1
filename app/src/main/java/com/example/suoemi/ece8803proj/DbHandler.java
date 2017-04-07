package com.example.suoemi.ece8803proj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Suoemi on 3/29/2017.
 */

public class DbHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    // Database Name
    private static final String DATABASE_NAME = "appinfo";
    // Contacts table name
    public static final String TABLE_EV_Login = "driverlogininfo";
    public static final String TABLE_SELL_Login = "sellerlogininfo";
    // Contacts Table Columns names

    public static final String TABLE_ID = "id";
    public static final String COLUMN_NAME_PASS = "password";
    public static final String COLUMN_NAME_USER = "username";
    public static final String COLUMN_NAME_No = "number";
    public static final String COLUMN_NAME_eBID = "seller_bid";
    public static final String COLUMN_NAME_ePRICE = "seller_price";
    public static final String COLUMN_NAME_eVREQ = "ev_requirement";
    public static final String COLUMN_CURR_ACC = "current_account";


    public DbHandler(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String EV_Log_entries = "CREATE TABLE " + TABLE_EV_Login + "(" + TABLE_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_USER + " TEXT,"
                + COLUMN_NAME_PASS + " TEXT," + COLUMN_NAME_No + " TEXT," + COLUMN_NAME_eVREQ + " INTEGER," + COLUMN_NAME_ePRICE + " INTEGER," + COLUMN_CURR_ACC + " INTEGER)";

        String SELL_Log_entries = "CREATE TABLE " + TABLE_SELL_Login + "(" + TABLE_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_USER + " TEXT,"
                + COLUMN_NAME_PASS + " TEXT," + COLUMN_NAME_No + " TEXT," + COLUMN_NAME_eBID + " INTEGER," + COLUMN_NAME_ePRICE + " INTEGER," + COLUMN_CURR_ACC + " INTEGER)";

        db.execSQL(EV_Log_entries);
        db.execSQL(SELL_Log_entries);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EV_Login);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELL_Login);
        onCreate(db);
    }

    public void addEVLog(LoginData loginData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_USER, loginData.getUsername());
        values.put(COLUMN_NAME_PASS, loginData.getPassword());
        values.put(COLUMN_NAME_No, loginData.getNumber());
        values.put(COLUMN_NAME_eVREQ, loginData.getEVReq());
        values.put(COLUMN_NAME_ePRICE, loginData.getePrice());
        values.put(COLUMN_CURR_ACC, loginData.getCheck());

        db.insert(TABLE_EV_Login, null, values);
        String log = "Id: " + loginData.getId();
        Log.d("Username: ", TABLE_EV_Login);
        db.close();
    }

    public void addEVSell(LoginData loginData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values2 = new ContentValues();

        values2.put(COLUMN_NAME_PASS, loginData.getPassword());
        values2.put(COLUMN_NAME_USER, loginData.getUsername());
        values2.put(COLUMN_NAME_No, loginData.getNumber());
        values2.put(COLUMN_NAME_eBID, loginData.geteBid());
        values2.put(COLUMN_NAME_ePRICE, loginData.getePrice());
        values2.put(COLUMN_CURR_ACC, loginData.getCheck());

        db.insert(TABLE_SELL_Login, null, values2);
        db.close();
    }

    public LoginData getEVLog(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor choose = db.query(TABLE_EV_Login, new String[]{TABLE_ID,
                COLUMN_NAME_USER, COLUMN_NAME_PASS, COLUMN_NAME_No, COLUMN_NAME_eVREQ, COLUMN_NAME_ePRICE, COLUMN_CURR_ACC}, TABLE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if(choose != null)
            choose.moveToFirst();
        LoginData contact = new LoginData(choose.getInt(0),
                choose.getString(1), choose.getString(2), choose.getString(3), choose.getInt(4), choose.getInt(5), choose.getInt(6));
        return contact;
    }

    public LoginData getSellLog(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor choose = db.query(TABLE_SELL_Login, new String[]{TABLE_ID,
                        COLUMN_NAME_USER, COLUMN_NAME_PASS, COLUMN_NAME_No, COLUMN_NAME_eBID, COLUMN_NAME_ePRICE, COLUMN_CURR_ACC}, TABLE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if(choose != null)
            choose.moveToFirst();
        LoginData contact = new LoginData(choose.getInt(0),
                choose.getString(1), choose.getString(2), choose.getString(3), choose.getInt(4), choose.getInt(5), choose.getInt(6));
        return contact;
    }

    public List<LoginData> getAllEVLog(){
        List<LoginData> LoginDataList = new ArrayList<LoginData>();

        String selQuery = "SELECT * FROM " + TABLE_EV_Login;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor choose = db.rawQuery(selQuery, null);

        if(choose.moveToFirst()){
            do{
                LoginData loginData = new LoginData();

                loginData.setId(choose.getInt(0));
                loginData.setUsername(choose.getString(1));
                loginData.setPassword(choose.getString(2));
                loginData.setNumber(choose.getString(3));
                loginData.setEvReq(choose.getInt(4));
                loginData.setePrice(choose.getInt(5));
                loginData.setCheck(choose.getInt(6));

                LoginDataList.add(loginData);
            }
            while (choose.moveToNext());
        }
        return LoginDataList;
    }

    public List<LoginData> getAllSellLog(){
        List<LoginData> LoginDataList = new ArrayList<LoginData>();

        String selQuery = "SELECT * FROM " + TABLE_SELL_Login;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor choose = db.rawQuery(selQuery, null);

        if(choose.moveToFirst()){
            do{
                LoginData loginData = new LoginData();
                loginData.setId(choose.getInt(0));
                loginData.setUsername(choose.getString(1));
                loginData.setPassword(choose.getString(2));
                loginData.setNumber(choose.getString(3));
                loginData.seteBid(choose.getInt(4));
                loginData.setePrice(choose.getInt(5));
                loginData.setCheck(choose.getInt(6));

                LoginDataList.add(loginData);
            }
            while (choose.moveToNext());
        }
        return LoginDataList;
    }

    public int getEVLoginDataCount(){
        String countQuery = "SELECT * FROM " + TABLE_EV_Login;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor choose = db.rawQuery(countQuery, null);
        choose.close();

        return choose.getCount();
    }

    public int getSellLoginDataCount(){
        String countQuery = "SELECT * FROM " + TABLE_SELL_Login;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor choose = db.rawQuery(countQuery, null);
        choose.close();

        return choose.getCount();
    }

    public int updateEVLoginData(LoginData loginData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_PASS, loginData.getPassword());
        values.put(COLUMN_NAME_USER, loginData.getUsername());
        values.put(COLUMN_NAME_No, loginData.getNumber());
        values.put(COLUMN_NAME_eVREQ, loginData.getEVReq());
        values.put(COLUMN_NAME_ePRICE, loginData.getePrice());
        values.put(COLUMN_CURR_ACC, loginData.getCheck());

// updating row
        return db.update(TABLE_EV_Login, values, TABLE_ID + " = ?",
        new String[]{String.valueOf(loginData.getId())});
    }

    public int updateSellLoginData(LoginData loginData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USER, loginData.getUsername());
        values.put(COLUMN_NAME_PASS, loginData.getPassword());
        values.put(COLUMN_NAME_No, loginData.getNumber());
        values.put(COLUMN_NAME_eBID, loginData.geteBid());
        values.put(COLUMN_NAME_ePRICE, loginData.getePrice());
        values.put(COLUMN_CURR_ACC, loginData.getCheck());

        return db.update(TABLE_SELL_Login, values, TABLE_ID + " = ?",
                new String[]{String.valueOf(loginData.getId())});
    }

    public void deleteEVLog(LoginData loginData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EV_Login, TABLE_ID + " = ?",
        new String[] { String.valueOf(loginData.getId()) });
        db.close();
    }

    public void deleteSellLog(LoginData loginData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SELL_Login, TABLE_ID + " = ?",
                new String[] { String.valueOf(loginData.getId()) });
        db.close();
    }
}
