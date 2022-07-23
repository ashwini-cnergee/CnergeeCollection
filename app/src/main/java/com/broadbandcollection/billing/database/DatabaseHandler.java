package com.broadbandcollection.billing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.broadbandcollection.billing.utils.Utils;


public class DatabaseHandler extends SQLiteOpenHelper {
	Context ctx;
	SQLiteDatabase db;
	 // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "CnergeeBillingManager";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "billing";

	private static final String TABLE_LOCATION_NEW = "location_new";
    
    private static final String TABLE_LOCATION = "location";
    
    public static final String TABLE_ID_CARD = "id_card";
    
    public static final String TABLE_UPDATE_EZ  = "update_ez";
    
    // Contacts Table Columns names
   // private static final String docket_no = "docketno";
    public static final String compl_no = "complaintno";
    //private static final String compl_det = "complaint";
    public static final String compl_category = "complcategory";
    public static final String compl_id = "complid";
    public static final String user_id = "userid";
    public static final String cust_name = "custname";
    public static final String cust_add = "custadd";
    public static final String cust_phone = "custphone";
    public static final String comments = "comments";
    
    public static final String status = "status";
    public static final String date = "date";
    public static final String time = "time";
    public static final String assdate = "assdate";
    
    public static final String ROW_ID="_id";
    public static final String LATITUDE="latitude";
    public static final String LONGITUDE="longitude";
    public static final String MEMBER_ID="member_id";
    public static final String DATE="datetime";
   
    public static final String PROVIDER="provider";
    public static final String GPS_STATUS="gps_status";
    public static final String ACTIVITY="activity";

    

    public static final String name = "name";
    public static final String doj = "doj";
    public static final String dob = "dob";
    public static final String mob_no = "mob_no";
    public static final String user_address = "user_address";
    public static final String comp_address = "comp_address";
    public static final String user_img = "user_img";
    public static final String comp_img = "comp_img";
    public static final String sign_img = "sign_img";
    
    // update ezextap
    
    public static final String CU_DATE = "date";
    public static final String  FLAG =  "flag";
    public static final String KEY_ID = "_id";



    String CREATE_LOCATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION + "("
            + ROW_ID + " INTEGER PRIMARY KEY,"
            + LATITUDE + " TEXT,"
            + LONGITUDE + " TEXT,"
            + MEMBER_ID + " TEXT,"
            + DATE + " TEXT,"
            + ACTIVITY + " TEXT,"
            + PROVIDER + " TEXT,"
            + GPS_STATUS + " TEXT )";

	String CREATE_LOCATION_NEW_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION_NEW + "("
			+ ROW_ID + " INTEGER PRIMARY KEY,"
			+ LATITUDE + " TEXT,"
			+ LONGITUDE + " TEXT,"
			+ MEMBER_ID + " TEXT,"
			+ DATE + " TEXT,"
			+ PROVIDER + " TEXT,"
			+ GPS_STATUS + " TEXT ,"
	        + ACTIVITY + " TEXT )";
    
    
    String CREATE_UPDATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_UPDATE_EZ + "("
    			+ KEY_ID + " INTEGER PRIMARY KEY,"
    			+  CU_DATE +" TEXT,"+ FLAG +" TEXT )";
    
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx=context;
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		 String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
	                + compl_no + " TEXT PRIMARY KEY," 
	                + compl_category + " TEXT,"
	                + compl_id + " TEXT,"
	                + user_id + " TEXT,"
	                + cust_name + " TEXT,"
	                + cust_add + " TEXT,"
	                + cust_phone + " TEXT,"
	                + status + " TEXT,"
				    + comments + " TEXT,"
	                
	                + date + " TEXT,"
	                + time + " TEXT,"
	                + assdate + " TEXT )";
		 
	        db.execSQL(CREATE_CONTACTS_TABLE);
	        db.execSQL(CREATE_LOCATION_TABLE);
		    db.execSQL(CREATE_LOCATION_NEW_TABLE);
	        db.execSQL(CREATE_UPDATE_TABLE);
	        Utils.log("Table Created","successfully");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPDATE_EZ);
        
        Utils.log("On UPgrade ",""+TABLE_UPDATE_EZ);
     //Create tables again
        
        onCreate(db);
        
	}

