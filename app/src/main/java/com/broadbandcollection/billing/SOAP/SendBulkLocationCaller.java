package com.broadbandcollection.billing.SOAP;

import android.content.Context;

import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.services.LocationSendService;

import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by Jyoti on 12/19/2018.
 */

public class SendBulkLocationCaller extends Thread {

    private String WSDL_TARGET_NAMESPACE;
    private String SOAP_URL;
    private String METHOD_NAME;
    AuthenticationMobile authObj;
    Context ctx;

    SendBulkLocationSOAP sendLocationSOAP;
    public String username,latitude,longitude,member_id,date,provider,is_gps,activity;

    public SendBulkLocationCaller(String WSDL_TARGET_NAMESPACE, String SOAP_URL, String METHOD_NAME, AuthenticationMobile Authobj,
                                  Context context,String latitude,String longitude,String member_id,String date,String provider,String is_gps,String activity) {
        // TODO Auto-generated constructor stub
        this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
        this.SOAP_URL = SOAP_URL;
        this.METHOD_NAME = METHOD_NAME;
        this.authObj=Authobj;
        this.ctx=context;
        this.latitude=latitude;
        this.longitude=longitude;
        this.member_id=member_id;
        this.date=date;
        this.provider=provider;
        this.is_gps=is_gps;
        this.activity=activity;
    }

    public void run(){
        try {
            sendLocationSOAP = new SendBulkLocationSOAP(WSDL_TARGET_NAMESPACE, SOAP_URL, METHOD_NAME);

            LocationSendService.rslt = sendLocationSOAP.CallSendBulkLocation(username, authObj,ctx,latitude,longitude,member_id,date,provider,is_gps,activity);

        }catch (SocketException e) {
            e.printStackTrace();
            LocationSendService.rslt = "Internet connection not available!!";
        }catch (SocketTimeoutException e) {
            e.printStackTrace();
            LocationSendService.rslt = "Internet connection not available!!";
        }catch (Exception e) {
            LocationSendService.rslt = "Invalid web-service response.<br>"+e.toString();
        }
    }
}
