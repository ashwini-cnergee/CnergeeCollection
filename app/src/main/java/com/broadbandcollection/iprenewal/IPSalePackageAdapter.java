package com.broadbandcollection.iprenewal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.broadbandcollection.R;

import java.util.ArrayList;

public class IPSalePackageAdapter extends ArrayAdapter<IPSalePackages> {

	Context ctx;
	ArrayList<IPSalePackages> alIpSalePackages;
	
	public IPSalePackageAdapter(Context context, int resource,
			ArrayList<IPSalePackages> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.ctx=context;
		this.alIpSalePackages=objects;
	}

	 public static class ViewHolder{
        
		 TextView tv_ip_packge_name,tv_ip_packge_rate;
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
		IPSalePackages ipSalePackages=(IPSalePackages)this.getItem(position);
		
		LayoutInflater	inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null){
			convertView=inflater.inflate(R.layout.row_ip_packages	, null);
			holder.tv_ip_packge_name=(TextView)convertView.findViewById(R.id.tv_packege_name);
			holder.tv_ip_packge_rate=(TextView)convertView.findViewById(R.id.tv_packege_rate);
			convertView.setTag( holder );
		}
		else{
			 holder=(ViewHolder)convertView.getTag();
		}
		
		
			
			holder.tv_ip_packge_name.setText(ipSalePackages.getIPSalePackageName());
			if(ipSalePackages.getBaseAmount()!=null){
				if(ipSalePackages.getBaseAmount().length()>0&&!ipSalePackages.getBaseAmount().equalsIgnoreCase("0")){
					holder.tv_ip_packge_rate.setVisibility(View.VISIBLE);
					holder.tv_ip_packge_rate.setText(ipSalePackages.getTotalAmount());
				}
				else{
					holder.tv_ip_packge_rate.setVisibility(View.GONE);
				}
			}
			else{
				holder.tv_ip_packge_rate.setVisibility(View.GONE);
			}
		
					
		return convertView;
	}
}
