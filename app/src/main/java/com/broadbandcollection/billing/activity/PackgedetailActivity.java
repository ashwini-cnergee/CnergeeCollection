package com.broadbandcollection.billing.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.broadbandcollection.billing.SOAP.GetStatusSOAP;
import com.broadbandcollection.billing.SOAP.PackgeCaller;
import com.broadbandcollection.billing.adapter.CustomGridAdapter;
import com.broadbandcollection.billing.adapter.SpinnerDaysAdapter;
import com.broadbandcollection.billing.obj.AuthenticationMobile;
import com.broadbandcollection.billing.obj.PackageList;
import com.broadbandcollection.billing.utils.AlertsBoxFactory;
import com.broadbandcollection.billing.utils.BundleHelper;
import com.broadbandcollection.billing.utils.PackageListParsing;
import com.broadbandcollection.R;
import com.broadbandcollection.billing.utils.Utils;

import java.io.Serializable;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PackgedetailActivity extends Activity {

    Utils utils = new Utils();
    BundleHelper bundleHelper;
    PackageListWebService packageListWebService = null;
    public static String rslt = "";

    String username,MemberId="",PackageType="";
    String selItem, AreaCode, AreaCodeFilter, CurrentPlan;
    String subscriberID, PackageListCode, PrimaryMobileNo, SecondryMobileNo,IsAutoReceipt,PaymentDate, DiscountPackRate,DiscountPackName,ConnectionTypeId;
    double OldPlanRate,Oustanding_Add_Amt;
    Double PoolRate=0.0;
    public static String strXML = "";
    public static Map<String, PackageList> mapPackageList;
    HashMap<String,ArrayList<PackageList>> hm_packge_list = new HashMap<>();
    private int previousSelectedPosition = -1;
    boolean is_selected = true;

    public static final String backBundelPackage = "com.cnergee.billing.showdetails.screen.INTENT";
    public static final String currentBundelPackage = "com.cnergee.billing.changepackage.screen.INTENT";
    public static final String backBundelPackage1 = "com.cnergee.billing.search.screen.INTENT";

    Spinner planList;
    GridView gridview,gridview1;
    Context context;
    ProgressDialog prgDialog1;
    public static String getUpdateDataString="";
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packgedetail);
        context = this;
        planList=(Spinner)findViewById(R.id.planList);
        gridview=(GridView) findViewById(R.id.gridview);
        gridview1=(GridView) findViewById(R.id.gridview1);



        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("CNERGEE", 0); // 0 - for private mode
        utils.setSharedPreferences(sharedPreferences);

        String	 AppVersion= Utils.GetAppVersion(PackgedetailActivity.this);

        Authobj=new AuthenticationMobile();
        Authobj.setMobileNumber(utils.getMobileNumber());
        Authobj.setMobLoginId(utils.getMobLoginId());
        Authobj.setMobUserPass(utils.getMobUserPass());
        Authobj.setIMEINo(utils.getIMEINo());
        Authobj.setCliectAccessId(utils.getCliectAccessId());
        Authobj.setMacAddress(utils.getMacAddress());
        Authobj.setPhoneUniqueId(utils.getPhoneUniqueId());
        Authobj.setAppVersion(AppVersion);

        bundleHelper = new BundleHelper(this.getIntent(),backBundelPackage,currentBundelPackage);
        if (bundleHelper.getCurrentExtras() == null) {
            return;
        }

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
        //ConnectionTypeId= bundleHelper.getCurrentExtras().getString("ConnectionTypeId");
        ConnectionTypeId=this.getIntent().getStringExtra("ConnectionTypeId");
        Oustanding_Add_Amt=this.getIntent().getDoubleExtra("Oustanding_Add_Amt", 0.0);
        PackageType= bundleHelper.getCurrentExtras().getString("PackageType");
        PoolRate=this.getIntent().getDoubleExtra("PoolRate", 0.0);

        //TextView title = (TextView)findViewById(R.id.headerView);
        //title.setText(R.string.package_list);

        if(utils.isOnline(PackgedetailActivity.this)){
//            packageListWebService = new PackageListWebService();
//            packageListWebService.execute((String)null);

//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//                new PackageListWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }
//            else
//                new PackageListWebService().execute();
//
           // if (isXMLFile()) {
                if(utils.isOnline(PackgedetailActivity.this)){
                    new GetPackageAsyncTask().execute();

                }
//            } else {
//                packageListWebService = new PackageListWebService();
//                packageListWebService.execute((String)null);
//            }
        }

        planList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setGrid("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Search View");

        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }else{
            searchView.setIconified(false);
        }

    }

    public class GetPackageAsyncTask extends AsyncTask<String, Void, Void>{

        String getDataResult="";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            prgDialog1= new ProgressDialog(PackgedetailActivity.this);
            prgDialog1.setMessage("Please wait...");
            prgDialog1.setCancelable(false);
            prgDialog1.show();
            getUpdateDataString="";
        }
        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            Utils.log("Update ","Package");
            GetStatusSOAP getStatusSOAP= new GetStatusSOAP(
                    getApplicationContext().getResources().getString(
                            R.string.WSDL_TARGET_NAMESPACE),
                    utils.getDynamic_Url(), getApplicationContext()
                    .getResources().getString(
                            R.string.METHOD_GET_STATUS)
            );
            try {
                //Utils.log("UserLoginName","is: "+utils.getAppUserName());

                getDataResult=getStatusSOAP.getPackageSOAP(Authobj,utils.getAppUserName(),"S");
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            prgDialog1.dismiss();
            try{
                //Utils.log("getDataResult","is: "+getDataResult);
                if(getDataResult.length()>0){
                    if(getDataResult.equalsIgnoreCase("ok")){
                        if(getUpdateDataString.length()>0){
                            if(getUpdateDataString.equalsIgnoreCase("PackageUpdate")){
                                new PackageListWebService().execute();
                                new UpdatePackageAsyncTask().execute();
                            }
                        }else{
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                                new PackageListWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }else
                                new PackageListWebService().execute();
                        }
                    }
                    else{
                        //AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
                    }
                }
                else{
                    //AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
                }

            }catch(Exception e){

            }


        }
    }

    public class UpdatePackageAsyncTask extends AsyncTask<String, Void, Void>{

        String getDataResult="";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

        }
        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            GetStatusSOAP getStatusSOAP= new GetStatusSOAP(
                    getApplicationContext().getResources().getString(
                            R.string.WSDL_TARGET_NAMESPACE),
                    utils.getDynamic_Url(), getApplicationContext()
                    .getResources().getString(
                            R.string.METHOD_GET_STATUS)
            );
            try {
                //Utils.log("UserLoginName","is: "+utils.getAppUserName());

                getDataResult=getStatusSOAP.getPackageSOAP(Authobj,utils.getAppUserName(),"U");
            } catch (SocketTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //prgDialog1.dismiss();
            try{
                if(getDataResult.length()>0){
                    if(getDataResult.equalsIgnoreCase("ok")){
                        if(getUpdateDataString.length()>0){
                            if(!getUpdateDataString.equalsIgnoreCase("PackageUpdate")){

                                //new PackageListWebService().execute();

                            }
                        }
                    }
                    else{
                        //AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
                    }
                }
                else{
                    //AlertsBoxFactory.showAlert("Please Try Again !!", AppSettingctivity.this);
                }

            }catch(Exception e){

            }


        }
    }

    private class PackageListWebService extends AsyncTask<String, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(
                PackgedetailActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Please Wait.. Loading XML file from server.");
            Dialog.show();
        }
        @Override
        protected void onCancelled() {
            Dialog.dismiss();
            packageListWebService = null;
        }
        protected void onPostExecute(Void unused) {
            //Utils.log("onPostExecute"," executed");

            Dialog.dismiss();
            packageListWebService = null;

            if (rslt.trim().equalsIgnoreCase("ok")) {
                setPackageList(PackgedetailActivity.strXML);
            } else {
                AlertsBoxFactory.showAlert("Invalid web-service response "
                        + rslt, context);
            }
            this.cancel(true);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {

                PackgeCaller caller = new PackgeCaller(getApplicationContext()
                        .getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        utils.getDynamic_Url(), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_GET_CONNCETION_TYPE_PACKAGE_LIST),getAuthenticationMobile());
                caller.setSubscriberId(subscriberID);
                caller.setAreaCode(getAreaCode());
                caller.setAreaCodeFilter(getAreaCodeFilter());
                caller.setConnectionTypeId(ConnectionTypeId);
                caller.join();
                caller.start();
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

    private void setPackageList(String str_xml ) {

        try {
            PackageListParsing packageList = new PackageListParsing(str_xml);
            Utils.log("packageList", ":"+packageList);
            mapPackageList = packageList.getMapPackageList();
            Utils.log("mapPackageList", ":"+mapPackageList.size());

            Iterator it = mapPackageList.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                //Integer key = (Integer) pair.getKey();
                PackageList package_List = (PackageList) pair.getValue();
                ArrayList<PackageList> al_sorted ;

                if(hm_packge_list.containsKey(package_List.getPackagevalidity())){
                    ArrayList<PackageList> al_pkg_list = hm_packge_list.get(package_List.getPackagevalidity());
                    al_pkg_list.add(package_List);

                    hm_packge_list.put(package_List.getPackagevalidity(),al_pkg_list) ;
                }else{
                    ArrayList<PackageList> al_package_list = new ArrayList<>();
                    al_package_list.add(package_List);

                    /*Collections.sort(al_package_list, new Comparator<PackageList>() {
                        @Override
                        public int compare(PackageList lhs, PackageList rhs) {
                            return lhs.getPackageRate().compareTo(rhs.getPackageRate());
                        }
                    });*/
                    hm_packge_list.put(package_List.getPackagevalidity(),al_package_list) ;
                }

                it.remove(); // avoids a ConcurrentModificationException
            }


            final ArrayAdapter adapterForSpinner = new ArrayAdapter(
                    PackgedetailActivity.this,
                    android.R.layout.simple_spinner_item);
            // adapterForSpinner.add("Select Days");

            Set<String> keys = hm_packge_list.keySet();
            Iterator<String> i = keys.iterator();
            final List<Integer> unsortList  = new ArrayList<Integer>();

            while (i.hasNext()) {
                String key = (String) i.next();
                if(!unsortList.contains(key))
                    unsortList.add(Integer.parseInt(key));
            }
            Collections.sort(unsortList,Collections.<Integer>reverseOrder());
            for(Integer temp: unsortList){
                CharSequence textHolder = "" + temp;
                adapterForSpinner.add(textHolder);
            }

            planList.setAdapter(adapterForSpinner);
            gridViewSetting(unsortList);
            setGrid("");
            final List<Integer> final_pkg_list1 = unsortList;

            gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CardView tv = (CardView) view.findViewById(R.id.card_view);
                    tv.setBackgroundColor(context.getResources().getColor(R.color.pkg6));

                    LinearLayout previousSelectedView = (LinearLayout) gridview1.getChildAt(previousSelectedPosition);
                    gridview1.getChildAt(previousSelectedPosition);

                    searchView.setIconified(false);


                    if (previousSelectedPosition != -1)
                    {
                        // previousSelectedView.setSelected(false);
                        CardView cardView = (CardView) previousSelectedView.findViewById(R.id.card_view);
                        cardView.setBackgroundColor(context.getResources().getColor(R.color.label_white_color));
                        tv.setBackgroundColor(context.getResources().getColor(R.color.pkg6));
                    }

                    previousSelectedPosition = position;

                    if(position!=0) {
                        is_selected = false;
                    }
                    else
                        is_selected =true;

                    if(is_selected)
                        gridview1.getChildAt(0).findViewById(R.id.card_view).setBackgroundColor(context.getResources().getColor(R.color.pkg6));
                    else
                        gridview1.getChildAt(0).findViewById(R.id.card_view).setBackgroundColor(context.getResources().getColor(R.color.label_white_color));
                    setGrid(((TextView) view.findViewById( R.id.tv_days )) .getText().toString());
                }

            });



        } catch (Exception e) {
            e.printStackTrace();
            AlertsBoxFactory.showErrorAlert("Error XML " + e.toString(),
                    context);
        }
    }

    private void gridViewSetting(List<Integer> unsortList) {

        // this is size of your list with data
        int size = unsortList.size();
        // Calculated single Item Layout Width for each grid element .. for me it was ~100dp
        int width = 100 ;

        // than just calculate sizes for layout params and use it
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview1.setLayoutParams(params);
        gridview1.setColumnWidth(singleItemWidth);
        gridview1.setHorizontalSpacing(2);
        gridview1.setStretchMode(GridView.STRETCH_SPACING);
        gridview1.setNumColumns(size);
        SpinnerDaysAdapter adapter = new SpinnerDaysAdapter(PackgedetailActivity.this, unsortList);
        gridview1.setAdapter(adapter);
    }


    public void setGrid(String days){
        String final_day_count;
        if(days.length()>0){
            String selecte_plan = days;
            String array[] = selecte_plan.split(" ");
            final_day_count = array[0];
        }else{
            String selecte_plan = planList.getSelectedItem().toString();
            String array[] = selecte_plan.split(" ");
            final_day_count = array[0];
        }

        Iterator it1 = hm_packge_list.entrySet().iterator();
        ArrayList<PackageList> package_List;
        ArrayList<PackageList> final_pkg_list = null;
        final ArrayList<PackageList> last_final_pkg = new ArrayList<>();

        while (it1.hasNext()) {
            Map.Entry pair = (Map.Entry) it1.next();
            String key = (String) pair.getKey();
            package_List = (ArrayList<PackageList>) pair.getValue();

            if(key.equals(final_day_count)){
                final_pkg_list = package_List;
            }
        }

        List<Integer> list  = new ArrayList<>();
        for (int i = 0; i < final_pkg_list.size(); i++) {
            list.add(Integer.parseInt(String.valueOf(final_pkg_list.get(i).getSrno())));
        }

        System.out.println("List in Java sorted in  order: " + list);
        Collections.sort(list);
        //Collections.sort(list, Collections.reverseOrder());
        for (int j = 0; j < list.size(); j++) {
            System.out.println("List in Java sorted in ascending order: " + list);
        }

        for (int k = 0; k < list.size(); k++) {
            for (int i = 0; i < final_pkg_list.size(); i++) {
                if (list.get(k).equals(Integer.parseInt(String.valueOf(final_pkg_list.get(i).getSrno())))) {
                    last_final_pkg.add(final_pkg_list.get(i));
                }
            }
        }

        for (int i = 0; i < last_final_pkg.size(); i++) {
            Log.e("price", ":" +last_final_pkg.get(i).getSrno() +"----"+ last_final_pkg.get(i).getPackageId() + "--------" + last_final_pkg.get(i).getPackageRate() + "--------" + last_final_pkg.get(i).getPackagevalidity());
        }

        gridview.setAdapter(  new CustomGridAdapter( this, last_final_pkg));

        final ArrayList<PackageList> final_pkg_list1 = last_final_pkg;
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(getApplicationContext(),((TextView) v.findViewById( R.id.tv_pkg_name )) .getText(), Toast.LENGTH_SHORT).show();
                PackgedetailActivity.this.finish();
                Intent intent =new Intent(PackgedetailActivity.this, ChangePackage_NewActivity.class);
                intent.putExtra("ConnectionTypeId", ConnectionTypeId);
                intent.putExtra("Selected_Pkg", (Serializable) final_pkg_list1.get(position));
                intent.putExtra(currentBundelPackage,  bundleHelper.getCurrentExtras());
                startActivity(intent);
            }
        });
        searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Search View");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                Log.e("query",":"+query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                ArrayList<PackageList> serachlist =new ArrayList<>();

                for (int i = 0; i < last_final_pkg.size() ; i++) {
                    if(last_final_pkg.get(i).getPlanName().toLowerCase().contains(newText) ){
                        serachlist.add(last_final_pkg.get(i));
                        gridview.setAdapter(new CustomGridAdapter(PackgedetailActivity.this, serachlist));
                    }
                }

                return false;
            }
        });


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
        Authobj.setAppVersion(Utils.GetAppVersion(PackgedetailActivity.this));
        return Authobj;
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

}



