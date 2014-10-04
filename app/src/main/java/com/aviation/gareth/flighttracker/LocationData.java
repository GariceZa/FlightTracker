package com.aviation.gareth.flighttracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;


/**
 * Created by Gareth on 04/08/2014.
 */
public class LocationData extends Activity {

    //variables
    LatLng newLatLng;
    LatLng oldLatLng;
    boolean flag = false;
    public double p2PDistance,totalDistance;
    Location newLoc = new Location("");
    Location oldLoc = new Location("");
    DecimalFormat oneDecFormat = new DecimalFormat("#.#");
    DecimalFormat bearingFormat = new DecimalFormat("###");
    public SharedPreferences sharedPrefs;
    //---------------------------------------

    //Gets the speed if it is available, in meters/second over ground and converts the value to the
    //users specified speed unit
    protected String getSpeed(Location location,Context context) {

        //variables
        double spd = 0;
        String unit="";
        //---------

        //obtaining users speed unit preference
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean speedKphVal = sharedPrefs.getBoolean("speedKph",false);
        boolean speedMphVal = sharedPrefs.getBoolean("speedMph",false);
        //------------------------------

        if(speedKphVal){
            unit = "kmh";
            spd =  location.getSpeed() * 3.6;
        }
        else if(speedMphVal){
            unit = "mph";
            spd = location.getSpeed() * 2.23694;
        }
        else{
            unit = "kts";
            spd = location.getSpeed() * 1.94384;
        }

        return Float.valueOf(oneDecFormat.format(spd)) + " " + unit;
    }

    //Gets the bearing, in degrees.
    protected int getBearing(Location location) {

        return Integer.valueOf(bearingFormat.format(location.getBearing()));
    }

    //Gets the altitude if available, in meters above sea level and converts the value to the users.
    // specified altitude unit
    protected String getAltitude(Location location,Context context) {

        //variables
        double alt=0;
        String unit="";
        //-----------

        //obtaining users altitude unit preference
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean altitudeMetersVal = sharedPrefs.getBoolean("altitudeMeters",false);
        //------------------------------

        if(altitudeMetersVal){
            unit = "m";
            alt = location.getAltitude();
        }
        else{
            unit = "ft";
            alt = location.getAltitude() * 3.28084;
        }


        return Float.valueOf(oneDecFormat.format(alt)) + " " + unit;
    }

    // Returns the approximate distance in meters between this location and the given location,
    // then converts the value to the users specified distance unit
    // Distance is defined using the WGS84 ellipsoid.
    protected String getDistanceTravelled(Location location,Context context) {

        //obtaining users distance unit preference
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean distanceKmVal = sharedPrefs.getBoolean("distanceKm",false);
        boolean distanceMilesVal = sharedPrefs.getBoolean("distanceMiles",false);
        //----------------------------------------

        String unit = "", totalDistanceTravelled = "";

        if(!flag){
            //set new lat/long
            newLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            flag = true;
            //initialize values
            p2PDistance = 0;
            totalDistance = 0;
        }
        else{
            //if flag == true set oldLatLng to newLatLngs previous value
            //set newLatLng to new value
            oldLatLng = new LatLng(newLatLng.latitude,newLatLng.longitude);
            newLatLng = new LatLng(location.getLatitude(),location.getLongitude());

            //set newLoc lat/lng
            newLoc.setLatitude(newLatLng.latitude);
            newLoc.setLongitude(newLatLng.longitude);

            //set oldLoc lat/lng
            oldLoc.setLatitude(oldLatLng.latitude);
            oldLoc.setLongitude(oldLatLng.longitude);

            //calculate distance between oldLoc and newLoc
            p2PDistance = oldLoc.distanceTo(newLoc);
            //add p2PDistance to totalDistance
            totalDistance += p2PDistance;

            //if the users preference is kilometers
            if(distanceKmVal){
               unit = "km";
               totalDistanceTravelled = String.valueOf(oneDecFormat.format(totalDistance / 1000));
            }//if the users preference is miles
            else if(distanceMilesVal){
                unit = "mi";
                totalDistanceTravelled = String.valueOf(oneDecFormat.format(totalDistance / 1609.344));
            }//if the users preference is nautical miles
           else{
                unit = "nm";
                totalDistanceTravelled = String.valueOf(oneDecFormat.format(totalDistance / 1852));
            }
        }
        //return a value with 2 decimal points
        return totalDistanceTravelled + " " + unit;
    }
}
