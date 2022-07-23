/*
 *
 * Java code developed by Ashok Parmar (parmar.ashok@gmail.com)
 * Date of code generation:  22 Dec. 2012
 *
 * Version 1.1
 *
 */
package com.broadbandcollection.billing.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.broadbandcollection.billing.interface_.ShowTime;
import com.broadbandcollection.billing.obj.InformCustomer;
import com.broadbandcollection.R;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.widget.Toast;

import com.broadbandcollection.billing.utils.Utils;
import com.broadbandcollection.widgets.DateInformCustomer;
import com.broadbandcollection.widgets.MyTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class PaymentPickupAdapter extends BaseAdapter implements ConnectionCallbacks, OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	// private Activity activity;
	ArrayList<String> alSubId;
	ArrayList<String> alPickupId;
	public Context context;
	private ArrayList<HashMap<String, String>> data;
	private ArrayList<String> alMessage;
	public ArrayList<Boolean> al_is_postpaid;
	private static LayoutInflater inflater = null;
	//DateTimePicker dateTimePicker;
	DateInformCustomer dateinformTimePicker;
	String date_time = "";
	Utils utils = new Utils();
	public String Username, MemberLoginId;
	String str_date_time = "";
	Button btnInformC;
	String time = "";
	String send_time = "";
	String number = "";
	ShowTime CallInterfaceInfCu;
	String SubscriberId = "";
	String PickUpId = "";
	String send_date = "";
	MyTextView name;
	private final static int PLAY_SERVICES_REQUEST = 1000;
	private final static int REQUEST_CHECK_SETTINGS = 2000;
	String CustAdress;

	private Location mLastLocation;
	private GoogleApiClient mGoogleApiClient;


	SharedPreferences sharedPreferences;
	ArrayList<InformCustomer> alInformedCustomerLists = new ArrayList<InformCustomer>();
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	LocationManager locationManager;

	HashMap<String, String> item;
	String subs_id = "";
	//String postpaid_value;
	boolean inform_cust = false;
	;
	double latitude;
	double longitude;

	public PaymentPickupAdapter(Context context,
								ArrayList<HashMap<String, String>> d, ArrayList<String> alMessage, ArrayList<InformCustomer> alInformedCustomerLists, ArrayList<String> alSubs, ArrayList<String> alPickupId, boolean is_inform_cust, ArrayList<Boolean> al_is_postpaid) {
		this.context = context;
		data = d;
		CallInterfaceInfCu = (ShowTime) context;
		this.alMessage = alMessage;
		this.alInformedCustomerLists = alInformedCustomerLists;
		this.alSubId = alSubs;
		this.alPickupId = alPickupId;
		this.inform_cust = is_inform_cust;
		this.al_is_postpaid = al_is_postpaid;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Utils.log("Constructor", "Postpaid size:" + al_is_postpaid.size());
	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		String[] monthName = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		if (convertView == null)
			vi = inflater.inflate(R.layout.paymt_pick_item, null);

		LinearLayout li = (LinearLayout) vi.findViewById(R.id.li_pay);

		RelativeLayout postpaid = (RelativeLayout) vi.findViewById(R.id.datatest);

		Button btnInformC = (Button) vi.findViewById(R.id.btn_inform_cust);

		Button btn_show_direction = (Button) vi.findViewById(R.id.btn_show_direction);

		name = (MyTextView) vi.findViewById(R.id.pmt_pik_name);
		MyTextView date = (MyTextView) vi.findViewById(R.id.pmt_pik_date);
		MyTextView tvMessage = (MyTextView) vi.findViewById(R.id.pmt_pik_message);
		MyTextView tv_is_postpaid = (MyTextView) vi.findViewById(R.id.tv_is_postpaid);

		item = new HashMap<String, String>();
		item = data.get(position);

		name.setText(item.get("MemberLoginID"));
		subs_id = item.get("MemberLoginID");

		if (al_is_postpaid != null & al_is_postpaid.size() > 0) {
			if (al_is_postpaid.get(position)) {
				tv_is_postpaid.setVisibility(View.VISIBLE);
			} else {
				tv_is_postpaid.setVisibility(View.GONE);
			}
		} else {
			tv_is_postpaid.setVisibility(View.GONE);
		}

		if (checkPlayServices()) {

			// Building the GoogleApi client
			buildGoogleApiClient();
		}

		if (alInformedCustomerLists != null) {
			if (alInformedCustomerLists.size() > 0) {

				for (int i = 0; i < alInformedCustomerLists.size(); i++) {

					//Utils.log("Mmember Id on Customer",""+subs_id);
					InformCustomer informCustomer = alInformedCustomerLists.get(i);


					Calendar cl = Calendar.getInstance();

					String todays_date = "" + cl.get(Calendar.DAY_OF_MONTH) + "" +
							(cl.get(Calendar.MONTH) + 1) + "" + cl.get(Calendar.YEAR);


					Utils.log("Position", "" + position);

					if (informCustomer.getSubscriberid().equalsIgnoreCase(subs_id)) {

						Utils.log("Informed Cust name", "" + informCustomer.getSubscriberid());
						Utils.log("Curremt name", "" + subs_id);


						Utils.log("Informed Cust Date", "" + informCustomer.getDate());
						Utils.log("Curremt Date", "" + todays_date);

						//if(informCustomer.getDate().equalsIgnoreCase(todays_date)){
						Utils.log("Next Activity", "called");
						//name.setTextColor(R.color.label_orange_color);
						name.setTextColor(Color.parseColor("#0000FF"));
						Utils.log("On Blue Color", "Color Blue Executed");

						break;
						//}
						//else{

						//	name.setTextColor(Color.parseColor("#000000"));
						//	Utils.log("On Black Color","Color Black Executed");

						//}
					} else {
						name.setTextColor(Color.parseColor("#000000"));
						Utils.log("On Black Color", "Color Black Executed");

					}
				}
			} else {
				name.setTextColor(Color.parseColor("#000000"));
				Utils.log("On Black Color", "Color Black Executed");
			}
		} else {
			//name.setTextColor(android.R.color.black);
			name.setTextColor(Color.parseColor("#000000"));
			Utils.log("On Black Color", "Color Black Executed");

		}


		final String strDateTime = (item.get("VisitDate"));
		CustAdress = (item.get("CustAdress"));
		final String[] str_split = strDateTime.split("T");
		String[] str_split_date = str_split[0].split("-");
		String[] str_split_time = str_split[1].split(":");
		String str_date = str_split_date[2] + "-"
				+ monthName[Integer.parseInt(str_split_date[1])] + "-"
				+ str_split_date[0] + " at " + str_split_time[0] + " : "
				+ str_split_time[1] + " : "
				+ str_split_time[2].toString().replace("+05", " ");

		Username = (utils.getAppUserName());
		MemberLoginId = (utils.getMobLoginId());

		String messgae = alMessage.get(position);

		tvMessage.setText(Html.fromHtml(messgae));
		date.setText(Html.fromHtml(str_date));

		btnInformC.setVisibility(View.VISIBLE);

//		if (inform_cust) {
//			btnInformC.setVisibility(View.VISIBLE);
//		} else {
//			btnInformC.setVisibility(View.GONE);
//		}


		btn_show_direction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getLocation();

				if (mLastLocation != null) {
					latitude = mLastLocation.getLatitude();
					longitude = mLastLocation.getLongitude();
					getAddress();

				}

				/*Geocoder geocoder;
				List<Address> addresses;
				geocoder = new Geocoder(context, Locale.getDefault());
				locationManager = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);

				try {
					addresses = geocoder.getFromLocation(20.5666, 45.345, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

					String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
					String city = addresses.get(0).getLocality();
					String state = addresses.get(0).getAdminArea();
					String country = addresses.get(0).getCountryName();
					String postalCode = addresses.get(0).getPostalCode();
					String knownName = addresses.get(0).getFeatureName();

					Log.e("Address",":"+address+" "+city + " "+state +" "+country +" "+postalCode+" "+knownName);

				} catch (IOException e) {
					e.printStackTrace();
				}


				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.com/maps?saddr=ghansoli&daddr=belapur"));
				context.startActivity(intent);*/

			}
		});


		btnInformC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.log("Size", "alSubId:" + alSubId.size());
				final String CheckSUBS = (alSubId.get(position));
				final String strDateTime = (data.get(position).get("VisitDate"));
				SubscriberId = CheckSUBS;
				final String[] str_split = strDateTime.split("T");
				PickUpId = alPickupId.get(position);
				Utils.log("MemberId OnCLick" + position, ":" + CheckSUBS);
				Utils.log("Time OnCLick" + position, ":" + strDateTime);

				//Utils.log("MemberId On Li",""+subs_id);
				Calendar cl = Calendar.getInstance();

				String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "" + (cl.get(Calendar.MONTH) + 1) + "" + cl.get(Calendar.YEAR);
				Utils.log("Date", "" + date);
				boolean is_popup_show = false;
				if (alInformedCustomerLists != null) {
					if (alInformedCustomerLists.size() > 0) {

						for (int i = 0; i < alInformedCustomerLists.size(); i++) {

							Utils.log("Mmember Id on Customer", "" + subs_id);
							InformCustomer informCustomer = alInformedCustomerLists.get(i);


							Utils.log("Position", "" + position);
							Utils.log("Subscriber Clicked", "" + CheckSUBS);
							Utils.log("Saved Subscriber", ":" + informCustomer.getSubscriberid());
							Utils.log("Todays Date", "" + date);
							Utils.log("Saved Date ", ":" + informCustomer.getDate());
							
						/*	Utils.log("Clicked Subscriber Name",":"+SubscriberId);
							Utils.log("Clicked Date ",":"+date);*/

							Utils.log("Position", "" + position);

							if (informCustomer.getSubscriberid().equalsIgnoreCase(CheckSUBS)) {

								Utils.log("Next Activity", "called");
								CallInterfaceInfCu.callDetailsActivity(SubscriberId, al_is_postpaid.get(position));
								is_popup_show = false;
								break;
								/*if(informCustomer.getDate().equalsIgnoreCase(date)){
									Utils.log("Next Activity","called");
									CallInterfaceInfCu.callDetailsActivity(SubscriberId);
									is_popup_show=false;
									break;
								}
								else{
									Utils.log("Popup Appear","Popup up");
									String[] str_split_date = str_split[0].split("-");
									String[] str_split_time = str_split[1].split(":");
									
									time = str_split_time[0] + " : " + str_split_time[1] + " : "
											+ str_split_time[2].toString().replace("+05", " ");;
									send_time=str_split_date[2] + ""
											+ Integer.parseInt(str_split_date[1]) + ""
											+ str_split_date[0] +str_split_time[0] + "" + str_split_time[1] + ""
											+ str_split_time[2].toString().replace("+05", "");;
											
											send_date=str_split_date[2] + ""
													+ Integer.parseInt(str_split_date[1]) + ""
													+ str_split_date[0];
									Utils.log("Time:",":"+time);
									is_popup_show=true;
								//	CustomerPopupView(time,send_time);
									Utils.log("Position",":"+position);
									//break;
									
								}*/
							} else {
								Utils.log("Popup Appear", "Popup up");
								String[] str_split_date = str_split[0].split("-");
								String[] str_split_time = str_split[1].split(":");

								time = str_split_time[0] + " : " + str_split_time[1] + " : "
										+ str_split_time[2].toString().replace("+05", " ");
								;
								send_time = str_split_date[2] + ""
										+ str_split_date[1] + ""
										+ str_split_date[0] + str_split_time[0] + "" + str_split_time[1] + ""
										+ str_split_time[2].toString().replace("+05", "");
								;

								send_date = str_split_date[2] + ""
										+ str_split_date[1] + ""
										+ str_split_date[0];
								Utils.log("Time:", ":" + time);
								//CustomerPopupView(time,send_time);
								Utils.log("Position", ":" + position);
								is_popup_show = true;
								//break;


							}
						}
						if (is_popup_show) {
							CustomerPopupView(time, send_time);
						} else {

						}
					} else {

						Utils.log("Popup Appear", "Popup up");
						String[] str_split_date = str_split[0].split("-");
						String[] str_split_time = str_split[1].split(":");

						time = str_split_time[0] + " : " + str_split_time[1] + " : "
								+ str_split_time[2].toString().replace("+05", " ");
						;
						send_time = str_split_date[2] + ""
								+ str_split_date[1] + ""
								+ str_split_date[0] + str_split_time[0] + "" + str_split_time[1] + ""
								+ str_split_time[2].toString().replace("+05", "");
						;

						send_date = str_split_date[2] + ""
								+ str_split_date[1] + ""
								+ str_split_date[0];
						Utils.log("Time:", ":" + time);
						CustomerPopupView(time, send_time);
						Utils.log("Position", ":" + position);

					}
				} else {
					Utils.log("Popup Appear", "Popup up");
					String[] str_split_date = str_split[0].split("-");
					String[] str_split_time = str_split[1].split(":");

					time = str_split_time[0] + " : " + str_split_time[1] + " : "
							+ str_split_time[2].toString().replace("+05", " ");
					;
					send_time = str_split_date[2] + ""
							+ str_split_date[1] + ""
							+ str_split_date[0] + str_split_time[0] + "" + str_split_time[1] + ""
							+ str_split_time[2].toString().replace("+05", "");
					;

					send_date = str_split_date[2] + ""
							+ str_split_date[1] + ""
							+ str_split_date[0];
					Utils.log("Time:", ":" + time);
					CustomerPopupView(time, send_time);
					Utils.log("Position", ":" + position);
				}
			}
		});

		li.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//	SubscriberId = subs_id;
				final String CheckSUBS = (alSubId.get(position));
				final String strDateTime = (data.get(position).get("VisitDate"));
				SubscriberId = CheckSUBS;
				if (inform_cust) {
					boolean is_popup_show = false;
				/*HashMap<String, String> item23 = new HashMap<String, String>();
				item23 = data.get(position);*/


					PickUpId = alPickupId.get(position);
					final String[] str_split = strDateTime.split("T");

					Utils.log("MemberId OnCLick" + position, ":" + CheckSUBS);
					Utils.log("Time OnCLick" + position, ":" + strDateTime);

					//Utils.log("MemberId On Li",""+subs_id);
					Calendar cl = Calendar.getInstance();

					String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "" +
							(cl.get(Calendar.MONTH) + 1) + "" + cl.get(Calendar.YEAR);
					Utils.log("Date", "" + date);
					if (alInformedCustomerLists != null) {
						if (alInformedCustomerLists.size() > 0) {

							for (int i = 0; i < alInformedCustomerLists.size(); i++) {

								Utils.log("Mmember Id on Customer", "" + subs_id);
								InformCustomer informCustomer = alInformedCustomerLists.get(i);


								Utils.log("Position", "" + position);
								Utils.log("Subscriber Clicked", "" + CheckSUBS);
								Utils.log("Saved Subscriber", ":" + informCustomer.getSubscriberid());
								Utils.log("Todays Date", "" + date);
								Utils.log("Saved Date ", ":" + informCustomer.getDate());

								Utils.log("Position", "" + position);


								if (informCustomer.getSubscriberid().equalsIgnoreCase(CheckSUBS)) {

									Utils.log("Next Activity", "called");
									CallInterfaceInfCu.callDetailsActivity(SubscriberId, al_is_postpaid.get(position));
									is_popup_show = false;
									break;
								
								/*if(informCustomer.getDate().equalsIgnoreCase(date)){
									Utils.log("Next Activity","called");
									CallInterfaceInfCu.callDetailsActivity(SubscriberId);
									is_popup_show=false;
									break;
								}
								else{
									Utils.log("Popup Appear","Popup up");
									String[] str_split_date = str_split[0].split("-");
									String[] str_split_time = str_split[1].split(":");
									
									time = str_split_time[0] + " : " + str_split_time[1] + " : "
											+ str_split_time[2].toString().replace("+05", " ");;
									send_time=str_split_date[2] + ""
											+ Integer.parseInt(str_split_date[1]) + ""
											+ str_split_date[0] +str_split_time[0] + "" + str_split_time[1] + ""
											+ str_split_time[2].toString().replace("+05", "");;
											
											send_date=str_split_date[2] + ""
													+ Integer.parseInt(str_split_date[1]) + ""
													+ str_split_date[0];
									Utils.log("Time:",":"+time);
									is_popup_show=true;
									//CustomerPopupView(time,send_time);
									Utils.log("Position",":"+position);
								}*/
								} else {
									Utils.log("Popup Appear", "Popup up");
									Utils.log("Unformated Date", ":" + str_split[0]);
									String[] str_split_date = str_split[0].split("-");
									String[] str_split_time = str_split[1].split(":");

									time = str_split_time[0] + " : " + str_split_time[1] + " : "
											+ str_split_time[2].toString().replace("+05", " ");
									;
									send_time = str_split_date[2] + ""
											+ str_split_date[1] + ""
											+ str_split_date[0] + str_split_time[0] + "" + str_split_time[1] + ""
											+ str_split_time[2].toString().replace("+05", "");
									;

									send_date = str_split_date[2] + ""
											+ str_split_date[1] + ""
											+ str_split_date[0];
									Utils.log("Time:", ":" + time);
									Utils.log("Error Date: is", ":" + str_split_date[1]);
									is_popup_show = true;
									//CustomerPopupView(time,send_time);
									Utils.log("Position", ":" + position);
								}
							}
							if (is_popup_show) {
								CustomerPopupView(time, send_time);
							} else {

							}
						} else {

							Utils.log("Popup Appear", "Popup up");
							Utils.log("Unformated Date", ":" + str_split[0]);
							String[] str_split_date = str_split[0].split("-");
							String[] str_split_time = str_split[1].split(":");

							time = str_split_time[0] + " : " + str_split_time[1] + " : "
									+ str_split_time[2].toString().replace("+05", " ");
							;
							send_time = str_split_date[2] + ""
									+ str_split_date[1] + ""
									+ str_split_date[0] + str_split_time[0] + "" + str_split_time[1] + ""
									+ str_split_time[2].toString().replace("+05", "");
							;

							send_date = str_split_date[2] + ""
									+ str_split_date[1] + ""
									+ str_split_date[0];
							Utils.log("Time:", ":" + time);

							Utils.log("Error Date: is", ":" + str_split_date[1]);
							//is_popup_show=true;
							CustomerPopupView(time, send_time);
							Utils.log("Position", ":" + position);

						}
					} else {
						Utils.log("Popup Appear", "Popup up");
						Utils.log("Unformated Date", ":" + str_split[0]);
						String[] str_split_date = str_split[0].split("-");
						String[] str_split_time = str_split[1].split(":");

						time = str_split_time[0] + " : " + str_split_time[1] + " : "
								+ str_split_time[2].toString().replace("+05", " ");
						;
						send_time = str_split_date[2] + ""
								+ str_split_date[1] + ""
								+ str_split_date[0] + str_split_time[0] + "" + str_split_time[1] + ""
								+ str_split_time[2].toString().replace("+05", "");
						;

						send_date = str_split_date[2] + ""
								+ str_split_date[1] + ""
								+ str_split_date[0];
						Utils.log("Time:", ":" + time);

						Utils.log("Date:", ":" + send_date);
						CustomerPopupView(time, send_time);
						Utils.log("Position", ":" + position);
						Utils.log("Error Date: is", ":" + str_split_date[1]);
					}
				} else {
					CallInterfaceInfCu.callDetailsActivity(SubscriberId, al_is_postpaid.get(position));
				}
			}
		});
		return vi;
	}

	public void getAddress()
	{

		Address locationAddress=getAddress(latitude,longitude);

		if(locationAddress!=null)
		{
			String address = locationAddress.getAddressLine(0);
			String address1 = locationAddress.getAddressLine(1);
			String city = locationAddress.getLocality();
			String state = locationAddress.getAdminArea();
			String country = locationAddress.getCountryName();
			String postalCode = locationAddress.getPostalCode();

			String currentLocation = null;

			if(!TextUtils.isEmpty(address))
			{
				currentLocation=address;

				if (!TextUtils.isEmpty(address1))
					currentLocation+=" "+address1;

				if (!TextUtils.isEmpty(city))
				{
					currentLocation+=" "+city;

					if (!TextUtils.isEmpty(postalCode))
						currentLocation+=" - "+postalCode;
				}
				else
				{
					if (!TextUtils.isEmpty(postalCode))
						currentLocation+= " "+postalCode;
				}

				if (!TextUtils.isEmpty(state))
					currentLocation+=" "+state;

				if (!TextUtils.isEmpty(country))
					currentLocation+=" "+country;

			}


			Log.e("Address",":"+currentLocation);
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?saddr="+currentLocation+"&daddr="+CustAdress+""));
			context.startActivity(intent);
		}

	}

	private void getLocation() {

		//if (isPermissionGranted) {

			try
			{
				mLastLocation = LocationServices.FusedLocationApi
						.getLastLocation(mGoogleApiClient);
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}

		//}

	}



	public Address getAddress(double latitude,double longitude)
	{
		Geocoder geocoder;
		List<Address> addresses;
		geocoder = new Geocoder(context, Locale.getDefault());

		try {
			addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
			return addresses.get(0);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}


	private boolean checkPlayServices() {

		GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

		int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);

		if (resultCode != ConnectionResult.SUCCESS) {
			if (googleApiAvailability.isUserResolvableError(resultCode)) {
				googleApiAvailability.getErrorDialog((Activity) context,resultCode,
						PLAY_SERVICES_REQUEST).show();
			} else {
				Toast.makeText(context,
						"This device is not supported.", Toast.LENGTH_LONG)
						.show();
			}
			return false;
		}
		return true;
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();

		mGoogleApiClient.connect();

		LocationRequest mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(mLocationRequest);

		PendingResult<LocationSettingsResult> result =
				LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(LocationSettingsResult locationSettingsResult) {

				final Status status = locationSettingsResult.getStatus();

				switch (status.getStatusCode()) {
					case LocationSettingsStatusCodes.SUCCESS:
						// All location settings are satisfied. The client can initialize location requests here
						getLocation();
						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						try {
							// Show the dialog by calling startResolutionForResult(),
							// and check the result in onActivityResult().
							status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);

						} catch (IntentSender.SendIntentException e) {
							// Ignore the error.
						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						break;
				}
			}
		});


	}






	@SuppressLint("NewApi")
	protected void CustomerPopupView(final String time, final String send_time) {
		// TODO Auto-generated method stub

		CallInterfaceInfCu = (ShowTime) context;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.dialog_inform_cst);
		int width = 0;
		int height = 0;

		Point size = new Point();/*
								 * WindowManager w =getWindowManager();
								 */
		WindowManager w = ((Activity) context).getWindowManager();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			w.getDefaultDisplay().getSize(size);
			width = size.x;
			height = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			width = d.getWidth();
			height = d.getHeight();
			;
		}

		Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
		Button btnChange = (Button) dialog.findViewById(R.id.btn_change_cst);

		TextView timetxt = (TextView) dialog.findViewById(R.id.time_txt);

		timetxt.setText(time);
		Utils.log("Time On Ok", ":" + time);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.log("Time On Ok", ":" + time);
				Utils.log("On Direcxt ", "On Click OK");
				CallInterfaceInfCu.CallinformCustomer(send_time, SubscriberId, send_date, PickUpId);

				Utils.log("Button ", "button Ok Executed");
				dialog.dismiss();

			}
		});

		btnChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				//SubscriberId=subs_id;//name.getText().toString();

				Utils.log("Button change", "button Change Executed");
				ChangeDate(send_date);
				dialog.dismiss();


			}
		});

		dialog.show();
		// (width/2)+((width/2)/2)
		// dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setLayout((width / 2) + (width / 2) / 2,
				LayoutParams.WRAP_CONTENT);

	}

	protected void ChangeDate(String date) {

		Utils.log("Date is ", ":" + date);
		CallInterfaceInfCu = (ShowTime) context;
		// TODO Auto-generated method stub
		final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		final AlertDialog alertd = dialog.create();
		dateinformTimePicker = new DateInformCustomer(context);
		dialog.setView(dateinformTimePicker);
		Username = (utils.getAppUserName());
		MemberLoginId = (utils.getMobLoginId());

		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				long date_time = dateinformTimePicker.getDateTimeMillis();
				Calendar cl = Calendar.getInstance();


				Utils.log("On Date ", "On Click OK");

				cl.setTimeInMillis(date_time); // here your time in miliseconds
				/*
				 * String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" +
				 * (cl.get(Calendar.MONTH)+1) + "/" + cl.get(Calendar.YEAR);
				 */
			/*	String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":"
						+ cl.get(Calendar.MINUTE);*/
				// insertValue(value,st1,st2,st3,st4);

				/*
				 * String day=""; if(cl.get(Calendar.DAY_OF_MONTH)>9){
				 * day=String.valueOf(cl.get(Calendar.DAY_OF_MONTH)); } else{
				 * day="0"+String.valueOf(cl.get(Calendar.DAY_OF_MONTH)); }
				 * String month=""; if((cl.get(Calendar.MONTH)+1)>9){
				 * month=String.valueOf(cl.get(Calendar.MONTH)+1); } else{
				 * month="0"+String.valueOf(cl.get(Calendar.MONTH)+1); }
				 */
				String hour = "";
				if ((cl.get(Calendar.HOUR_OF_DAY)) > 9) {
					hour = String.valueOf(cl.get(Calendar.HOUR_OF_DAY));
				} else {
					hour = "0" + String.valueOf(cl.get(Calendar.HOUR_OF_DAY));
				}

				String min = "";
				if ((cl.get(Calendar.MINUTE)) > 9) {
					min = String.valueOf(cl.get(Calendar.MINUTE));
				} else {
					min = "0" + String.valueOf(cl.get(Calendar.MINUTE));
				}

				String sec = "";
				if ((cl.get(Calendar.SECOND)) > 9) {
					sec = String.valueOf(cl.get(Calendar.SECOND));
				} else {
					sec = "0" + String.valueOf(cl.get(Calendar.SECOND));
				}

				/*
				 * if((cl.get(Calendar.HOUR_OF_DAY)+1)>9){
				 * month=String.valueOf(cl.get(Calendar.MONTH)+1); } else{
				 * day="0"+String.valueOf(cl.get(Calendar.MONTH)+1); }
				 */
				send_time = send_date + hour + min + sec;
				// Utils.log("Date",":"+date);
				Utils.log("Time", ":" + time);
				// etDate_Time.setText(date +" "+ time);
				//SubscriberId=subs_id;//name.getText().toString();
				CallInterfaceInfCu.CallinformCustomer(send_time, SubscriberId, send_date, PickUpId);
				Utils.log("UserName", ":" + MemberLoginId);
				Utils.log("Usert", ":" + Username);
				Utils.log("UserNAme3 ", ":" + utils.app_username);
				Utils.log("Memeber", ":" + utils.MobLoginId);

			}
		});

		dialog.setNegativeButton("CANCEL",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						dialog.cancel();
						alertd.dismiss();
					}
				});

		/*
		 * dialog.getWindow().setBackgroundDrawable(new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 * dialog.getWindow().setLayout((width/2)+(width/2)/2,
		 * LayoutParams.WRAP_CONTENT);
		 */

		dialog.show();
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		getLocation();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult result) {
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());
	}
}

