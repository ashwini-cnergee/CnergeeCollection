package com.broadbandcollection.billing.obj;

import android.content.Context;
import android.location.LocationManager;

import java.util.List;

public class AppConstants1 {

	public static boolean  APP_OPEN=false;
	public static boolean GPS_AVAILABLE=false;
	public static boolean is_paymentpickup_running=false;
	public static boolean hasGPSDevice(Context context)
    {
        final LocationManager mgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if ( providers == null ) return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }
}
