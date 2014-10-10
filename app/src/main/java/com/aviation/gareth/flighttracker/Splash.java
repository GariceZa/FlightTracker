package com.aviation.gareth.flighttracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Gareth on 25/09/2014.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //creating a new thread
        Thread timer = new Thread() {

            //Overriding the run method
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException Err) {
                    Err.printStackTrace();
                } finally{
                    Intent startMaps = new Intent(".MapsActivity");
                    startActivity(startMaps);
                }
            }
        };

        //when the thread starts it will sleep for 3 secs and then the MapsActivity will load
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finishes the activity when the startMaps intent is started
        finish();
    }
}

