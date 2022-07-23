package com.broadbandcollection.billing.obj;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by Jyoti on 12/15/2018.
 */

public class LocationRecord implements KvmSerializable {

    String latitude;
    String longitude;
    String memberid;
    String date;
    String provider;
    String is_gps;
    String activity;

    public static String LocationObj="LocationOBJ";
    public static String ClientName="CliectAccessId";

    public LocationRecord(){}
    public LocationRecord(LocationRecord locationRecord){}
    public LocationRecord(String latitude ,String Longitude,String memberid,String date,String provider,
                          String is_gps ,String activity){
        this.latitude = latitude;
        this.longitude = Longitude;
        this.memberid = memberid;
        this.date = date;
        this.provider = provider;
        this.is_gps = is_gps;
        this.activity = activity;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        longitude = longitude;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getIs_gps() {
        return is_gps;
    }

    public void setIs_gps(String is_gps) {
        this.is_gps = is_gps;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return getLatitude();
            case 1:
                return getLongitude();
            case 2:
                return getMemberid();
            case 3:
                return getDate();
            case 4:
                return getProvider();
            case 5:
                return getIs_gps();
            case 6:
                return getActivity();
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 7;
    }

    @Override
    public void setProperty(int i, Object value) {
        switch (i) {
            case 0:
                latitude = value.toString();
                break;
            case 1:
                longitude = value.toString();
                break;

            case 2:
                memberid = value.toString();
                break;

            case 3:
                date = value.toString();
                break;

            case 4:
                provider = value.toString();
                break;
            case 5:
                is_gps = value.toString();
                break;
            case 6:
                activity = value.toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo info) {
        switch (i) {
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Latitude";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Longitude";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MemberId";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Date";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Provider";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Isgps";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Activity";
                break;
            default:
                break;
        }
    }
}
