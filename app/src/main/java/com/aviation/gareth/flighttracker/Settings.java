package com.aviation.gareth.flighttracker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by Gareth on 25/09/2014.
 */
public class Settings extends Activity {

    //Variables
    public SharedPreferences settings;
    ToggleButton togKph,togMph,togKnots,togKm,togMiles,togNm,togMeters,togFeet;
    Boolean kph,mph,knots,km,miles,nm,meters,feet;
    //--------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //removing the action bar icon
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        //---------------------------

        initialize();
        getPreferences();
        setToggleButtons();

        //Setting speed preferences
        togKph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togKph.setEnabled(false);

                if(togMph.isChecked()){
                    togMph.setChecked(false);
                    togMph.setEnabled(true);
                }
                else if(togKnots.isChecked()){
                    togKnots.setChecked(false);
                    togKnots.setEnabled(true);
                }
                //saving preferences
                SharedPreferences.Editor edit = settings.edit();
                edit.putBoolean("speedKph",togKph.isChecked());
                edit.putBoolean("speedMph",togMph.isChecked());
                edit.putBoolean("speedKnots",togKnots.isChecked());
                edit.commit();
                //--------------------
            }
        });

        togMph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togMph.setEnabled(false);

                if(togKph.isChecked()){
                    togKph.setChecked(false);
                    togKph.setEnabled(true);
                }
                else if(togKnots.isChecked()){
                    togKnots.setChecked(false);
                    togKnots.setEnabled(true);
                }
                //saving preferences
                SharedPreferences.Editor edit = settings.edit();
                edit.putBoolean("speedKph",togKph.isChecked());
                edit.putBoolean("speedMph",togMph.isChecked());
                edit.putBoolean("speedKnots",togKnots.isChecked());
                edit.commit();
                //--------------------
            }
        });

        togKnots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togKnots.setEnabled(false);

                if(togKph.isChecked()){
                    togKph.setChecked(false);
                    togKph.setEnabled(true);
                }
                else if(togMph.isChecked()){
                    togMph.setChecked(false);
                    togMph.setEnabled(true);
                }
                //saving preferences
                SharedPreferences.Editor edit = settings.edit();
                edit.putBoolean("speedKph",togKph.isChecked());
                edit.putBoolean("speedMph",togMph.isChecked());
                edit.putBoolean("speedKnots",togKnots.isChecked());
                edit.commit();
                //--------------------
            }
        });
        //-------------------------

        //Setting distance preferences
        togKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togKm.setEnabled(false);

                if(togMiles.isChecked()){
                    togMiles.setChecked(false);
                    togMiles.setEnabled(true);
                }
                else if(togNm.isChecked()){
                    togNm.setChecked(false);
                    togNm.setEnabled(true);
                }
                //saving preferences
                SharedPreferences.Editor edit = settings.edit();
                edit.putBoolean("distanceKm",togKm.isChecked());
                edit.putBoolean("distanceMiles",togMiles.isChecked());
                edit.putBoolean("distanceNm",togNm.isChecked());
                edit.commit();
                //--------------------
            }
        });
        togMiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togMiles.setEnabled(false);

                if(togKm.isChecked()){
                    togKm.setChecked(false);
                    togKm.setEnabled(true);
                }
                else if(togNm.isChecked()){
                    togNm.setChecked(false);
                    togNm.setEnabled(true);
                }
                //saving preferences
                SharedPreferences.Editor edit = settings.edit();
                edit.putBoolean("distanceKm",togKm.isChecked());
                edit.putBoolean("distanceMiles",togMiles.isChecked());
                edit.putBoolean("distanceNm",togNm.isChecked());
                edit.commit();
                //--------------------
            }
        });
        togNm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togNm.setEnabled(false);

                if(togKm.isChecked()){
                    togKm.setChecked(false);
                    togKm.setEnabled(true);
                }
                else if(togMiles.isChecked()){
                    togMiles.setChecked(false);
                    togMiles.setEnabled(true);
                }
                //saving preferences
                SharedPreferences.Editor edit = settings.edit();
                edit.putBoolean("distanceKm",togKm.isChecked());
                edit.putBoolean("distanceMiles",togMiles.isChecked());
                edit.putBoolean("distanceNm",togNm.isChecked());
                edit.commit();
                //--------------------
            }
        });
        //-------------------------

        //Setting altitude preferences
        togMeters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togMeters.setEnabled(false);

                if(togFeet.isChecked()){
                    togFeet.setChecked(false);
                    togFeet.setEnabled(true);
                }
                //saving preferences
                SharedPreferences.Editor edit = settings.edit();
                edit.putBoolean("altitudeMeters",togMeters.isChecked());
                edit.putBoolean("altitudeFeet",togFeet.isChecked());
                edit.commit();
                //--------------------
            }
        });

        togFeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togFeet.setEnabled(false);

                if(togMeters.isChecked()){
                    togMeters.setChecked(false);
                    togMeters.setEnabled(true);
                }
                //saving preferences
                SharedPreferences.Editor edit = settings.edit();
                edit.putBoolean("altitudeMeters",togMeters.isChecked());
                edit.putBoolean("altitudeFeet",togFeet.isChecked());
                edit.commit();
                //--------------------
            }
        });
        //-------------------------
    }


    @Override
    //adding the preferences menu to the actionbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preferencesmenu, menu);
        return true;
    }

    @Override
    //if the back button on the menu is pressed then finish the activity
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.back:

                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize(){

        //linking toggle buttons to their corresponding resource
        togKph      = (ToggleButton)findViewById(R.id.togKph);
        togMph      = (ToggleButton)findViewById(R.id.togMph);
        togKnots    = (ToggleButton)findViewById(R.id.togKnots);
        togKm       = (ToggleButton)findViewById(R.id.togKm);
        togMiles    = (ToggleButton)findViewById(R.id.togMiles);
        togNm       = (ToggleButton)findViewById(R.id.togNm);
        togMeters   = (ToggleButton)findViewById(R.id.togMeters);
        togFeet     = (ToggleButton)findViewById(R.id.togFeet);
        //----------------------

    }
    //setting preference values
    private void getPreferences(){

        //using the preference manager to get the shared preference values
        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //setting default speed preferences
        kph     = settings.getBoolean("speedKph",false);
        mph     = settings.getBoolean("speedMph",false);
        knots   = settings.getBoolean("speedKnots",true);

        //setting default distance preferences
        km      = settings.getBoolean("distanceKm",false);
        miles   = settings.getBoolean("distanceMiles",false);
        nm      = settings.getBoolean("distanceNm",true);

        //setting default altitude preferences
        meters  = settings.getBoolean("altitudeMeters",false);
        feet    = settings.getBoolean("altitudeFeet",true);
    }
    //checking toggle buttons
    private void setToggleButtons(){
    //Speed
        togKph.setChecked(kph);

        if(kph.equals(true)){
            togKph.setEnabled(false);
        }

        togMph.setChecked(mph);

        if(mph.equals(true)){
            togMph.setEnabled(false);
        }

        togKnots.setChecked(knots);

        if(knots.equals(true)){
            togKnots.setEnabled(false);
        }
    //---------------------------

    //Distance
        togKm.setChecked(km);

        if(km.equals(true)){
            togKm.setEnabled(false);
        }

        togMiles.setChecked(miles);

        if(miles.equals(true)){
            togMiles.setEnabled(false);
        }

        togNm.setChecked(nm);

        if(nm.equals(true)){
            togNm.setEnabled(false);
        }
    //---------------------------

    //Altitude
        togMeters.setChecked(meters);

        if(meters.equals(true)){
            togMeters.setEnabled(false);
        }

        togFeet.setChecked(feet);

        if(feet.equals(true)){
            togFeet.setEnabled(false);
        }
    //---------------------------
    }
}
