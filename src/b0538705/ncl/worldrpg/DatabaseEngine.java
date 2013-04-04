package b0538705.ncl.worldrpg;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class DatabaseEngine {

	public String databaseToUse = "local";
	
	public static LocalDatabase localDatabase;
	
	//range per single point - in meters
	public static int rangeOfInfluence=200;
	
	
	public DatabaseEngine(Context context)
	{
		DatabaseEngine.localDatabase = new LocalDatabase(context);
	}
	
	public boolean addPointToDatabase(LatLng location)
	{
		if(!areAnyPointsWithinRange(location))
		{
			SQLiteDatabase db = DatabaseEngine.localDatabase.getWritableDatabase();
			
			db.execSQL("insert into POINTS VALUES(null,'" + location.latitude + "','" + location.longitude + "', 0 , 0);");
		}
		
		
		return false;
	}
	
	public boolean areAnyPointsWithinRange(LatLng location)
	{
		
		//convert meters to geographical coordinates
		double rangeLat = ((rangeOfInfluence*3.2808399)/3.64)*0.00001;
		double rangeLon = ((rangeOfInfluence*3.2808399)/2.22)*0.00001;
		
		double latitude = location.latitude;
		double longitude = location.longitude;
		
		//form a query that will return only points within the range
		//limit 1, since we only need to know if there are any, not how many of them
		String query = 
				"select * from POINTS where " + (latitude-rangeLat) + " < latitude AND "
				+ (latitude+rangeLat) + " > latitude AND "
				+ (longitude-rangeLon) + " < longitude AND "
				+ (longitude+rangeLon) + " > longitude "
				+ "LIMIT 1;"; 
		
		
		//get the database
		SQLiteDatabase db = localDatabase.getReadableDatabase();
		
		//execute query
		Cursor result = db.rawQuery(query,null);
		
		
		if(result.getCount()>0)
		{
			db.close();
			result.close();
			Support.printDebug("worldrpg-database", "point exists for your location - not created");
			return true;
		}else
		{
			db.close();
			result.close();
			Support.printDebug("worldrpg-database", "point created in the database");
			return false;
		}

	}
	
}
