package com.broadbandcollection.billing.obj;

import android.content.Context;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class InternalStorage_Inform{

	   private InternalStorage_Inform() {}
	 
	   public static void writeObject(Context context, String key, String s) throws IOException {

	      FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
	      ObjectOutputStream oos = new ObjectOutputStream(fos);
	      oos.writeObject(s);
	      oos.close();
	      fos.close();
	   }
	 
	   public static Object readObject(Context context, String key) throws IOException,
	         ClassNotFoundException {
	      FileInputStream fis = context.openFileInput(key);
	      ObjectInputStream ois = new ObjectInputStream(fis);
	      Object s =ois.readObject();
	      return s;
	   }
	}
