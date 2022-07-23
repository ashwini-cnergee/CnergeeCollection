package com.broadbandcollection.billing.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.broadbandcollection.billing.obj.PackageList;
import com.broadbandcollection.R;

import java.util.ArrayList;


public class CustomGridAdapter extends BaseAdapter {

    private Context context;
    ArrayList<PackageList> arrayList;

    //Constructor to initialize values
    public CustomGridAdapter(Context context, ArrayList<PackageList> arrayList) {
        this.context        = context;
        this.arrayList     = arrayList;
    }

    @Override
    public int getCount() {

        // Number of times getView method call depends upon gridValues.length
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    // Number of times getView method call depends upon gridValues.length

    public View getView(int position, View convertView, ViewGroup parent) {

        // LayoutInflator to call external grid_item.xml file

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate( R.layout.horizontal_item_view , null);

            TextView tv_days = (TextView) gridView.findViewById(R.id.tv_days);
            TextView tv_pkg_name = (TextView) gridView.findViewById(R.id.tv_pkg_name);
            TextView tv_validity = (TextView) gridView.findViewById(R.id.tv_validity);

            LinearLayout ll_amount =(LinearLayout)gridView.findViewById(R.id.ll_amount);

            TextView tv_desc = (TextView) gridView.findViewById(R.id.tv_desc);
            tv_desc.setTextColor(context.getResources().getColor(R.color.help_calc));

            tv_days.setText(context.getResources().getString(R.string.rs)+ " "+ Math.round(Double.parseDouble(arrayList.get(position).getPackageRate())));
            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/UbuntuCondensed-Regular.ttf");
            tv_days.setTypeface(face);

            tv_pkg_name.setText(arrayList.get(position).getPlanName());
            Typeface face1 = Typeface.createFromAsset(context.getAssets(),
                    "fonts/UbuntuCondensed-Regular.ttf");
            tv_pkg_name.setTypeface(face1);

            Log.e("Offer Desc",":"+arrayList.get(position).getOfferdesc());
            tv_validity.setText(arrayList.get(position).getPackagevalidity() + " " +" Days");

            //tv_validity.setText(arrayList.get(position).getPackagevalidity() + " Days");
//            try {
//                if (arrayList.get(position).getOfferdesc().length() > 0 || arrayList.get(position).getOfferdesc() != null || !arrayList.get(position).getOfferdesc().equalsIgnoreCase("null")) {
//                    tv_validity.setText(arrayList.get(position).getOfferdesc());
//                    tv_validity.setVisibility(View.VISIBLE);
//                } else {
//                    tv_validity.setVisibility(View.GONE);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }

            tv_desc.setText(arrayList.get(position).getPackagedesc());
            Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Neuton_Regular.ttf");
            tv_desc.setTypeface(face2);
            tv_validity.setTypeface(face2);
            String text = arrayList.get(position).getPackageRate();
            Double intVal = 0.00;

            try {
                intVal = Double.parseDouble(text);

                Log.e("intVal",":"+intVal);
                if(intVal <= 1000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg1));
                }else if(intVal > 1000 && intVal<=2000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg2));
                }else if(intVal > 2000 && intVal<=5000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg3));
                }else if(intVal > 5000 && intVal<=10000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg4));
                }else{
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg5));
                }
            } catch (NumberFormatException e) {
                intVal = Double.parseDouble(arrayList.get(position).getPackageRate());
                if(intVal <= 1000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg1));
                }else if(intVal > 1000 && intVal<=2000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg2));
                }else if(intVal > 2000 && intVal<=5000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg3));
                }else if(intVal > 5000 && intVal<=10000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg4));
                }else{
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg5));
                }
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}