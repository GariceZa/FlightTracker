package com.aviation.gareth.flighttracker;

import android.app.ListActivity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Gareth on 01/10/2014.
 */
public class ViewFlights extends ListActivity {

    //variables
    DBAdapter db = new DBAdapter(this);
    ArrayList<String> flights = new ArrayList<String>();
    //-------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flights);

        //removing the action bar icon
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        //---------------------------

        try{
            db.open();//connecting to the db
            flights = db.getFlights(this);//getting all saved flights
            //creating a listview
            setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,flights));
        }
        catch (Exception Err){
            Toast.makeText(getApplicationContext(), "Error: " + Err, Toast.LENGTH_LONG).show();
        }
        finally {

            db.close();//closing db connection
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewflightsmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.viewflightsback:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
