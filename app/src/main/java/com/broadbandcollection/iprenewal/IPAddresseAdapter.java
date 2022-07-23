package com.broadbandcollection.iprenewal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.broadbandcollection.R;
import com.broadbandcollection.billing.utils.Utils;

import java.util.ArrayList;

public class IPAddresseAdapter extends ArrayAdapter<IP_Address> {

	Context ctx;
	ArrayList<IP_Address> alIp_Addresses;
	ArrayList<Boolean> alBooleans;
	
	public IPAddresseAdapter(Context context, int resource,
                             ArrayList<IP_Address> objects, ArrayList<Boolean> alBooleans) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.ctx=context;
		this.alIp_Addresses=objects;
		this.alBooleans=alBooleans;
	}

	 public static class ViewHolder{
        
		// RadioButton rb_ip_addr;
		 TextView tv_ip_addr;
		 RelativeLayout rl_ip_addrs;
	 }
	 
	 @Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getView(position, convertView, parent);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder= new ViewHolder();
		IP_Address IP_Address=(IP_Address)this.getItem(position);
		
		LayoutInflater	inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null){
			convertView=inflater.inflate(R.layout.row_ip_address	, null);
			//holder.rb_ip_addr=(RadioButton)convertView.findViewById(R.id.rb_ip_addr);
			holder.tv_ip_addr=(TextView)convertView.findViewById(R.id.tv_ip_addr);
			holder.rl_ip_addrs=(RelativeLayout)convertView.findViewById(R.id.rl_ip_addrs);
			
			convertView.setTag( holder );
		}
		else{
			 holder=(ViewHolder)convertView.getTag();
		}
		
		if(Change_Ip_Activity.color_position!=-1){
			Utils.log("color ", "position:"+ Change_Ip_Activity.color_position);
			if(position== Change_Ip_Activity.color_position){
				holder.tv_ip_addr.setTextColor(R.color.label_red_color);
			}
			else{
				holder.tv_ip_addr.setTextColor(R.color.label_black_color);	
			}
		}
		else{
			holder.tv_ip_addr.setTextColor(R.color.label_black_color);
		}
				
			holder.tv_ip_addr.setText(IP_Address.getIPADDRESS());
			
		
					
		return convertView;
	}
}
