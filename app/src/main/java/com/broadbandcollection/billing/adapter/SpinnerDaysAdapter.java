package com.broadbandcollection.billing.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.broadbandcollection.R;

import java.util.List;




/**
 * Created by Jyoti on 8/27/2018.
 */

public class SpinnerDaysAdapter extends BaseAdapter {


    private Context context;
   List<Integer> arrayList;

    //Constructor to initialize values
    public SpinnerDaysAdapter(Context context, List<Integer> arrayList) {
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

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate( R.layout.grid_days , null);

            TextView tv_days = (TextView) gridView.findViewById(R.id.tv_days);

            tv_days.setText(String.valueOf(Math.round(Double.parseDouble(arrayList.get(position).toString()))));
            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/DroidSerif_Bold.ttf");
            tv_days.setTypeface(face);

            TextView tv_day = (TextView) gridView.findViewById(R.id.tv_day);
            CardView cardView = (CardView) gridView.findViewById(R.id.card_view);

            Typeface face1 = Typeface.createFromAsset(context.getAssets(),
                    "fonts/UbuntuCondensed-Regular.ttf");
            tv_day.setTypeface(face1);

                if(position == 0){
                    cardView.setBackgroundColor(context.getResources().getColor(R.color.pkg6));
                } else {
                    cardView.setBackgroundColor(context.getResources().getColor(R.color.label_white_color));
                }


        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}
