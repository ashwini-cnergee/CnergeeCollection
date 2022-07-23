package com.broadbandcollection.iprenewal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.broadbandcollection.R;

import java.util.ArrayList;

public class PoolDetailsAdapter extends ArrayAdapter<PoolDetails> {

	Context ctx;
	ArrayList<PoolDetails> alPoolDetails;
	
	public PoolDetailsAdapter(Context context, int resource,
			ArrayList<PoolDetails> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.ctx=context;
		this.alPoolDetails=objects;
	}

	 public static class ViewHolder{
        
		 TextView tv_ip_pool_name;
	 }
	 
	 @Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getView(position, convertView, parent);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder= new ViewHolder();
		PoolDetails poolDetails =(PoolDetails)this.getItem(position);
		
		LayoutInflater	inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null){
			convertView=inflater.inflate(R.layout.spinner_item	, null);
			holder.tv_ip_pool_name=(TextView)convertView.findViewById(R.id.tv_spinner_item);
			
			convertView.setTag( holder );
		}
		else{
			 holder=(ViewHolder)convertView.getTag();
		}
		
		
			
			holder.tv_ip_pool_name.setText(poolDetails.getIPPoolName());
			
					
		return convertView;
	}
}
