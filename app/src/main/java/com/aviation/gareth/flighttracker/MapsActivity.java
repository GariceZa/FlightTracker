package com.aviation.gareth.flighttracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements  android.location.LocationListener {

    //Variables
    private GoogleMap mMap;
    private String provider,startDate,endDate;
    private Location location;
    private LocationManager locMan;
    private List<LatLng> points = new ArrayList<LatLng>();
    boolean tracking = false,oneTimeZoom = false;
    TextView tvSpeed,tvBearing,tvAltitude,tvDistance;
    SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm"); //date format(day of week, dom, month,year time)
    //---------
    //Classes
    GooglePlayServices GPServices = new GooglePlayServices();
    LocationData LData = new LocationData();
    //---------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //removing the action bar icon
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        //---------------------------

        if(GPServices.IsGooglePlayAvailable(this))
        {
            setContentView(R.layout.activity_maps);
            setUpMapIfNeeded();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    /*
        Prepares the Screen's standard options menu to be displayed.
        This is called right before the menu is shown, every time it is shown.
        You can use this method to efficiently enable/disable items or otherwise dynamically modify the contents.
    */
        SharedPreferences MapSetting = getSharedPreferences("MapSetting",0);
        String mapType = MapSetting.getString("map","");

        if(mapType.equals("normal"))
        {
            menu.findItem(R.id.normal).setChecked(true);
        }
        else if(mapType.equals("satellite"))
        {
            menu.findItem(R.id.satellite).setChecked(true);
        }
        else if(mapType.equals("terrain"))
        {
            menu.findItem(R.id.terrain).setChecked(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences MapSetting = getSharedPreferences("MapSetting",0);
        SharedPreferences.Editor edit = MapSetting.edit();

        switch(item.getItemId())
        {
            case R.id.normal:

                item.setChecked(true);
                edit.putString("map","normal");
                edit.commit();
                mMap.setMapType(1);

                break;
            case R.id.satellite:

                item.setChecked(true);
                edit.putString("map","satellite");
                edit.commit();
                mMap.setMapType(4);


                break;
            case R.id.terrain:

                item.setChecked(true);
                edit.putString("map","terrain");
                edit.commit();
                mMap.setMapType(3);
                break;
            case R.id.viewFlights:

                startActivity(new Intent(this,ViewFlights.class));

                break;
            case R.id.StartStopTracking:

                if(!GPSEnabled())
                {
                    GPSDisabledAlert();
                }
                else
                {
                    if(!tracking)
                    {
                        tracking = true;
                        startDate = df.format(Calendar.getInstance().getTime());
                        item.setTitle("Stop");
                        Toast.makeText(getBaseContext(), "Tracking Started", Toast.LENGTH_SHORT).show();
                    }
                    else if(tracking)
                    {
                        tracking = false;
                        endDate = df.format(Calendar.getInstance().getTime());
                        item.setTitle("Start");
                        Toast.makeText(getBaseContext(), "Tracking Stopped", Toast.LENGTH_SHORT).show();
                        SaveDialog save = SaveDialog.newInstance();
                        save.show(getFragmentManager(),"dialog");
                    }
                }

                break;
            case R.id.preferences:

                startActivity(new Intent(this,Settings.class));

                break;
            case R.id.exit:

                ExitDialog EDF = ExitDialog.newInstance("Exit?");
                EDF.show(getFragmentManager(),"dialog");

                break;
            case R.id.legalnotices:

                startActivity(new Intent(this,GoogleLegalNotice.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng LL = new LatLng(location.getLatitude(), location.getLongitude());

        // zoom into the users location one time
        if(!oneTimeZoom)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LL,13));
            oneTimeZoom = true;

        }//if the user has clicked start
        if(tracking)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LL));
            drawTrack(LL);
            updateFlightInformation(location);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //do nothing
    }

    @Override
    public void onProviderEnabled(String provider) {
        //do nothing
    }

    @Override
    public void onProviderDisabled(String provider) {
        //do nothing
    }

    private void updateFlightInformation(Location location) {
        //updates flight information in fragment_flight_info
        tvSpeed.setText("Spd: "     + LData.getSpeed(location,this));
        tvBearing.setText("Brg: "   + LData.getBearing(location));
        tvAltitude.setText("Alt: "  + LData.getAltitude(location,this));
        tvDistance.setText("Dist: " + LData.getDistanceTravelled(location,this));
    }

    private void setUpMapIfNeeded() {
        // Do a  check to confirm that the map is not already instantiated
        if (mMap == null) {
            // obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if obtaining the map was successful
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        //set location manager,location & provider
        mMap.setMyLocationEnabled(true);
		locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
		provider = LocationManager.GPS_PROVIDER;
		location = locMan.getLastKnownLocation(provider);
		locMan.requestLocationUpdates(provider, 2500, 0, this);
        //-------------------------------------------------------
        //link textviews
        tvSpeed     =(TextView)findViewById(R.id.speed);
        tvBearing   =(TextView)findViewById(R.id.bearing);
        tvAltitude  =(TextView)findViewById(R.id.altitude);
        tvDistance = (TextView)findViewById(R.id.distance);
        //-------------------------------------------------------

        setMapType();


    }

    private void setMapType() {
    /*
        sets the map type according to the value stored in the shared preferences
    */
        SharedPreferences MapSetting = getSharedPreferences("MapSetting",0);
        String mapType = MapSetting.getString("map","");

        if(mapType.equals("normal"))
        {
            mMap.setMapType(1);
        }
        else if(mapType.equals("satellite"))
        {
            mMap.setMapType(4);
        }
        else if(mapType.equals("terrain"))
        {
            mMap.setMapType(3);
        }
    }

    private void drawTrack(LatLng point) {
    /*
        receives the new latitude/longitude co ordinate, adds it to the points arraylist
        and draws a line from the last position to the current position
    */
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);
            points.add(point);
            polylineOptions.addAll(points);
            mMap.addPolyline(polylineOptions);
    }

    private boolean GPSEnabled() {
        //checks if gps is enabled in the device settings
        LocationManager GPSEnabled = (LocationManager)getSystemService(LOCATION_SERVICE);
        return  GPSEnabled.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void GPSDisabledAlert(){

        /*
            create an alert dialog which can direct the user to the settings to turn
            gps on so that the app can perform tracking
         */
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled.Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent EnableGPSIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(EnableGPSIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void doPositiveClick() {
        //exit the application
        finish();
    }

    public void doNegativeClick() {
      //do nothing
    }

    public void doSave(String desc) {
        /*
            when the user clicks save in the save dialog
            connect to the db and insert new values
         */
        DBAdapter db = new DBAdapter(this);
        long insert;
        try{
            if(desc.trim().length() < 1){
                desc = "No Details entered";
            }
            db.open();
            insert = db.insertFlightInfo(desc,LData.totalDistance,startDate,endDate);
            if(insert==4){
                Toast.makeText(getApplicationContext(),"Flight Saved ",Toast.LENGTH_SHORT).show();
            }
        }catch(SQLException Err){
            Toast.makeText(getApplicationContext(),"Error Saving Flight: " + Err,Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
            clearFlightInfo();
        }

    }

    public void doNotSave() {
        clearFlightInfo();
    }

    private void clearFlightInfo(){
        //initializing distance data to 0
        LData.totalDistance = 0;
        LData.p2PDistance = 0;
        //-----------------------

        //clearing textviews
        tvSpeed.setText("Spd: ");
        tvBearing.setText("Brg: ");
        tvAltitude.setText("Alt: ");
        tvDistance.setText("Dist: ");
        //------------------
    }
}
