package com.broadbandcollection.billing.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;


import com.broadbandcollection.billing.obj.ApplicationInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class AutoUpdateHelper {
	
	private int VersionCode;
	private String VersionName;
	private ArrayList<ApplicationInfo> appsInfo;
	private String apkURL;
	private String apkName;
	
	private Context context;
	
	private static final String PATH = Environment.getExternalStorageDirectory() + "/download/";
     
	public AutoUpdateHelper(Context context){
		this.context=context;
	}

	public void GetVersionFromFTPServer(String BuildVersionPath,String userName,String password,String fileName)throws 
		IllegalStateException,FTPIllegalReplyException,FTPException,IOException, FTPDataTransferException, FTPAbortedException
    {
		// FTP URL (Starts with ftp://, sftp:// or ftps:// followed by hostname and port).
		//Uri ftpUri = Uri.parse(BuildVersionPath);
		FTPClient client = new FTPClient();
	//	try {
			/*Log.i(" >>> ","BuildVersionPath "+BuildVersionPath);
			Log.i(" >>> ","userName "+userName);
			Log.i(" >>> ","password "+password);
			Log.i(" >>> ","fileName "+fileName);*/
			
			client.connect(BuildVersionPath,21);
			/*Log.i(" >>> ","CONNECT FTP "+client.isConnected());*/
			
			 
			 OutputStream ow = new ByteArrayOutputStream();
			
			if(client.isConnected()){
				client.login(userName, password);
				client.download(fileName, ow, -1, null);
				setVersion(ow.toString());
				ow.flush();
				ow.close();
			}
			
    }
	
	public void GetVersionFromHTTPServer(String BuildVersionPath)throws MalformedURLException,IOException
    {
		URL url;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream in=null;
		HttpURLConnection c=null;
		 
		try {
			url = new URL(BuildVersionPath.toString());
			/*Log.i("HttpURLConnection ! ",""+url.toString());*/
			
            c = (HttpURLConnection) url.openConnection();           
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            /*Log.i("HttpURLConnection Complete.! ","");*/
            //Toast.makeText(getApplicationContext(), "HttpURLConnection Complete.!", Toast.LENGTH_SHORT).show();  

            in = c.getInputStream();
       
           
            byte[] buffer = new byte[1024]; //that stops the reading after 1024 chars..
            //in.read(buffer); //  Read from Buffer.
            //baos.write(buffer); // Write Into Buffer.

            int len1 = 0;
            while ( (len1 = in.read(buffer)) != -1 ) 
            {               
                baos.write(buffer,0, len1); // Write Into ByteArrayOutputStream Buffer.
            }

               
            String s = baos.toString();// baos.toString(); contain Version Code = 2; \n Version name = 2.1;
            /*Log.i("HttpURLConnection TEXT ",""+s);*/
          
            
           // baos.close();
        }finally{
        	try{
        		in.close();
        		baos.close();
        		c.disconnect();
        	}catch(NullPointerException e){}
        }

    }
	protected void setVersion(String s){
		String temp = "";  
		  for (int i = 0; i < s.length(); i++)
          {               
              i = s.indexOf("=") + 1; 
              while (s.charAt(i) == ' ') // Skip Spaces
              {
                  i++; // Move to Next.
              }
              while (s.charAt(i) != ';'&& (s.charAt(i) >= '0' && s.charAt(i) <= '9' || s.charAt(i) == '.'))
              {
                  temp = temp.toString().concat(Character.toString(s.charAt(i))) ;
                  i++;
              }
              //
              s = s.substring(i); // Move to Next to Process.!
              temp = temp + " "; // Separate w.r.t Space Version Code and Version Name.
          }
          /*Log.i(" VERSION FILE OUT ",""+temp);*/
          String[] fields = temp.split(" ");// Make Array for Version Code and Version Name.

          VersionCode = Integer.parseInt(fields[0].toString());// .ToString() Return String Value.
          VersionName = fields[1].toString();
          setVersionCode(VersionCode);
          setVersionName(VersionName);
	}
	 // Get Information about Only Specific application which is Install on Device.
    public String getInstallPackageVersionInfo(String appName) 
    {
        String InstallVersion = "";     
        ArrayList<ApplicationInfo> apps = getInstalledApps(false); /* false = no system packages */
        setApplicationInfoList(apps);
        
        final int max = apps.size();
        for (int i=0; i<max; i++) 
        {
            //apps.get(i).prettyPrint();        
            if(apps.get(i).getAppName().toString().equals(appName.toString()))
            {
                InstallVersion = "Install Version Code: "+ apps.get(i).getVersionCode()+ " Version Name: "+ apps.get(i).getVersionName().toString();
                break;
            }
        }

        return InstallVersion.toString();
    }
    
    private ArrayList<ApplicationInfo> getInstalledApps(boolean getSysPackages) 
    {       
        ArrayList<ApplicationInfo> res = new ArrayList<ApplicationInfo>();        
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);

        for(int i=0;i<packs.size();i++) 
        {
        	
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            ApplicationInfo newInfo = new ApplicationInfo();
            newInfo.setAppName(p.applicationInfo.loadLabel(context.getPackageManager()).toString());
            newInfo.setPackageName(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode);
            //newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
            /*Log.i(" INSATALL APPs ",""+newInfo.getAppName());*/
            res.add(newInfo);
        }
        return res; 
    }
    public void FTPDownloadOnSDcard(String BuildVersionPath,String userName,String password,String fileName) throws IOException, IllegalStateException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException
    {
    	FTPClient client = new FTPClient();
    	OutputStream fos=null;
    	InputStream is=null;
    	
 		/*Log.i(" >>> ","BuildVersionPath "+BuildVersionPath);
 		Log.i(" >>> ","userName "+userName);
 		Log.i(" >>> ","password "+password);
 		Log.i(" >>> ","fileName "+fileName);*/
 		
 		client.connect(BuildVersionPath,21);
 		/*Log.i(" >>> ","CONNECT FTP "+client.isConnected());*/
 		//OutputStream ow = new ByteArrayOutputStream();
 		
 		try{
	 		if(client.isConnected()){
	 			client.login(userName, password);
	 			 File file = new File(PATH); // PATH = /mnt/sdcard/download/
	             if (!file.exists()) {
	                 file.mkdirs();
	             }
	             
	             File outputFile = new File(file, getApkName());
	             if(outputFile.exists()){
                    outputFile.delete();
                }
	            client.download(fileName,outputFile);
	             /*Log.i(" DOWNLOAD Complete on SD Card.! ",""+PATH);*/
	    	}
 		}finally{
 			try{  
        		fos.close();
        		is.close();
            }catch(NullPointerException n){}
 		}
    }
    
 // Download On My Mobile SDCard or Emulator.
    public void HTTPDownloadOnSDcard() throws IOException
    {
    	FileOutputStream fos=null;
    	InputStream is=null;
    	
        try{
            URL url = new URL(getApkURL()); // Your given URL.
            /*Log.i(" INSATALL APPs ",""+url.toString());*/
            
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect(); // Connection Complete here.!

            /*Log.i(" CONNECT ","");*/
            //Toast.makeText(getApplicationContext(), "HttpURLConnection complete.", Toast.LENGTH_SHORT).show();
            /*Log.i(" DOWNLOAD PATH ",""+PATH);*/
            File file = new File(PATH); // PATH = /mnt/sdcard/download/
            if (!file.exists()) {
                file.mkdirs();
            }
            
            File outputFile = new File(file, getApkName());
            if(outputFile.exists()){
                outputFile.delete();
            }
            
          //  File outputFile = new File(file, getApkName());           
            fos = new FileOutputStream(outputFile);
            
            //      Toast.makeText(getApplicationContext(), "SD Card Path: " + outputFile.toString(), Toast.LENGTH_SHORT).show();

            is = c.getInputStream(); // Get from Server and Catch In Input Stream Object.

            /*Log.i(" InputStream ",""+is.available());*/
            
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1); // Write In FileOutputStream.
            }
            /*Log.i(" DOWNLOAD Complete on SD Card.! ",""+PATH);*/
         /*   fos.close();
            is.close();*/
            //till here, it works fine - .apk is download to my sdcard in download file.
            // So plz Check in DDMS tab and Select your Emualtor.

            //Toast.makeText(getApplicationContext(), "Download Complete on SD Card.!", Toast.LENGTH_SHORT).show();
            //download the APK to sdcard then fire the Intent.
        }finally{
        	
        	try{  
        		fos.close();
        		is.close();
            }catch(NullPointerException n){}
        
    }
}
    public void setApplicationInfoList(ArrayList<ApplicationInfo> apps){
    	this.appsInfo = apps;
    }
    public ArrayList<ApplicationInfo> getApplicationInfoList(){
    	return this.appsInfo ;
    }
     
	/**
	 * @return the versionCode
	 */
	public int getVersionCode() {
		return VersionCode;
	}

	/**
	 * @param versionCode the versionCode to set
	 */
	public void setVersionCode(int versionCode) {
		VersionCode = versionCode;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return VersionName;
	}

	/**
	 * @param versionName the versionName to set
	 */
	public void setVersionName(String versionName) {
		VersionName = versionName;
	}
	/**
	 * @return the apkURL
	 */
	public String getApkURL() {
		return apkURL;
	}
	/**
	 * @param apkURL the apkURL to set
	 */
	public void setApkURL(String apkURL) {
		this.apkURL = apkURL;
	}
	/**
	 * @return the apkName
	 */
	public String getApkName() {
		return apkName;
	}
	/**
	 * @param apkName the apkName to set
	 */
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	
	
}



