package com.broadbandcollection.billing.activity;



import static com.broadbandcollection.billing.utils.Utils.PaymentMode;
import static com.broadbandcollection.billing.utils.Utils.PaymentURL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.broadbandcollection.billing.SOAP.AdditionalAmountDetailsSOAP;
import com.broadbandcollection.billing.SOAP.AdjustmentCaller;
import com.broadbandcollection.billing.activity.BaseApplication;
import com.broadbandcollection.billing.activity.CardActivity;
import com.broadbandcollection.billing.activity.CashActivity;
import com.broadbandcollection.billing.activity.ChequeActivity;
import com.broadbandcollection.billing.activity.RenewActivity;
import com.broadbandcollection.billing.activity.ShowDetailsActivity;
import com.broadbandcollection.billing.obj.AppConstants1;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PackageList;

import com.broadbandcollection.R;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;
import com.broadbandcollection.billing.utils.FinishEvent;
import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.ip.renewal.SOAP.CommonAsyncTask;
import com.broadbandcollection.ip.renewal.SOAP.CommonSOAP;


import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangePackage_NewActivity extends AppCompatActivity {
    RadioButton cashOption, chequeOption, edcOption;
    RadioButton renewOption, nowOption, adjOption;

    Spinner spinnerList;
    TextView price;

    private String updateFrom;

    public static String rslt = "";
    public static Map<String, PackageList> mapPackageList;
    public static String strXML = "";
    public static String adjStringVal = "";

    static String extStorageDirectory = Environment
            .getExternalStorageDirectory().toString();
    final static String xml_folder = "cnergee";
    final static String TARGET_BASE_PATH = extStorageDirectory + "/"
            + xml_folder + "/";

    public String xml_file_postFix = "PackageList.xml";
    public String xml_file;
    public String xml_file_with_path;

    //final static String xml_file = TARGET_BASE_PATH + "PackageList.xml";
    public static Context context;
    public static boolean isAdjOptionClick = false;

    String username;
    String selItem, AreaCode, AreaCodeFilter, CurrentPlan;
    String subscriberID, PackageListCode, PrimaryMobileNo, SecondryMobileNo,IsAutoReceipt,PaymentDate, DiscountPackRate,DiscountPackName,ConnectionTypeId;
    double OldPlanRate,NewPlanRate;
    //String previousPlanRate = "";
    AdditionalAmountDetailsSOAP additionalAmountDetailsSOAP;

    Utils utils = new Utils();
    AdjustmentWebService adjustmentWebService = null;

    public static final String backBundelPackage = "com.cnergee.billing.showdetails.screen.INTENT";
    public static final String currentBundelPackage = "com.cnergee.billing.changepackage.screen.INTENT";
    public static final String backBundelPackage1 = "com.cnergee.billing.search.screen.INTENT";

    BundleHelper bundleHelper;
    Bundle extras1;
    LocationManager locationManager;
    public static String getUpdateDataString="";
    AlertDialog alert ;
    AlertDialog.Builder   alertDialogBuilder;
    ProgressDialog prgDialog1;
    TableLayout tableLayoutadditional;
    String sel_package_id="";
    Double Oustanding_Add_Amt;
    TextView tv_Outstanding_Add_Amount;
    TextView tvLabel_outstanding,tv_package_name;
    Double NewPlanRate_send=0.0;
    String MemberId="",str_PaymentMode = "",str_URL;
    RadioButton rb_sms_pg,rb_time,rb_volume,rb_dd_pg,rb_upi_pg;
    RadioGroup rg_group,rg_time_volume;
    String pg_sms_renewal_type="";

    String PackageType="";
    String advance_type_for_volume_pkg="";
    Double PoolRate=0.0;

    PackageList selected_pkg;

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.runFinalizersOnExit(true);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mFinishSHowDetails);

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AppConstants1.APP_OPEN=true;
        if(AppConstants1.GPS_AVAILABLE){
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                //  Toast.makeText(SearchVendorActivity.this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
            }else{
                // showGPSDisabledAlertToUser();
            }
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if(alert!=null){
            if(alert.isShowing()){
                alert.dismiss();
            }
        }
        AppConstants1.APP_OPEN=false;

    }

    @SuppressWarnings("static-access")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change_packge);
        BaseApplication.getEventBus().register(this);

        selected_pkg = (PackageList) getIntent().getSerializableExtra("Selected_Pkg");

        rb_sms_pg=(RadioButton) findViewById(R.id.rb_sms_pg);
        rb_upi_pg = findViewById(R.id.rb_upi_pg);
        rb_dd_pg = findViewById(R.id.rb_dd_pg);
        SharedPreferences sharedPreferences12 = getApplicationContext()
                .getSharedPreferences("CNERGEE", 0);
        Utils.log("PG SMS", "is"+sharedPreferences12.getBoolean("sms_pg", false));
        if(!sharedPreferences12.getBoolean("sms_pg", false)){
            rb_sms_pg.setVisibility(View.GONE);
        }
        else{
            if(Utils.ACTION_TAKE.equalsIgnoreCase("Reactivate")){
                rb_sms_pg.setVisibility(View.GONE);
            }
        }

        if(AppConstants1.hasGPSDevice(this)){
            AppConstants1.GPS_AVAILABLE=true;
            //Toast.makeText(this, "Gps Available Main", Toast.LENGTH_SHORT).show();
        }
        else{
            AppConstants1.GPS_AVAILABLE=false;
            //Toast.makeText(this, "Gps Not Available Main", Toast.LENGTH_SHORT).show();
        }



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("GpsStatus"));


        LocalBroadcastManager.getInstance(this).registerReceiver(
                mFinishSHowDetails, new IntentFilter("finish_showdeatils"));
        mapPackageList = new HashMap<String, PackageList>();
        adjStringVal = "";
        isAdjOptionClick = false;

        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("CNERGEE", 0); // 0 - for private mode
        utils.setSharedPreferences(sharedPreferences);

        String	 AppVersion= Utils.GetAppVersion(ChangePackage_NewActivity.this);

        Authobj=new AuthenticationMobile();
        Authobj.setMobileNumber(utils.getMobileNumber());
        Authobj.setMobLoginId(utils.getMobLoginId());
        Authobj.setMobUserPass(utils.getMobUserPass());
        Authobj.setIMEINo(utils.getIMEINo());
        Authobj.setCliectAccessId(utils.getCliectAccessId());
        Authobj.setMacAddress(utils.getMacAddress());
        Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
        Authobj.setAppVersion(AppVersion);

        tv_Outstanding_Add_Amount=(TextView) findViewById(R.id.tv_Outstanding_Add_Amount);
        tvLabel_outstanding=(TextView)findViewById(R.id.tvLabel_outstanding);
        tv_package_name=(TextView)findViewById(R.id.tv_package_name);
        price = (TextView) findViewById(R.id.price);

       // tv_package_name.setText(selected_pkg.getPlanName());
       // price.setText(selected_pkg.getPackageRate());



        price.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(tableLayoutadditional.getVisibility()==View.VISIBLE){
                    tableLayoutadditional.setVisibility(View.GONE);
                }
                else{
                    tableLayoutadditional.setVisibility(View.VISIBLE);
                }
            }
        });
        context = this;
        extras1 = this.getIntent().getBundleExtra(backBundelPackage1);
        bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
        if (bundleHelper.getCurrentExtras() == null) {
            return;
        }
        tableLayoutadditional=(TableLayout)findViewById(R.id.tableLayoutadditional);
        tableLayoutadditional.setVisibility(View.GONE);
        username = bundleHelper.getCurrentExtras().getString("username");
        selItem = bundleHelper.getCurrentExtras().getString("selItem");
        MemberId= bundleHelper.getCurrentExtras().getString("MemberId");
        subscriberID = bundleHelper.getCurrentExtras().getString("subscriberID");
        OldPlanRate = bundleHelper.getCurrentExtras().getDouble("PlanRate");
        PackageListCode = bundleHelper.getCurrentExtras().getString("PackageListCode");
        PrimaryMobileNo = bundleHelper.getCurrentExtras().getString("PrimaryMobileNo");
        SecondryMobileNo = bundleHelper.getCurrentExtras().getString("SecondryMobileNo");
        AreaCode = bundleHelper.getCurrentExtras().getString("AreaCode");
        AreaCodeFilter = bundleHelper.getCurrentExtras().getString("AreaCodeFilter");
        CurrentPlan = bundleHelper.getCurrentExtras().getString("CurrentPlan");
        IsAutoReceipt = bundleHelper.getCurrentExtras().getString("IsAutoReceipt");
        PaymentDate = bundleHelper.getCurrentExtras().getString("PaymentDate");

        DiscountPackRate= bundleHelper.getCurrentExtras().getString("discountpackrate");
        DiscountPackName= bundleHelper.getCurrentExtras().getString("discountedPack");
        ConnectionTypeId=this.getIntent().getStringExtra("ConnectionTypeId");
        Oustanding_Add_Amt=this.getIntent().getDoubleExtra("Oustanding_Add_Amt", 0.0);
        PackageType= bundleHelper.getCurrentExtras().getString("PackageType");
        PoolRate=this.getIntent().getDoubleExtra("PoolRate", 0.0);

        setPlanDetails();
        price.setText(Double.toString(NewPlanRate));
        Log.e("price data",":"+price.getText().toString()+"  "+NewPlanRate);
        new GetAdditionalAmountAsyncTask().execute();

        if(Oustanding_Add_Amt==0.0){
            //tv_Outstanding_Add_Amount.setVisibility(View.GONE);
            //tvLabel_outstanding.setVisibility(View.GONE);
        }
        tv_Outstanding_Add_Amount.setText(""+Oustanding_Add_Amt);
        setAreaCode(AreaCode);
        setAreaCodeFilter(AreaCodeFilter);

        renewOption = (RadioButton) findViewById(R.id.radioRenew);
        nowOption = (RadioButton) findViewById(R.id.radioNow);
        adjOption = (RadioButton) findViewById(R.id.radioAdj);

        rg_group=(RadioGroup)findViewById(R.id.radioPayMode);

        rg_time_volume=(RadioGroup) findViewById(R.id.rg_time_volume);

        rb_time=(RadioButton) findViewById(R.id.rb_time);
        rb_volume=(RadioButton) findViewById(R.id.rb_volume);


        if(PackageType!=null &&PackageType.length()>0){

            if(PackageType.equalsIgnoreCase("Volume")){
                rg_time_volume.setVisibility(View.VISIBLE);
            }
            else{
                rg_time_volume.setVisibility(View.GONE);
            }
        }
        else{
            rg_time_volume.setVisibility(View.GONE);
        }

	/*	renewOption.setChecked(false);
		nowOption.setChecked(false);
		adjOption.setChecked(false);*/

        renewOption.setOnClickListener(updateFromOptionOnClickListener);
        nowOption.setOnClickListener(updateFromOptionOnClickListener);
        adjOption.setOnClickListener(updateFromOptionOnClickListener);

        cashOption = (RadioButton) findViewById(R.id.radioCash);
        chequeOption = (RadioButton) findViewById(R.id.radioCheque);
        edcOption = (RadioButton) findViewById(R.id.radioEDC);

        RadioGroup rg_group=(RadioGroup)findViewById(R.id.radioPayMode);

		/*rg_group.removeAllViews();

		rg_group.addView(cashOption);
		rg_group.addView(chequeOption);
		rg_group.addView(edcOption);
		rg_group.addView(rb_sms_pg);*/

        if(! Utils.ACCEPT_CHEQUE){
            chequeOption.setVisibility(View.GONE);
        }


        cashOption.setOnClickListener(paymentTypeOptionOnClickListener);
        chequeOption.setOnClickListener(paymentTypeOptionOnClickListener);
        edcOption.setOnClickListener(paymentTypeOptionOnClickListener);
        rb_dd_pg.setOnClickListener(paymentTypeOptionOnClickListener);
        rb_upi_pg.setOnClickListener(paymentTypeOptionOnClickListener);
        final Button btnBack = (Button) findViewById(R.id.backBtn);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

