package com.broadbandcollection.billing.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


import com.broadbandcollection.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AndroidDateTimeActivity extends Activity {

	private Calendar dateTime = Calendar.getInstance();
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"MMMM dd, yyyy");
	private SimpleDateFormat timeFormatter = new SimpleDateFormat(
			"hh:mm a");

	private static final int DIALOG_DATE = 1;
	private static final int DIALOG_TIME = 2;

	private Button datePicker;
	private Button timePicker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datetime);
		
		Bundle bundle = getIntent().getExtras();
		String HeaderName = bundle.getString("Header");
		if(HeaderName.equals("CB")){
		TextView headerView = (TextView)findViewById(R.id.headerView);
		headerView.setText("Call Back");
		}
		
		if(HeaderName.equals("VisitAgain"))
		{
			TextView headerView = (TextView)findViewById(R.id.headerView);
			headerView.setText("Visit Again");
			
		}
		if(HeaderName.equals("OutStation"))
		{
			TextView headerView = (TextView)findViewById(R.id.headerView);
			headerView.setText("Out Station / Exams");
			
		}
		
		
		datePicker = (Button) findViewById(R.id.btn_set_date);
		datePicker.setText(dateFormatter.format(dateTime.getTime()));
		datePicker.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_DATE);
			}
		});

		timePicker = (Button) findViewById(R.id.btn_set_time);
		timePicker.setText(timeFormatter.format(dateTime.getTime()));
		timePicker.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showDialog(DIALOG_TIME);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DATE:
			return new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					dateTime.set(year, monthOfYear, dayOfMonth);
					datePicker.setText(dateFormatter
							.format(dateTime.getTime()));
				}
			}, dateTime.get(Calendar.YEAR),
			   dateTime.get(Calendar.MONTH),
			   dateTime.get(Calendar.DAY_OF_MONTH));

		case DIALOG_TIME:
			return new TimePickerDialog(this, new OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker view, int hourOfDay,
						int minute) {
					dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
					dateTime.set(Calendar.MINUTE, minute);
					timePicker.setText(timeFormatter
							.format(dateTime.getTime()));

				}
			}, dateTime.get(Calendar.HOUR_OF_DAY),
			   dateTime.get(Calendar.MINUTE), false);

		}
		return null;
	}
}
