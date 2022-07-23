package com.broadbandcollection.billing.SOAP;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.broadbandcollection.billing.database.DatabaseHandler;
import com.broadbandcollection.billing.obj.Authentication;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.LocationRecord;
import com.broadbandcollection.billing.obj.MemberData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by Jyoti on 12/19/2018.
 */

public class SendBulkLocationSOAP {


    private String WSDL_TARGET_NAMESPACE;
    private String SOAP_URL;
    private String METHOD_NAME;
    public MemberData memberData;
    LocationRecord locationRecord;

    ArrayList<LocationRecord> al_location_record =new ArrayList<>();
    String latitude, Longitude, memberid,date,provider, is_gps, activity;

    LocationRecord[] location ;

    public SendBulkLocationSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
                            String METHOD_NAME) {
        this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
        this.SOAP_URL = SOAP_URL;
        this.METHOD_NAME = METHOD_NAME;
    }

    public String CallSendBulkLocation(String username,
                                       AuthenticationMobile Authobj, Context ctx, String latitude, String longitude,
                                       String memberid, String date, String provider, String is_gps, String activity)throws SocketException,SocketTimeoutException,Exception {
        try{
            String row_id;
            DatabaseHandler db= new DatabaseHandler(ctx);
            Cursor mCur=db.getLocationNew();

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
            PropertyInfo pi = new PropertyInfo();
            pi = new PropertyInfo();
            pi.setName(AuthenticationMobile.AuthName);
            pi.setValue(Authobj);
            pi.setType(Authobj.getClass());
            request.addProperty(pi);

            if(mCur.getCount()>0) {
                while (mCur.moveToNext()) {
                    Log.e("ROWID",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.ROW_ID)));
                    Log.e("LATITUDE",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.LATITUDE)));
                    Log.e("LONGITUDE",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.LONGITUDE)));
                    Log.e("MEMBER_ID",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.MEMBER_ID)));
                    Log.e("DATE",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.DATE)));
                    Log.e("PROVIDER",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.PROVIDER)));
                    Log.e("GPS_STATUS",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.GPS_STATUS)));
                    Log.e("ACTIVITY",":"+mCur.getString(mCur.getColumnIndex(DatabaseHandler.ACTIVITY)));

                    latitude = mCur.getString(mCur.getColumnIndex(DatabaseHandler.LATITUDE));
                    Longitude = mCur.getString(mCur.getColumnIndex(DatabaseHandler.LONGITUDE));
                    memberid = mCur.getString(mCur.getColumnIndex(DatabaseHandler.MEMBER_ID));
                    date = mCur.getString(mCur.getColumnIndex(DatabaseHandler.DATE));
                    provider = mCur.getString(mCur.getColumnIndex(DatabaseHandler.PROVIDER));
                    is_gps = mCur.getString(mCur.getColumnIndex(DatabaseHandler.GPS_STATUS));
                    activity = mCur.getString(mCur.getColumnIndex(DatabaseHandler.ACTIVITY));

                    locationRecord =new LocationRecord(latitude,Longitude,memberid,date,provider,is_gps,activity);
                    al_location_record.add(locationRecord);

                }
            }
            Log.e("Location Size",":"+al_location_record.size());
            location = new LocationRecord[al_location_record.size()];
            for (int j = 0; j < al_location_record.size(); j++) {
              location[j]= al_location_record.get(j);
            }

            SoapObject root = new SoapObject(WSDL_TARGET_NAMESPACE, "LocationRecord");
            for (LocationRecord i : location){
                root.addProperty("Location", i);
            }
            request.addSoapObject(root);
            Log.e("Array Size",":"+location.length);

            try{
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                envelope.encodingStyle = SoapSerializationEnvelope.ENC;
                envelope.implicitTypes = true;
                envelope.addMapping(WSDL_TARGET_NAMESPACE, "Authobj", new Authentication().getClass());

                envelope.addMapping(WSDL_TARGET_NAMESPACE, "Location", LocationRecord.class);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
                androidHttpTransport.debug = true;

                try{
                    androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);

                    Log.e("MultiLocationSOAP req",": "+androidHttpTransport.requestDump);
                    //Log.e("LocationSOAP response",": "+androidHttpTransport.responseDump);
                    //Utils.log("Envelope response","is: "+envelope.getResponse().toString());

                }catch(SoapFault e){
                    if (androidHttpTransport != null) {
                        androidHttpTransport.reset();
                        androidHttpTransport.getServiceConnection().disconnect();
                    }

                    return "error";
                }
                catch(Exception e){
                    if (androidHttpTransport != null) {
                        androidHttpTransport.reset();
                        androidHttpTransport.getServiceConnection().disconnect();
                    }
                    return "error";
                }
                catch(Error e){
                    if (androidHttpTransport != null) {
                        androidHttpTransport.reset();
                        androidHttpTransport.getServiceConnection().disconnect();
                    }
                    return "error";
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            int i=db.deleteAllRow();
            Log.e("delete Result",":"+i);
            al_location_record.clear();


           /* if(mCur.getCount()>0){
                while(mCur.moveToNext()){

                    SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);

                    PropertyInfo pi = new PropertyInfo();
                    pi.setName("UserLoginName");
                    pi.setValue(username);
                    pi.setType(String.class);
                    request.addProperty(pi);

                    row_id=mCur.getString(mCur
                            .getColumnIndex(DatabaseHandler.ROW_ID));

                    pi = new PropertyInfo();
                    pi.setName(AuthenticationMobile.AuthName);
                    pi.setValue(Authobj);
                    pi.setType(Authobj.getClass());
                    request.addProperty(pi);

                    pi= new PropertyInfo();
                    pi.setName("Latitude");
                    pi.setValue(mCur.getColumnIndex(DatabaseHandler.LATITUDE));
                    pi.setType(String.class);
                    request.addProperty(pi);

                    pi= new PropertyInfo();
                    pi.setName("Longitude");
                    pi.setValue(mCur.getColumnIndex(DatabaseHandler.LONGITUDE));
                    pi.setType(String.class);
                    request.addProperty(pi);

                    pi= new PropertyInfo();
                    pi.setName("LocationDate");
                    pi.setValue(date);
                    pi.setType(String.class);
                    request.addProperty(pi);

                    pi= new PropertyInfo();
                    pi.setName("LocationProvider");
                    pi.setValue(provider);
                    pi.setType(String.class);
                    request.addProperty(pi);

                    pi= new PropertyInfo();
                    pi.setName("GPSStatus");
                    pi.setValue(is_gps);
                    pi.setType(String.class);
                    request.addProperty(pi);

                    pi= new PropertyInfo();
                    pi.setName("Activity");
                    pi.setValue(activity);
                    pi.setType(String.class);
                    request.addProperty(pi);

                    pi= new PropertyInfo();
                    pi.setName("ForService");
                    pi.setValue("Collection");
                    pi.setType(String.class);
                    request.addProperty(pi);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);

                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    envelope.encodingStyle = SoapSerializationEnvelope.ENC;
                    envelope.implicitTypes = true;
                    envelope.addMapping(WSDL_TARGET_NAMESPACE, "Authobj",
                            new Authentication().getClass());

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
                    androidHttpTransport.debug = true;
                    try{
                        androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,envelope);

                        Log.e("LocationSOAP request",": "+androidHttpTransport.requestDump);
                        Log.e("LocationSOAP response",": "+androidHttpTransport.responseDump);
                        Utils.log("Envelope response","is: "+envelope.getResponse().toString());
                        if(envelope.getResponse().toString().equalsIgnoreCase("1")){
                            db.DeleteRow(row_id);
                        }else{

                        }

                    }catch(SoapFault e){
                        if (androidHttpTransport != null) {
                            androidHttpTransport.reset();
                            androidHttpTransport.getServiceConnection().disconnect();
                        }

                        return "error";
                    }
                    catch(Exception e){
                        if (androidHttpTransport != null) {
                            androidHttpTransport.reset();
                            androidHttpTransport.getServiceConnection().disconnect();
                        }
                        return "error";
                    }
                    catch(Error e){
                        if (androidHttpTransport != null) {
                            androidHttpTransport.reset();
                            androidHttpTransport.getServiceConnection().disconnect();
                        }
                        return "error";
                    }
                }

            }*/

            return "OK";
        }
        catch(Exception e){

            //	Utils.log("Main try","error"+e);
            return "error";
        }

    }

    private boolean isvalid;

    public void setIsValidUser(boolean isvalid) {
        this.isvalid = isvalid;
    }

    public boolean isValidUser() {
        return isvalid;
    }

}
