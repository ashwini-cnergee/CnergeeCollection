package com.broadbandcollection.billing.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.broadbandcollection.billing.obj.ApplicationInfo;
import com.broadbandcollection.billing.utils.AutoUpdateHelper;

import com.broadbandcollection.R;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class SplashscreenActivity extends Activity {
    private static final String PERMISSIONS_REQUIRED[] = new String[]{

            /*Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE*/

            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.VIBRATE,Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE

    };

    private static final int REQUEST_PERMISSIONS = 110 ;


    public static Context context;
    public UpdateNewAPK updateNewAPK = null;
    public boolean isUpdateFound = false;
    public String app_ver="";
    public AutoUpdateHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        checkPermissions();
    }

    private void checkPermissions() {
        boolean permissionsGranted = checkPermission(PERMISSIONS_REQUIRED);
        if (permissionsGranted) {
            if(!isUpdateFound){
                Thread splashThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            int waited = 0;
                            while (waited < 2000) {
                                sleep(100);
                                waited += 100;
                            }
                        } catch (InterruptedException e) {
                            // do nothing
                        } finally {
                            finish();

                            Intent intent = new Intent(SplashscreenActivity.this, Login.class);
                            intent.putExtra("from", "1");
                            startActivity(intent);
                        }
                    }
                };
                splashThread.start();
            }
        } else {
            boolean showRationale = true;
            for (String permission: PERMISSIONS_REQUIRED) {
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                if (!showRationale) {
                    break;
                }
            }

            String dialogMsg = showRationale ? "We need some permissions to run this APP!" : "You've declined the required permissions, please grant them from your phone settings";

            new AlertDialog.Builder(this)
                    .setTitle("Permissions Required")
                    .setMessage(dialogMsg)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();
                            ActivityCompat.requestPermissions(SplashscreenActivity.this, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);
                        }
                    }).create().show();
        }
    }

    private boolean checkPermission(String permissions[]) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {

            boolean hasGrantedPermissions = true;
            for (int i=0; i<grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasGrantedPermissions = false;
                    break;
                }
            }

            if (!hasGrantedPermissions) {
                finish();
            }else {
                if(!isUpdateFound){
                    Thread splashThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                int waited = 0;
                                while (waited < 2000) {
                                    sleep(100);
                                    waited += 100;
                                }
                            } catch (InterruptedException e) {
                                // do nothing
                            } finally {
                                finish();

                                Intent intent = new Intent(SplashscreenActivity.this, Login.class);
                                intent.putExtra("from", "1");
                                startActivity(intent);
                            }
                        }
                    };
                    splashThread.start();
                }
            }
        } else {
            finish();
        }
    }


    private class UpdateNewAPK extends AsyncTask<String, Void, Void> {

        //private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            //Dialog.setMessage("Please Wait..");
            //Dialog.show();
        }
        protected void onPostExecute(Void unused) {
            //Dialog.dismiss();
            updateNewAPK = null;

            if(!isUpdateFound){
                Thread splashThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            int waited = 0;
                            while (waited < 2000) {
                                sleep(100);
                                waited += 100;
                            }
                        } catch (InterruptedException e) {
                            // do nothing
                        } finally {
                            finish();

                            Intent intent = new Intent(SplashscreenActivity.this, Login.class);
                            intent.putExtra("from", "1");
                            startActivity(intent);
                        }
                    }
                };
                splashThread.start();
            }else{
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                //SelfInstall01Activity.this.finish(); Close The App.

                                try{
                                    DownloadOnSDcard("FTP");
                                    InstallApplication();
                                    UnInstallApplication(getString(R.string.BUILD_PACKAGE_NAME));
                                }catch (IOException e){
                                    Toast.makeText(getApplicationContext(), "Error! " +
                                            e.toString(), Toast.LENGTH_LONG).show();
                                }catch(Exception e){}

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                finish();

                                Intent intent = new Intent(SplashscreenActivity.this, Login.class);
                                intent.putExtra("from", "1");
                                startActivity(intent);
                                break;
                        }
                    }
                };
    			 /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                 builder.setMessage("New Apk Available..").setPositiveButton("Yes Proceed", dialogClickListener)
                     .setNegativeButton("No.", dialogClickListener).show();
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(Html.fromHtml("<font color='#7CFC00'>Click Yes to start download new APK file.</font>"))
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(Html.fromHtml("<font color='#7CFC00'><b>New APK Available </b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);

                AlertDialog alert = builder.create();
                alert.show();

            }
        }
        @Override
        protected void onCancelled() {
            //Dialog.dismiss();
            updateNewAPK = null;
            helper = null;
        }

        @Override
        protected Void doInBackground(String... arg0) {

            String BuildVersionPath;
            String PackageName;
            String AppName;

            helper = new AutoUpdateHelper(context);
            try {
                BuildVersionPath = getString(R.string.BUILD_VERSION_PATH)+"/"+getString(R.string.BUILD_VERSION_FILE);
                PackageName = getString(R.string.BUILD_PACKAGE_NAME);
                AppName = getString(R.string.app_name);
                helper.setApkURL(getString(R.string.BUILD_VERSION_PATH)+"/"+getString(R.string.BUILD_APK_FILE_NAME)+".apk");
                //helper.setApkURL(getString(R.string.BUILD_VERSION_PATH));
                helper.setApkName(getString(R.string.BUILD_APK_FILE_NAME)+".apk");
                //helper.setApkName(app_ver+"_"+getString(R.string.BUILD_APK_FILE_NAME)+".apk");

				/*Log.i("CENRGEE CURRENT VER ",""+app_ver);
				Log.i("CENRGEE CURRENT APP NAME ",""+AppName);
				Log.i("CENRGEE UPDATED SETUP ",""+BuildVersionPath);
				Log.i("CENRGEE UPDATED SETUP ",""+PackageName);*/

                try{
                    helper.GetVersionFromFTPServer(getString(R.string.BUILD_VERSION_PATH),getString(R.string.FTP_USERNAME), getString(R.string.FTP_PASSWORD),getString(R.string.BUILD_VERSION_FILE));
                    //helper.GetVersionFromHTTPServer(BuildVersionPath);
					/*Log.i("CENRGEE UPDATED VERSION ",""+helper.getVersionCode());
					Log.i("CENRGEE UPDATE VERSION NAME",""+helper.getVersionName());*/

                }catch (MalformedURLException e) {
                    Toast.makeText(getApplicationContext(), "Error." + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    isUpdateFound = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error." + e.getMessage(), Toast.LENGTH_SHORT).show();
                    isUpdateFound = false;
                }catch(NullPointerException nu){
                    nu.printStackTrace();
                    isUpdateFound = false;
                }
                String temp = helper.getInstallPackageVersionInfo(AppName.toString());
				/*Log.i("CENRGEE INSTALL",""+temp);*/

                ArrayList<ApplicationInfo> apps = helper.getApplicationInfoList();
                final int max = apps.size();
                for (int i=0; i<max; i++) {

		        	/*Log.i("CENRGEE getAppName ",""+apps.get(i).getAppName().toString());*/
                    if(apps.get(i).getAppName().toString().equals(AppName.toString()))
                    {
                        if(helper.getVersionCode() <= apps.get(i).getVersionCode())
                        {
                            isUpdateFound = true;
                            break;
                        }
                        if(helper.getVersionCode() > apps.get(i).getVersionCode())
                        {
                            isUpdateFound = true;
                            break;
                        }
                    }
                }
		        /*Log.i(" >>>>>>>>>>> IS UPDATE ",""+isUpdateFound);*/

            }catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isUpdateFound = false;
            }
            return null;
        }
    }

    private void DownloadOnSDcard(String protoType)throws IOException, IllegalStateException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException {
        if(protoType.equalsIgnoreCase("FTP"))
            helper.FTPDownloadOnSDcard(getString(R.string.BUILD_VERSION_PATH),getString(R.string.FTP_USERNAME),getString(R.string.FTP_PASSWORD),getString(R.string.BUILD_APK_FILE_NAME)+".apk");
        else
            helper.HTTPDownloadOnSDcard();


    }
    public void UnInstallApplication(String packageName)// Specific package Name Uninstall.
    {
        //Uri packageURI = Uri.parse("package:com.CheckInstallApp");
        Uri packageURI = Uri.parse(packageName.toString());
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        startActivity(uninstallIntent);
    }

    public void InstallApplication()
    {
        Uri packageURI = Uri.parse(getString(R.string.BUILD_PACKAGE_NAME));
        Intent intent = new Intent(Intent.ACTION_VIEW, packageURI);

        //	      Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.setFlags(Intent.ACTION_PACKAGE_REPLACED);

        //	intent.setAction(Settings. ACTION_APPLICATION_SETTINGS);

        intent.setDataAndType
                (Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/"  + helper.getApkName())),
                        "application/vnd.android.package-archive");

        // Not open this Below Line Bcuz...
        ////intent.setClass(this, Project02Activity.class); // This Line Call Activity Recursively its dangerous.

        startActivity(intent);
    }

}