public void open() throws SQLiteException{
		
		db=this.getWritableDatabase();
	}
	
	public void close() {
		Utils.log("Database","Closed");
		this.close();
	}
	
	public long insertLocation(String str_latitude,String str_longitude,String str_memid,String str_date,String str_provider,String str_gps_status,String str_activity){
	SQLiteDatabase db= this.getWritableDatabase();
		long i=-1;
		ContentValues cv = new ContentValues();
		cv.put(LATITUDE, str_latitude);
		cv.put(LONGITUDE, str_longitude);
		cv.put(MEMBER_ID, str_memid);
		cv.put(DATE, str_date);
		
		cv.put(PROVIDER, str_provider);
		cv.put(GPS_STATUS, str_gps_status);
		cv.put(ACTIVITY, str_activity);
		i=db.insert(TABLE_LOCATION, null, cv);
		//db.close();
		return i;
	}

	public long insertNewLocation(String str_latitude,String str_longitude,String str_memid,String str_date,
								  String str_provider,String str_gps_status,String activity){
		SQLiteDatabase db= this.getWritableDatabase();
		long i=-1;
		ContentValues cv = new ContentValues();
		cv.put(LATITUDE, str_latitude);
		cv.put(LONGITUDE, str_longitude);
		cv.put(MEMBER_ID, str_memid);
		cv.put(DATE, str_date);
		cv.put(PROVIDER, str_provider);
		cv.put(GPS_STATUS, str_gps_status);
		cv.put(ACTIVITY, activity);

		i=db.insert(TABLE_LOCATION_NEW, null, cv);
		//db.close();

		Log.e("Data location inserted",":"+i);
		return i;
	}

	
	public Cursor getLocation(){
		SQLiteDatabase db= this.getWritableDatabase();
		Cursor mCur = null;
		mCur=db.query(TABLE_LOCATION, null, null, null, null, null, null);
		//Utils.log("Count is",":"+mCur.getCount());
		//db.close();
		return mCur;
		
	}
	public Cursor getLocationNew(){
		SQLiteDatabase db= this.getWritableDatabase();
		Cursor mCur = null;
		mCur=db.query(TABLE_LOCATION_NEW, null, null, null, null, null, null);
		//Utils.log("Count is",":"+mCur.getCount());
		//db.close();
		return mCur;

	}


	public Cursor getLastRecord(){
		SQLiteDatabase db= this.getReadableDatabase();
		Cursor mCur = null;
		String query = TABLE_LOCATION_NEW+ " ORDER BY "+ROW_ID +" DESC LIMIT 1";
		mCur=db.query(query, null, null, null, null, null, null);
		return mCur;
	}

	public int deleteAllRow(){
		SQLiteDatabase db= this.getWritableDatabase();
		//db.execSQL("delete from "+ TABLE_LOCATION_NEW);
		int i=db.delete(TABLE_LOCATION_NEW, null, null);
        return i;
	}
	
	public void DeleteRow(String row_id){
		SQLiteDatabase db= this.getWritableDatabase();
		int i=db.delete(TABLE_LOCATION, ROW_ID+"=?", new String[]{row_id});
	    //Utils.log("Row Deleted","is: "+i);
		//db.close();
		return;
	}
	
	//Ezetap Inserting 
	   public void addEzetap(String date) {  
	        SQLiteDatabase db = this.getWritableDatabase();  
	   
	        ContentValues values = new ContentValues();  
	        values.put(CU_DATE,date); // Contact Name  
	        values.put(FLAG, 1); // Contact Phone  
	   
	        // Inserting Row  
	        db.insert(TABLE_UPDATE_EZ, null, values);  
	        
	        //2nd argument is String containing nullColumnHack  
	       // db.close(); // Closing database connection
	    } 
	  
	   
	   public void updateEzetap(String date){
		   
		   SQLiteDatabase db = this.getWritableDatabase();  
		   
	        ContentValues values = new ContentValues();  
	        values.put(CU_DATE,date); // Contact Name  
	        values.put(FLAG, 1); // Contact Phone  
	   
	        // Inserting Row  
	        db.update(TABLE_UPDATE_EZ, values, null, null);
	        //2nd argument is String containing nullColumnHack  
	       // db.close(); // Closing database connection
	   
	   }
	   
	   
	   
	   
	   public Cursor readingEzetap() {
		    SQLiteDatabase db = this.getReadableDatabase();
		    
				    
		 /*
		    Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
		            KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
		            new String[] { String.valueOf(id) }, null, null, null, null);*/
		    
		/*    Cursor cursor = db.query(TABLE_UPDATE_EZ, new String[]{ROW_ID, CU_DATE, FLAG }, ROW_ID+ "=1", null, null, null, null);*/
	
		    
		    
		    Cursor cursor = db.query(TABLE_UPDATE_EZ,null,null,null,null,null,null);
		  
		 Utils.log("Cursor Eztap","size:"+cursor.getCount());
		 
		    // return contact
		    return cursor;
		}
	
	public void createIdCardTable() {

		SQLiteDatabase db = this.getWritableDatabase();
		
		String CREATE_ID_CARD_TABLE = "CREATE TABLE IF NOT EXISTS" + TABLE_ID_CARD + "("
				+ "_id" + " TEXT PRIMARY KEY AUTOINCREMENT," + name + " TEXT,"
				+ doj + " TEXT," + dob + " TEXT," + mob_no + " TEXT,"
				+ user_address + " TEXT," + comp_address + " TEXT," + user_img
				+ " TEXT," + comp_img + " TEXT," + sign_img + " TEXT )";

		db.execSQL(CREATE_ID_CARD_TABLE);
	}
	public boolean exists(String table) {
	    try {
	    	db = this.getWritableDatabase();
	    	Cursor mCur = null;
			mCur=db.query(table, null, null, null, null, null, null);
	         return true;
	    } catch (SQLException e) {
	         return false;
	    }
	}




}
