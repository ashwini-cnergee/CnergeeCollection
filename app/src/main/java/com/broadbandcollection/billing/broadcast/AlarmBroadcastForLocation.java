package com.broadbandcollection.billing.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.broadbandcollection.billing.services.LocationSendService;


/**
 * Created by Jyoti on 12/18/2018.
 */

public class AlarmBroadcastForLocation extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String activity= intent.getStringExtra("activity");

        Intent i= new Intent(context, LocationSendService.class);
        i.putExtra("activity", activity);
        context.startService(i);
    }
}
