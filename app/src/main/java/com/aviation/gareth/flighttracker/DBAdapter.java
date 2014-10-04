package com.aviation.gareth.flighttracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Gareth on 30/09/2014.
 */
public class DBAdapter {

    //Variables
    static final String KEY_ROWID 			= "flightID";
    static final String KEY_DESCRIPTION 	= "flightDescription";
    static final String KEY_FLIGHTDISTANCE 	= "flightDistance";
    static final String KEY_STARTTIME 	    = "startTime";
    static final String KEY_ENDTIME 	    = "endTime";
    static final String DATABASE_NAME 		= "FlightInfoDB";
    static final String DATABASE_TABLE 		= "tbl_FlightInfo";
    static final int DATABASE_VERSION 		= 1;

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    //creates the table if it does not exist
    static final String DATABASE_CREATE = "CREATE TABLE tbl_FlightInfo(flightID Integer Primary Key Autoincrement  NOT NULL,flightDescription Text NOT NULL,flightDistance Double NOT NULL,startTime Text NOT NULL,endTime Text NOT NULL)";
    //-------------------------------

    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(DATABASE_CREATE);
            }catch(Exception Err){
                Log.e("SQL Error", "There has been an error creating the DB", Err);
            }
        }

        //if the db version is different then it will be upgraded
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("SQL Information","Upgrading db from version:" + DATABASE_VERSION + "to " + newVersion);

            try{
                db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
                onCreate(db);
            }catch(Exception Err){
                Log.e("SQL Error", "There has been an error updating the DB", Err);
            }
        }
    }

    //opens a db connection
    public DBAdapter open() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //closes a db connection
    public void close(){
        DBHelper.close();
    }

    //inserts saved values into the db
    public long insertFlightInfo(String flightDesc,Double dist,String startTime,String endTime){
        ContentValues insertValues = new ContentValues();

        insertValues.put(KEY_DESCRIPTION,flightDesc);
        insertValues.put(KEY_FLIGHTDISTANCE,dist);
        insertValues.put(KEY_STARTTIME,startTime);
        insertValues.put(KEY_ENDTIME,endTime);

        return db.insert(DATABASE_TABLE,null,insertValues);
    }

    //returns an array list of all flights
    public ArrayList<String>getFlights(Context context){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean km = sharedPrefs.getBoolean("distanceKm",false);
        boolean mi = sharedPrefs.getBoolean("distanceMiles",false);
        String desc,sTime,eTime,unit;
        Double dist;
        DecimalFormat decForm = new DecimalFormat("#.##");
        ArrayList<String> flights = new ArrayList<String>();

        try{
            Cursor cur = db.rawQuery("SELECT flightDescription,flightDistance,startTime,endTime FROM " + DATABASE_TABLE,null);
            //setting the distance unit based on the preference setting
            if(km){
                unit = "Km";
            }
            else if(mi){
                unit = "Mi";
            }
            else{
                unit = "Nm";
            }

            if(cur!=null){
                if(cur.moveToFirst()){
                    do{
                        desc = cur.getString(cur.getColumnIndex(KEY_DESCRIPTION));
                        dist = cur.getDouble(cur.getColumnIndex(KEY_FLIGHTDISTANCE));
                        //setting the distance value according to the users preference
                        if(km){
                            dist =  Double.valueOf(decForm.format(dist / 1000));
                        }
                        else if(mi){
                            dist =  Double.valueOf(decForm.format(dist / 1609.344));
                        }
                        else{
                            dist =  Double.valueOf(decForm.format(dist / 1852));
                        }
                        sTime = cur.getString(cur.getColumnIndex(KEY_STARTTIME));
                        eTime = cur.getString(cur.getColumnIndex(KEY_ENDTIME));

                        flights.add(desc + "\n" + "Departed: " + sTime + "\n" + "Arrived: " + eTime + "\n" +  dist + " " + unit);

                    }while (cur.moveToNext());
                }
            }
        }
        catch(SQLiteException Err){
            Log.e(getClass().getSimpleName(), "Could not open the database");
        }

        return flights;
    }
}
