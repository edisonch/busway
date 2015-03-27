package com.scandiumsc.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class BuswayHelper extends SQLiteOpenHelper 
{
	private static final String DATABASE_NAME="busway.db";
	private static final int SCHEMA_VERSION=1;
	
	public BuswayHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE halte_busway (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, lat REAL, lon REAL, line REAL, pole REAL);");
		db.execSQL("CREATE TABLE home_neighbour (_id INTEGER PRIMARY KEY AUTOINCREMENT, home_id REAL, neighbour_id REAL);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
//		db.execSQL("ALTER TABLE halte_busway ADD COLUMN lat TEXT");
//		db.execSQL("ALTER TABLE halte_busway ADD COLUMN lon TEXT");
	}
	
	public Cursor getAllHalte()
	{
		return(getReadableDatabase().rawQuery("SELECT _id, name, lat, lon, line, pole FROM halte_busway ORDER BY name",null));
	}
	
	public Cursor getByHalteId(String id) 
	{
		String[] args={id};
		return(getReadableDatabase().rawQuery("SELECT _id, name, lat, lon, line, pole FROM halte_busway WHERE _ID=?", args));
	}
	
	public Cursor getAllRoute()
	{
		return(getReadableDatabase().rawQuery("SELECT _id, home_id, neighbour_id FROM home_neighbour ORDER BY _id",null));
	}
	
	public Cursor getAllNeighbour()
	{
		return(getReadableDatabase().rawQuery("SELECT _id, home_id, neighbour_id FROM home_neighbour ORDER BY home_id",null));
	}
	
	public void insertHalte(String name, double lat, double lon, double line, int pole) 
	{
		ContentValues cv=new ContentValues();
		
		cv.put("name", name);
		cv.put("lat", lat);
		cv.put("lon", lon);
		cv.put("line", line);
		cv.put("pole", pole);
		
		getWritableDatabase().insert("halte_busway", "name", cv);
	}
	
	public void insertHomeAndNeighbour(double name, double neighbour) 
	{
		ContentValues cv=new ContentValues();
		
		cv.put("home_id", name);
		cv.put("neighbour_id", neighbour);
		
		getWritableDatabase().insert("home_neighbour", "name", cv);
	}

	public Integer getId(Cursor c) 
	{
		return(c.getInt(1));
	}
	
	public String getName(Cursor c) 
	{
		return(c.getString(2));
	}
	
	public double getLatitude(Cursor c) 
	{
		return(c.getDouble(3));
	}
	
	public double getLongitude(Cursor c) 
	{
		return(c.getDouble(4));
	}
	
	public double getLine(Cursor c) 
	{
		return(c.getDouble(5));
	}
	
	public double getPole(Cursor c) 
	{
		return(c.getDouble(6));
	}
}