//				Intent intent = new Intent(ChangePackage_NewActivity.this,
//						ShowDetailsActivity.class);
//				intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
//				intent.putExtra(backBundelPackage1, extras1);
//				//intent.putExtra(backBundelPackage,bundleHelper.getBackExtras() );
//				startActivity(intent);
            }
        });
        setAuthenticationMobile();

        spinnerList = (Spinner) this.findViewById(R.id.planList);

        xml_file = ConnectionTypeId+"_"+AreaCode+"_"+xml_file_postFix;

		/*Log.i("******* XML FILE ******** ", xml_file);*/
        xml_file_with_path =  TARGET_BASE_PATH + xml_file;
		/*Log.i("******* XML FILE ******** ", xml_file_with_path);*/

//        if (isXMLFile()) {
//            setPackageList();
//            if(utils.isOnline(ChangePackage_NewActivity.this)){
//                new ChangePackage_NewActivity.GetPackageAsyncTask().execute();
//            }
//        } else {
//            packageListWebService = new ChangePackage_NewActivity.PackageListWebService();
//            packageListWebService.execute((String)null);
//        }

//        spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//
//				/*renewOption.setChecked(false);
//				nowOption.setChecked(false);
//				adjOption.setChecked(false);*/
//                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
//                radioGroup.clearCheck();
//
//                String selecte_plan = spinnerList.getSelectedItem().toString();
//                if(!selecte_plan.equalsIgnoreCase("Select Package")){
//                    setPlanDetails();
//                    price.setText(Double.toString(NewPlanRate));
//                    Log.e("price data",":"+price.getText().toString()+"  "+NewPlanRate);
//                    new ChangePackage_NewActivity.GetAdditionalAmountAsyncTask().execute();
//                }else{
//                    Toast.makeText(ChangePackage_NewActivity.this, "Please select valid package from the list", Toast.LENGTH_LONG).show();
//                    price.setText("0");
//                }
//            }
//
//            public void onNothingSelected(AdapterView<?> arg0) {
//                price.setText("0");
//            }
//        });

        rb_sms_pg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){

                    SharedPreferences sharedPreferences = getApplicationContext()
                            .getSharedPreferences("CNERGEE", 0);

                    String selecte_plan = spinnerList.getSelectedItem().toString();
                    if(selecte_plan.contains("Select Package")){
                        Toast.makeText(ChangePackage_NewActivity.this,
                                "Please select valid package from the list",
                                Toast.LENGTH_LONG).show();

                        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
                        radioGroup.clearCheck();

                    }
                    else{
                        boolean isOk = false;

                        if(renewOption.isChecked()){
                            isOk = true;
                        }else if(nowOption.isChecked()){
                            isOk = true;
                        }else if(adjOption.isChecked()){
                            isOk = true;
                        }else{
                            isOk = false;
                        }

                        if(!isOk){
                            Toast.makeText(ChangePackage_NewActivity.this,
                                    "Please select valid update from option",
                                    Toast.LENGTH_LONG).show();

                        }
                        else{
                            if(sharedPreferences.getBoolean("sms_pg", false)){
                                alertDialogBuilder  = new AlertDialog.Builder(ChangePackage_NewActivity.this);
                                final EditText et_mob_number=new EditText(ChangePackage_NewActivity.this);
                                if(PrimaryMobileNo!=null)
                                    et_mob_number.setText(PrimaryMobileNo);
                                alertDialogBuilder.setTitle("Confirmation");
                                alertDialogBuilder.setMessage("Please Confirm Mobile Number")
                                        //alertDialogBuilder.setMessage("Are you sure?")
                                        .setCancelable(false)

                                        .setPositiveButton("YES",
                                                new DialogInterface.OnClickListener(){
                                                    public void onClick(DialogInterface dialog, int id){
				            	/*rb_sms_pg.setChecked(false);
				            	if(et_mob_number.length()>0&&et_mob_number.length()==10)
				            		send_sms_pg_request(et_mob_number.getText().toString());
				            	else
				            		Toast.makeText(ChangePackage_NewActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();*/
                                                    }
                                                })
                                        .setNegativeButton("NO",
                                                new DialogInterface.OnClickListener(){
                                                    public void onClick(DialogInterface dialog, int id){
                                                        rb_sms_pg.setChecked(false);
                                                    }
                                                });
                                alertDialogBuilder.setView(et_mob_number);
                                alert  = alertDialogBuilder.create();
                                alert.show();

                                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {

                                        //Do stuff, possibly set wantToCloseDialog to true then...
                                        rb_sms_pg.setChecked(false);
                                        if(et_mob_number.length()>0&&et_mob_number.length()==10){
                                            alert.dismiss();
                                            send_sms_pg_request(et_mob_number.getText().toString());
                                        }
                                        else
                                            Toast.makeText(ChangePackage_NewActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                                    }
                                });
                            }
                            else{
                                alertDialogBuilder  = new AlertDialog.Builder(ChangePackage_NewActivity.this);
                                alertDialogBuilder.setMessage("Payment can not be made using this option.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener(){
                                                    public void onClick(DialogInterface dialog, int id){

                                                    }
                                                });

                                alert  = alertDialogBuilder.create();
                                alert.show();
                            }

                        }
                    }
                }
                else{

                }
            }

        });

        rb_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    advance_type_for_volume_pkg="dt";
                }
            }
        });

        rb_volume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    advance_type_for_volume_pkg="v";
                }
            }
        });
    }



    public class GetAdditionalAmountAsyncTask extends AsyncTask<String, Void, Void>{

        String additionalAmtResult;
        String additionalAmtResponse;
        String AppVersion;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog=new ProgressDialog(ChangePackage_NewActivity.this);
            progressDialog.show();
            AppVersion= Utils.GetAppVersion(ChangePackage_NewActivity.this);
            additionalAmountDetailsSOAP= new AdditionalAmountDetailsSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), utils.getDynamic_Url(), getString(R.string.METHOD_ADDITONAL_AMOUNT));

        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            AuthenticationMobile Authobj = new AuthenticationMobile();
            Authobj.setMobileNumber(utils.getMobileNumber());
            Authobj.setMobLoginId(utils.getMobLoginId());
            Authobj.setMobUserPass(utils.getMobUserPass());
            Authobj.setIMEINo(utils.getIMEINo());
            Authobj.setCliectAccessId(utils.getCliectAccessId());
            Authobj.setMacAddress(utils.getMacAddress());
            Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
            Authobj.setAppVersion(AppVersion);

            additionalAmtResult=additionalAmountDetailsSOAP.getAdditionalAmount(Authobj, subscriberID, utils.getAppUserName(),sel_package_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(additionalAmtResult.length()>0){
                if(additionalAmtResult.equalsIgnoreCase("OK")){

                    TextView tvPackRate=(TextView)findViewById(R.id.tvPackrate);
                    TextView tvAddAmtDetail=(TextView)findViewById(R.id.AddAmount);
                    TextView tvDiscount=(TextView)findViewById(R.id.DiscAmt);
                    TextView tvPayable=(TextView)findViewById(R.id.TotalPayAmt);
                    TextView tvFineAmt=(TextView)findViewById(R.id.FineAmt);
                    TextView tvDaysFineAmt=(TextView)findViewById(R.id.DaysFineAmt);
                    TableRow tableRowPack=(TableRow)findViewById(R.id.tableRowPackage);
                    TableRow tableRowAddtAmount=(TableRow)findViewById(R.id.tableRowAddtAmount);
                    TableRow tableRowDiscount=(TableRow)findViewById(R.id.tableRowDiscount);
                    TableRow tableFine=(TableRow)findViewById(R.id.tableFine);
                    TableRow tableTotal=(TableRow)findViewById(R.id.tableTotal);
                    TableRow tableDaysFineAmt=(TableRow)findViewById(R.id.tableDaysFineAmt);
                    if(additionalAmountDetailsSOAP.getPayableAmt()!=null)

                        NewPlanRate=Double.valueOf(additionalAmountDetailsSOAP.getPayableAmt());
                    if(additionalAmountDetailsSOAP.getPayableAmt()!=null)
                        NewPlanRate_send=Double.valueOf(additionalAmountDetailsSOAP.getPayableAmt());

                    tv_package_name.setText(selected_pkg.getPlanName());
                    price.setText(additionalAmountDetailsSOAP.getPayableAmt()+ " (Click Here for Details)");

                    // dialog.setTitle("Amount Details");
                    if(additionalAmountDetailsSOAP.getPice()!=null){
                        if(additionalAmountDetailsSOAP.getPice().equalsIgnoreCase("0")){
                            tableRowPack.setVisibility(View.GONE);
                        }
                        else{
                            tvPackRate.setText(additionalAmountDetailsSOAP.getPice());
                        }
                    }
                    if(additionalAmountDetailsSOAP.getAdditionalAmt()!=null){
                        if(additionalAmountDetailsSOAP.getAdditionalAmt().equalsIgnoreCase("0")){
                            tableRowAddtAmount.setVisibility(View.GONE);
                        }
                        else{
                            tvAddAmtDetail.setText(additionalAmountDetailsSOAP.getAdditionalAmt());
                        }
                    }
                    if(additionalAmountDetailsSOAP.getDisount_amt()!=null){
                        if(additionalAmountDetailsSOAP.getDisount_amt().equalsIgnoreCase("0")){
                            tableRowDiscount.setVisibility(View.GONE);
                        }
                        else{
                            tvDiscount.setText(additionalAmountDetailsSOAP.getDisount_amt());
                        }
                    }
                    if(additionalAmountDetailsSOAP.getFine_amt()!=null){
                        if(additionalAmountDetailsSOAP.getFine_amt().equalsIgnoreCase("0")){
                            tableFine.setVisibility(View.GONE);
                        }
                        else{
                            tvFineAmt.setText(additionalAmountDetailsSOAP.getFine_amt());
                        }
                    }

                    if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
                        if(additionalAmountDetailsSOAP.getPayableAmt().equalsIgnoreCase("0")){
                            tableTotal.setVisibility(View.GONE);
                        }
                        else{
                            tvPayable.setText(additionalAmountDetailsSOAP.getPayableAmt());
                        }
                    }

                    if(additionalAmountDetailsSOAP.getDays_Fine_Amt()!=null){
                        if(additionalAmountDetailsSOAP.getDays_Fine_Amt().equalsIgnoreCase("0")){
                            tableDaysFineAmt.setVisibility(View.GONE);
                        }
                        else{
                            tvDaysFineAmt.setText(additionalAmountDetailsSOAP.getDays_Fine_Amt());
                        }
                    }
                    // dialog.setContentView(v);

                    // dialog.show();
                }
            }
        }
    }


    public void send_sms_pg_request(String MobileNumber){

        CommonSOAP commonSOAP= new CommonSOAP(
                utils.getDynamic_Url(),
                getApplicationContext().getResources().getString(
                        R.string.WSDL_TARGET_NAMESPACE), getApplicationContext()
                .getResources().getString(
                        R.string.METHOD_SEND_PG_SMS));

        SoapObject request = new SoapObject(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.METHOD_SEND_PG_SMS));

        PropertyInfo pi = new PropertyInfo();
        pi.setName("UserLoginName");
        pi.setValue(utils.getAppUserName());
        pi.setType(String.class);
        request.addProperty(pi);
        commonSOAP.setRequest(request);

        pi = new PropertyInfo();
        pi.setName(AuthenticationMobile.AuthName);
        pi.setValue(Authobj);
        pi.setType(Authobj.getClass());
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MemberId");
        pi.setValue(MemberId);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MemberLoginId");
        pi.setValue(subscriberID);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("MobileNumber");
        pi.setValue(MobileNumber);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("PackageName");
        pi.setValue(str_planname);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("RenewalType");
        pi.setValue(pg_sms_renewal_type);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Amount");
        pi.setValue(NewPlanRate_send);
        pi.setType(String.class);
        request.addProperty(pi);



        commonSOAP.setRequest(request);

        final ProgressDialog prgDialog= new ProgressDialog(ChangePackage_NewActivity.this);
        prgDialog.setMessage("Please wait..");
        prgDialog.setCancelable(false);
        prgDialog.show();
        new CommonAsyncTask(ChangePackage_NewActivity.this){


            @Override
            protected void onPostExecute(ArrayList<String> result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                prgDialog.dismiss();
                if(result.get(0).equalsIgnoreCase("OK")){
                    String response_sms=result.get(1);
                    if(response_sms.length()>0){
                        if(response_sms.contains("#")){
                            String[] res_array=response_sms.split("#");
                            if(res_array.length>0){
                                if(res_array[1].equalsIgnoreCase("1")){

                                    alertDialogBuilder  = new AlertDialog.Builder(ChangePackage_NewActivity.this);
                                    alertDialogBuilder.setMessage(res_array[0])
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener(){
                                                        public void onClick(DialogInterface dialog, int id){
                                                            ChangePackage_NewActivity.this.finish();
                                                            BaseApplication.getEventBus().post(new FinishEvent(RenewActivity.class.getSimpleName()));
                                                            BaseApplication.getEventBus().post(new FinishEvent(ShowDetailsActivity.class.getSimpleName()));
                                                        }
                                                    });

                                    alert  = alertDialogBuilder.create();
                                    alert.show();

                                }
                                else{
                                    rb_sms_pg.setChecked(false);
                                    alertDialogBuilder  = new AlertDialog.Builder(ChangePackage_NewActivity.this);
                                    alertDialogBuilder.setMessage(res_array[0])
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener(){
                                                        public void onClick(DialogInterface dialog, int id){
                                                            ChangePackage_NewActivity.this.finish();
                                                            BaseApplication.getEventBus().post(new FinishEvent(RenewActivity.class.getSimpleName()));
                                                            BaseApplication.getEventBus().post(new FinishEvent(ShowDetailsActivity.class.getSimpleName()));
                                                        }
                                                    });

                                    alert  = alertDialogBuilder.create();
                                    alert.show();
                                }
                            }
                            else{
                                rb_sms_pg.setChecked(false);
                                alertDialogBuilder  = new AlertDialog.Builder(ChangePackage_NewActivity.this);
                                alertDialogBuilder.setMessage(response_sms)
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener(){
                                                    public void onClick(DialogInterface dialog, int id){

                                                    }
                                                });

                                alert  = alertDialogBuilder.create();
                                alert.show();
                            }
                        }
                        else{
                            rb_sms_pg.setChecked(false);
                        }
                    }
                    else{
                        rb_sms_pg.setChecked(false);
                    }
                }
                else{
                    rb_sms_pg.setChecked(false);
                    AlertsBoxFactory.showAlert("Web Service Not Executed", ChangePackage_NewActivity.this);
                }
            }
        }.execute(commonSOAP);


    }


    /**
     * @return the areaCode
     */
    public String getAreaCode() {
        return AreaCode;
    }

    /**
     * @param areaCode the areaCode to set
     */
    public void setAreaCode(String areaCode) {
        AreaCode = areaCode;
    }

    /**
     * @return the areaCodeFilter
     */
    public String getAreaCodeFilter() {
        return AreaCodeFilter;
    }

    /**
     * @param areaCodeFilter the areaCodeFilter to set
     */
    public void setAreaCodeFilter(String areaCodeFilter) {
        AreaCodeFilter = areaCodeFilter;
    }

    private AuthenticationMobile Authobj = null;

    private AuthenticationMobile getAuthenticationMobile(){
        return Authobj;
    }
    private AuthenticationMobile setAuthenticationMobile(){

        Authobj = new AuthenticationMobile();
        Authobj.setMobileNumber(utils.getMobileNumber());
        Authobj.setMobLoginId(utils.getMobLoginId());
        Authobj.setMobUserPass(utils.getMobUserPass());
        Authobj.setIMEINo(utils.getIMEINo());
        Authobj.setCliectAccessId(utils.getCliectAccessId());
        Authobj.setMacAddress(utils.getMacAddress());
        Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
        Authobj.setAppVersion(Utils.GetAppVersion(ChangePackage_NewActivity.this));
        return Authobj;
    }
    
    View.OnClickListener updateFromOptionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           // String selecte_plan = spinnerList.getSelectedItem().toString();
            String selecte_plan = selected_pkg.getPlanName();
            if(selecte_plan.equalsIgnoreCase("Select Package")){
                Toast.makeText(ChangePackage_NewActivity.this,
                        "Please select valid package from the list",
                        Toast.LENGTH_LONG).show();
                renewOption.setChecked(false);
                nowOption.setChecked(false);
                adjOption.setChecked(false);
                return;
            }

            if (renewOption.isChecked()) {
                updateFrom = "S";
                isAdjOptionClick = false;
                setPlanDetails();
                price.setText(additionalAmountDetailsSOAP.getPayableAmt());
                pg_sms_renewal_type="R";

            } else if (nowOption.isChecked()) {
                updateFrom = "I";
                isAdjOptionClick = false;
                setPlanDetails();
                price.setText(additionalAmountDetailsSOAP.getPayableAmt());
                pg_sms_renewal_type="I";
            } else if (adjOption.isChecked()) {
                updateFrom = "I";
                adjustmentWebService = new AdjustmentWebService();
                adjustmentWebService.execute((String)null);
                pg_sms_renewal_type="A";
            }
        }
    };
    
    View.OnClickListener paymentTypeOptionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           // String selecte_plan = spinnerList.getSelectedItem().toString();
            String selecte_plan = selected_pkg.getPlanName();
            if(selecte_plan.contains("Select Package")){
                Toast.makeText(ChangePackage_NewActivity.this,
                        "Please select valid package from the list",
                        Toast.LENGTH_LONG).show();
				
			/*	cashOption.setChecked(false);
				chequeOption.setChecked(false);
				edcOption.setChecked(false);*/
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
                radioGroup.clearCheck();
				
			/*	renewOption.setChecked(false);
				nowOption.setChecked(false);
				adjOption.setChecked(false);*/

                return;
            }
            boolean isOk = false;
			/*
			RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
			int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
			String radioButtonSelected = "";
			 
			switch (checkedRadioButton) {
			  case R.id.renewOption : radioButtonSelected = "radiobutton1";
			                   	              break;
			  case R.id.nowOption : radioButtonSelected = "radiobutton2";
					                      break;
			  case R.id.radiobutton3 : radioButtonSelected = "radiobutton3";
					                      break;
			}*/

            if(renewOption.isChecked()){
                isOk = true;
            }else if(nowOption.isChecked()){
                isOk = true;
            }else if(adjOption.isChecked()){
                isOk = true;
            }else{
                isOk = false;
            }

            if(!isOk){
                Toast.makeText(ChangePackage_NewActivity.this,
                        "Please select valid update from option",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (chequeOption.isChecked()) {
                str_PaymentMode = "CQ";

                if(is_package_type_valid()){
                    if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
                        if(!isAdjOptionClick)
                            setPlanDetails();

                        if(NewPlanRate == 0){
                            AlertsBoxFactory.showAlert("Package price is zero.",context );
                            return;
                        }
                        Intent finishIntent = new Intent("finish_showdetails");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);

                        finish();
                        Intent intent = new Intent(ChangePackage_NewActivity.this,
                                ChequeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", "" + username);
                        bundle.putString("updatefrom", "" + updateFrom);
                        bundle.putString("MemberId",  MemberId);
                        bundle.putString("subscriberID", subscriberID);
                        bundle.putDouble("PlanRate",NewPlanRate_send);
                        bundle.putString("CurrentPlan", str_planname);
                        bundle.putString("PackageListCode", PackageListCode);
                        bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
                        bundle.putString("SecondryMobileNo", SecondryMobileNo);
                        bundle.putString("AreaCode", AreaCode);
                        bundle.putString("AreaCodeFilter", AreaCodeFilter);
                        bundle.putBoolean("isPlanchange", true);
                        bundle.putString("IsAutoReceipt", IsAutoReceipt);
                        bundle.putString("backAction", "CP");
                        bundle.putString("PaymentDate",PaymentDate);
                        bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);
                        
                        intent.putExtra(PaymentURL, getApplicationContext().getResources().getString(R.string.METHOD_SEND_CHEQUE_MEMBER_DATA));
                        intent.putExtra(PaymentMode, str_PaymentMode);
                        intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
                        intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
                        intent.putExtra("com.cnergee.billing.cheque.screen.INTENT", bundle);
                        intent.putExtra(backBundelPackage1, extras1);
                        intent.putExtra("ConnectionTypeId", ConnectionTypeId);
                        intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
                        intent.putExtra("PoolRate", PoolRate);
                        Utils.log(""+this.getClass().getSimpleName(),""+Oustanding_Add_Amt);


                        startActivity(intent);
                    }
                    else{
                        AlertsBoxFactory.showAlert("Package price is zero.",context );
                        return;
                    }
                }
            } else if (edcOption.isChecked()) {
                if(is_package_type_valid()){
                    if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
                        if(!isAdjOptionClick)
                            setPlanDetails();

                        if(NewPlanRate == 0){
                            AlertsBoxFactory.showAlert("Package price is zero.",context );
                            return;
                        }
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("CNERGEE", 0);
                        //if(sharedPreferences.getBoolean("creditcard", true)){

                        //if(sharedPreferences.getBoolean("user_card_allow", true)){

                        Intent finishIntent = new Intent("finish_showdetails");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);

                        finish();
                        Intent intent = new Intent(ChangePackage_NewActivity.this, CardActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", "" + username);
                        bundle.putString("updatefrom", "" + updateFrom);
                        bundle.putString("MemberId",  MemberId);
                        bundle.putString("subscriberID", subscriberID);
                        bundle.putDouble("PlanRate",NewPlanRate_send);
                        bundle.putString("CurrentPlan", str_planname);
                        bundle.putString("PackageListCode", PackageListCode);
                        bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
                        bundle.putString("SecondryMobileNo", SecondryMobileNo);
                        bundle.putString("AreaCode", AreaCode);
                        bundle.putString("AreaCodeFilter", AreaCodeFilter);
                        bundle.putBoolean("isPlanchange", true);
                        bundle.putString("IsAutoReceipt", IsAutoReceipt);
                        bundle.putString("backAction", "CP");
                        bundle.putString("PaymentDate",PaymentDate);
                        bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);

                        intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
                        intent.putExtra("com.cnergee.billing.card.screen.INTENT", bundle);
                        intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
                        intent.putExtra(backBundelPackage1, extras1);
                        intent.putExtra("ConnectionTypeId", ConnectionTypeId);
                        intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
                        intent.putExtra("PoolRate", PoolRate);


                        startActivity(intent);
					/*}
					else{
						
						 alertDialogBuilder  = new AlertDialog.Builder(ChangePackage_NewActivity.this);
					        alertDialogBuilder.setMessage("Payment can not be made using this option.")
					        .setCancelable(false)
					        .setPositiveButton("OK",
					                new DialogInterface.OnClickListener(){
					            public void onClick(DialogInterface dialog, int id){
					               
					            }
					        });
					       
					        alert  = alertDialogBuilder.create();
					        alert.show();
					}*/
				/*}
				else{
					
					 alertDialogBuilder  = new AlertDialog.Builder(ChangePackage_NewActivity.this);
				        alertDialogBuilder.setMessage("Payment can not be made using this option.")
				        .setCancelable(false)
				        .setPositiveButton("OK",
				                new DialogInterface.OnClickListener(){
				            public void onClick(DialogInterface dialog, int id){
				               
				            }
				        });
				       
				        alert  = alertDialogBuilder.create();
				        alert.show();
				}*/
                    }
                    else{
                        AlertsBoxFactory.showAlert("Package price is zero.",context );
                        return;
                    }
                }
            } else if (cashOption.isChecked()) {
                if(is_package_type_valid()){
                    if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
                        if(!isAdjOptionClick)
                            setPlanDetails();

                        if(NewPlanRate == 0){
                            AlertsBoxFactory.showAlert("Package price is zero.",context );
                            return;
                        }

                        Intent finishIntent = new Intent("finish_showdetails");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);
                        finish();
                        Intent intent = new Intent(ChangePackage_NewActivity.this,
                                CashActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("username", "" + username);
                        bundle.putString("updatefrom", "" + updateFrom);
                        bundle.putString("MemberId",  MemberId);
                        bundle.putString("subscriberID", subscriberID);
                        bundle.putDouble("PlanRate", NewPlanRate_send);
                        bundle.putString("CurrentPlan", str_planname);
                        bundle.putString("PackageListCode", PackageListCode);
                        bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
                        bundle.putString("SecondryMobileNo", SecondryMobileNo);
                        bundle.putString("AreaCode", AreaCode);
                        bundle.putString("AreaCodeFilter", AreaCodeFilter);
                        bundle.putBoolean("isPlanchange", true);
                        bundle.putString("IsAutoReceipt", IsAutoReceipt);
                        bundle.putString("backAction", "CP");
                        bundle.putString("PaymentDate",PaymentDate);
                        bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);
                        //bundle.putString("ConnectionTypeId",ConnectionTypeId );
                        intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
                        intent.putExtra("com.cnergee.billing.cash.screen.INTENT", bundle);
                        intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
                        intent.putExtra(backBundelPackage1, extras1);
                        intent.putExtra("ConnectionTypeId", ConnectionTypeId);
                        intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
                        intent.putExtra("PoolRate", PoolRate);
                        startActivity(intent);
                    }
                }
                else{
                    AlertsBoxFactory.showAlert("Package price is zero.",context );
                    return;
                }
            }
            else if (rb_dd_pg.isChecked()) {
                str_PaymentMode = "DD";
                if(is_package_type_valid()){
                    if(additionalAmountDetailsSOAP.getPayableAmt()!=null){
                        if(!isAdjOptionClick)
                            setPlanDetails();

                        if(NewPlanRate == 0){
                            AlertsBoxFactory.showAlert("Package price is zero.",context );
                            return;
                        }
                        Intent finishIntent = new Intent("finish_showdetails");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);
                        finish();

                        Intent intent = new Intent(ChangePackage_NewActivity.this,
                                ChequeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", "" + username);
                        bundle.putString("updatefrom", "" + updateFrom);
                        bundle.putString("MemberId",  MemberId);
                        bundle.putString("subscriberID", subscriberID);
                        bundle.putDouble("PlanRate",NewPlanRate_send);
                        bundle.putString("CurrentPlan", str_planname);
                        bundle.putString("PackageListCode", PackageListCode);
                        bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
                        bundle.putString("SecondryMobileNo", SecondryMobileNo);
                        bundle.putString("AreaCode", AreaCode);
                        bundle.putString("AreaCodeFilter", AreaCodeFilter);
                        bundle.putBoolean("isPlanchange", true);
                        bundle.putString("IsAutoReceipt", IsAutoReceipt);
                        bundle.putString("backAction", "CP");
                        bundle.putString("PaymentDate",PaymentDate);
                        bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);

                        intent.putExtra(PaymentMode, str_PaymentMode);
                        intent.putExtra(PaymentURL,"SendMemberDataCheque");
                        intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
                        intent.putExtra(backBundelPackage,  bundleHelper.getBackExtras());
                        intent.putExtra("com.cnergee.billing.cheque.screen.INTENT", bundle);
                        intent.putExtra(backBundelPackage1, extras1);
                        intent.putExtra("ConnectionTypeId", ConnectionTypeId);
                        intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
                        intent.putExtra("PoolRate", PoolRate);
                        Utils.log(""+this.getClass().getSimpleName(),""+Oustanding_Add_Amt);


                        startActivity(intent);
                    }
                    else{
                        AlertsBoxFactory.showAlert("Package price is zero.",context );
                        return;
                    }
                }
            }
            else if (rb_upi_pg.isChecked()) {
                str_PaymentMode = "UPI";
                if (is_package_type_valid()) {
                    if (additionalAmountDetailsSOAP.getPayableAmt() != null) {
                        if (!isAdjOptionClick)
                            setPlanDetails();

                        if (NewPlanRate == 0) {
                            AlertsBoxFactory.showAlert("Package price is zero.", context);
                            return;
                        }
                        Intent finishIntent = new Intent("finish_showdetails");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(finishIntent);

                        finish();

                        Intent intent = new Intent(ChangePackage_NewActivity.this,
                                ChequeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", "" + username);
                        bundle.putString("updatefrom", "" + updateFrom);
                        bundle.putString("MemberId", MemberId);
                        bundle.putString("subscriberID", subscriberID);
                        bundle.putDouble("PlanRate", NewPlanRate_send);
                        bundle.putString("CurrentPlan", str_planname);
                        bundle.putString("PackageListCode", PackageListCode);
                        bundle.putString("PrimaryMobileNo", PrimaryMobileNo);
                        bundle.putString("SecondryMobileNo", SecondryMobileNo);
                        bundle.putString("AreaCode", AreaCode);
                        bundle.putString("AreaCodeFilter", AreaCodeFilter);

                        bundle.putBoolean("isPlanchange", true);
                        bundle.putString("IsAutoReceipt", IsAutoReceipt);
                        bundle.putString("backAction", "CP");
                        bundle.putString("PaymentDate", PaymentDate);
                        bundle.putString("advance_type_for_volume_pkg", advance_type_for_volume_pkg);
                        intent.putExtra(PaymentMode, str_PaymentMode);
                        intent.putExtra(PaymentURL,"SendMemberDataUPI");
                        intent.putExtra(currentBundelPackage, bundleHelper.getCurrentExtras());
                        intent.putExtra(backBundelPackage, bundleHelper.getBackExtras());
                        intent.putExtra("com.cnergee.billing.cheque.screen.INTENT", bundle);
                        intent.putExtra(backBundelPackage1, extras1);
                        intent.putExtra("ConnectionTypeId", ConnectionTypeId);
                        intent.putExtra("Oustanding_Add_Amt", Oustanding_Add_Amt);
                        intent.putExtra("PoolRate", PoolRate);
                        Utils.log("" + this.getClass().getSimpleName(), "" + Oustanding_Add_Amt);


                        startActivity(intent);
                    } else {
                        AlertsBoxFactory.showAlert("Package price is zero.", context);
                        return;
                    }
                }
            }
        }
    };

    public boolean is_package_type_valid(){
        if(PackageType!=null&&PackageType.length()>0){
            if(PackageType.equalsIgnoreCase("VOLUME")){
                if(rb_time.isChecked()||rb_volume.isChecked()){
                    return true;
                }
                else{
                    AlertsBoxFactory.showAlert("Please select Advance type.", this);
                    return false;

                }
            }
            else{
                return true;
            }
        }
        else{
            return true;
        }
    }


    private String str_planname,str_planrate;
    private void setPlanDetails() {

        String selecte_plan = selected_pkg.getPlanName();

        PackageList packageList = selected_pkg;
        if (packageList != null) {

            str_planname = packageList.getPlanName();
            str_planrate = packageList.getPackageRate();
            sel_package_id=packageList.getPackageId();

            Log.e("setPlanDetails",":"+sel_package_id+" "+str_planname+" "+str_planrate);

            try{
                if(selecte_plan.equalsIgnoreCase(DiscountPackName))
                {
                    NewPlanRate = Double.parseDouble(DiscountPackRate);
                }
                else
                {
                    NewPlanRate = Double.parseDouble(packageList.getPackageRate());
                }
            }catch(NumberFormatException nu){
                NewPlanRate = 0;
            }

        } else {
            str_planname = CurrentPlan;
            //str_planrate = PlanRate + "";
            NewPlanRate = 0;
        }
        //Utils.log("New Plan","Rate: "+NewPlanRate);
    }

    private class AdjustmentWebService extends AsyncTask<String, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(
                ChangePackage_NewActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Please Wait.. Recalulate Price.");
            Dialog.show();
            Dialog.setCancelable(false);
        }
        @Override
        protected void onCancelled() {
            Dialog.dismiss();
            adjustmentWebService = null;

        }
        protected void onPostExecute(Void unused) {

            Dialog.dismiss();
            adjustmentWebService = null;

            if (rslt.trim().equalsIgnoreCase("ok")) {
                try {
                    NewPlanRate_send = Double.parseDouble(adjStringVal);

                    price.setText(Double.toString(NewPlanRate_send));
                    isAdjOptionClick = true;
                } catch (NumberFormatException nue) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
                    radioGroup.clearCheck();
                    if(adjStringVal.equalsIgnoreCase("anyType{}")){
                        AlertsBoxFactory.showAlert("Conversion is not possible.", context);
                    }else{
                        AlertsBoxFactory.showAlert(adjStringVal, context);
                    }
                }

            } else {
                AlertsBoxFactory.showAlert("Invalid web-service response "
                        + rslt, context);
            }
            this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                AdjustmentCaller adjCaller = new AdjustmentCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        utils.getDynamic_Url(),
                        getApplicationContext().getResources().getString(
                                R.string.METHOD_GET_PACKAGE_ADJUSTMENT_AMOUNT),getAuthenticationMobile());
                adjCaller.setUsername(username);
                setPlanDetails();
                adjCaller.setNewPlan(str_planname);
                adjCaller.setAreaCode(AreaCode);
                adjCaller.setAreaCodeFilter(AreaCodeFilter);
                adjCaller.setSubscriberID(subscriberID);

                adjCaller.join();
                adjCaller.start();
                rslt = "START";

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }

            } catch (Exception e) {
                AlertsBoxFactory.showErrorAlert("Error web-service response "
                        + e.toString(), context);
            }
            return null;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Utils.log("Service","Message");
            //  ... react to local broadcast message
            if(AppConstants1.GPS_AVAILABLE){
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    if(alert!=null)
                        alert.dismiss();
                }
                else{
                    // showGPSDisabledAlertToUser();
                }
            }
        }
    };

    private BroadcastReceiver mFinishSHowDetails = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Utils.log("Service","Message");
            //  ... react to local broadcast message
            finish();


        }
    };

}
