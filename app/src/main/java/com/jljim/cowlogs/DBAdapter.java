package com.jljim.cowlogs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Assessment purpose:  Record cow breed into a database
 * Assessment number:   2
 * Unit Coordinator:    Ron Balsys
 * Lecture & Tutor:     Partha Gangavalli
 * File name:           DBAdapter.java
 * File purpose:        DBAdapter to manage the database to add cows.
 * * Copyrights:        Make by Balsys (n.d.,https://moodle.cqu.edu.au/pluginfile.php/1625629/mod_resource/content/9/week5-13234.html) and modified by Jose Jimenez
 */

public class DBAdapter {
    // Columns names variables
    static final String KEY_COWID = "_id";
    static final String KEY_COW = "cow";
    static final String KEY_DATE = "Date";
    static final String KEY_WEIGHT = "Weight";
    static final String KEY_AGE = "Age";
    static final String KEY_CONDITION = "Condition";
    static final String KEY_LONGITUDE = "Longitude";
    static final String KEY_LATITUDE = "Latitude";

    // Database variables
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "CowLogs2DB";
    static final String DATABASE_TABLE = "contacts";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "create table contacts (_id integer primary key, "
                    + "cow text not null, date text not null, weight text not null, age text not null, condition text not null, longitude text not null, latitude text not null);";

    // Context to the database
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    // DBAdapter constructor
    public DBAdapter(Context ctx)
    {
        // add context in the class
        this.context = ctx;
        // Initialised the DatabaseHelper with the context
        DBHelper = new DatabaseHelper(context);
    }

    // class of the DatabaseHelper
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }
    //---insert a contact into the database---
    public long insertCow(CowLogs cowLog)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_COWID, Integer.parseInt(cowLog.getId()));
        initialValues.put(KEY_COW, cowLog.getBreed());
        initialValues.put(KEY_DATE, cowLog.getDate());
        initialValues.put(KEY_WEIGHT, cowLog.getWeight());
        initialValues.put(KEY_AGE, cowLog.getAge());
        initialValues.put(KEY_CONDITION, cowLog.getCondition());
        initialValues.put(KEY_LONGITUDE, cowLog.getLongitude());
        initialValues.put(KEY_LATITUDE, cowLog.getLatitude());
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    //---deletes a particular contact---
    public boolean deleteCow(long cowId)
    {
        return db.delete(DATABASE_TABLE, KEY_COWID + "=" + cowId, null) > 0;
    }
    //---retrieves all the contacts---
    public Cursor getAllCows()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_COWID, KEY_COW, KEY_DATE, KEY_WEIGHT, KEY_AGE, KEY_CONDITION, KEY_LONGITUDE, KEY_LATITUDE}, null, null, null, null, null);
    }
    //---retrieves a particular contact---
    public Cursor getCow(long cowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_COWID, KEY_COW, KEY_DATE, KEY_WEIGHT, KEY_AGE, KEY_CONDITION, KEY_LONGITUDE, KEY_LATITUDE}, KEY_COWID + "=" + cowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //---updates a contact---
    public boolean updateCow(long cowId, String cow, String date, String weight, String age, String condition, String longitude, String latitude)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_COW, cow);
        args.put(KEY_DATE, date);
        args.put(KEY_WEIGHT, weight);
        args.put(KEY_AGE, age);
        args.put(KEY_CONDITION, condition);
        args.put(KEY_LONGITUDE, longitude);
        args.put(KEY_LATITUDE, latitude);
        return db.update(DATABASE_TABLE, args, KEY_COWID + "=" + cowId, null) > 0;
    }
    //---deletes all entries---
    public boolean removeAll()
    {
        return db.delete(DATABASE_TABLE, null, null) >= 0;
    }
}