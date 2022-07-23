package com.broadbandcollection.billing.obj;

import android.content.Context;


import com.broadbandcollection.billing.utils.Utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class InternalStorage{
	 
	
	
	
	   private InternalStorage() {}
	 
	   public static void writeObject(Context context, String key, Object s) throws IOException {
		   
		   Utils.log("In Internal Storage","internal Storage Executed");
	      FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
	      ObjectOutputStream oos = new ObjectOutputStream(fos);
	      oos.writeObject(s);
	      oos.close();
	      fos.close();
	   }
	 
	   public static String readObject(Context context, String key) throws IOException,
	         ClassNotFoundException {
	      FileInputStream fis = context.openFileInput(key);
	      ObjectInputStream ois = new ObjectInputStream(fis);
	      String s =String.valueOf(ois.readObject());
	      return s;
	   }
	}
