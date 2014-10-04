package com.aviation.gareth.flighttracker;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Gareth on 13/07/2014.
 */
public class GoogleLegalNotice extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlelegalnotice);

        //removing the action bar icon
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        //---------------------------

        // a text view that displays the google legal terms of use
        ((TextView)findViewById(R.id.legalNotice)).setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.legalnoticemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.back:

                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
