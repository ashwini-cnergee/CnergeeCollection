package com.broadbandcollection.billing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.broadbandcollection.R;

public class LogoutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logout);
		
		finish();
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_logout, menu);
		return true;
	}
	
	/*// add this line for Removing Force Close
	@Override
	protected void onDestroy() {
	// closing Entire Application
	android.os.Process.killProcess(android.os.Process.myPid());
	super.onDestroy();
	}*/

}
