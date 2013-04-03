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
		
		double rangeLat = ((rangeOfInfluence*3.2808399)/3.64)*0.00001;
		double rangeLon = ((rangeOfInfluence*3.2808399)/2.22)*0.00001;
		
		double latitude = location.latitude;
		double longitude = location.longitude;
		
		String query = 
				"select * from POINTS where " + (latitude-rangeLat) + " < latitude AND "
				+ (latitude+rangeLat) + " > latitude AND "
				+ (longitude-rangeLon) + " < longitude AND "
				+ (longitude+rangeLon) + " > longitude ;";
		
		SQLiteDatabase db = localDatabase.getReadableDatabase();
		
		Cursor result = db.rawQuery(query,null);
		
		if(result.getCount()>0)
		{
			Log.d("worldrpg-database", "there are other points around!");
			return true;
		}else
		{
			Log.d("worldrpg-database", "there are no other points around!");
			return false;
		}

	}
	
}
