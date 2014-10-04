package com.aviation.gareth.flighttracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Gareth on 12/07/2014.
 */
public class GooglePlayServices {

     public boolean IsGooglePlayAvailable(Context context)
    {
    /*  checks to see if google play services is available, if not then the user is
        alerted and provided with a link to the play store to get the service
    */
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if(status == ConnectionResult.SUCCESS)
        {
            return true;
        }
        else
        {
            ((Dialog) GooglePlayServicesUtil.getErrorDialog(status, (Activity) context, 10)).show();
        }
        return false;
    }

}
