package b0538705.ncl.worldrpg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class DatabaseEngine {

	public String databaseToUse = "local";
	
	public static LocalDatabase localDatabase;
	
	
	
	public DatabaseEngine(Context context)
	{
		DatabaseEngine.localDatabase = new LocalDatabase(context);
	}
	
	public boolean addPointToDatabase(LatLng location)
	{
		if(!areAnyPointsWithinRange(location))
		{
			
			//write that location to the database
			SQLiteDatabase db = DatabaseEngine.localDatabase.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put("latitude", location.latitude);
			values.put("longitude", location.longitude);
			values.put("normal", Support.activeScenario.normal);
			values.put("infected", Support.activeScenario.infected);
			values.put("panicked", Support.activeScenario.panicked);
			
			
			long id = db.insert("POINTS",null, values);
			
			db.close();
			
			//add the inserted location into the game,with default values
			Support.activeSpawningLocations.add(new SpawningLocation(id, location));
		}
		
		
		return false;
	}
	
	public void updatePointInTheDatabase(SpawningLocation sl)
	{
		//update the given location
		SQLiteDatabase db = DatabaseEngine.localDatabase.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("normal", sl.normal);
		values.put("infected", sl.infected);
		values.put("panicked", sl.panicked);
		
		long id = sl.id;
		
		db.update("POINTS", values, "_id=" + id, null);
		
		db.close();
	}
	
	public boolean areAnyPointsWithinRange(LatLng location)
	{
		
		//convert meters to geographical coordinates
		double rangeLat = ((Support.activeScenario.spawningLocationWidth*3.2808399)/3.64)*0.00001;
		double rangeLon = ((Support.activeScenario.spawningLocationWidth*3.2808399)/2.22)*0.00001;
		
		double latitude = location.latitude;
		double longitude = location.longitude;
		
		//form a query that will return only points within the range
		String query = 
				"select * from POINTS where " + (latitude-rangeLat) + " <= latitude AND "
				+ (latitude+rangeLat) + " >= latitude AND "
				+ (longitude-rangeLon) + " <= longitude AND "
				+ (longitude+rangeLon) + " >= longitude "
				+ ";"; 
		
		
		//get the database
		SQLiteDatabase db = localDatabase.getReadableDatabase();
		
		//execute query
		Cursor result = db.rawQuery(query,null);
		
		
		if(result.getCount()>0)
		{
			//check all points within range to see if they were already added, if not add them
			//move to the next result, quit the loop if there are no more left
			while(result.moveToNext())
			{
				//get the id
				long tempID = result.getLong(0);
				if(!DatabaseEngine.doesSpawningLocationExists(tempID))
				{
					//if there is no such location added yet to the game,add it:
					double tempLatitude = result.getDouble(1);
					double tempLongitute = result.getDouble(2);
					int tempNormal = result.getInt(3);
					int tempInfected = result.getInt(4);
					int tempPanicked = result.getInt(5);
					
					SpawningLocation tempSpawningLocation = new SpawningLocation(tempID, new LatLng(tempLatitude,tempLongitute), tempNormal, tempInfected, tempPanicked);
					Support.activeSpawningLocations.add(tempSpawningLocation);
					
					Support.printDebug("worldrpg-database", "point existed in the database - loaded");
				}
				
				
				
				
				
			}
			
			db.close();
			result.close();
			
			return true;
		}else
		{
			db.close();
			result.close();
			Support.printDebug("worldrpg-database", "point did not exist - created in the database");
			return false;
		}

	}
	
	public static boolean doesSpawningLocationExists(long id)
	{
		for(SpawningLocation sp: Support.activeSpawningLocations)
		{
			if(sp.id == id)
			{
				return true;
			}
		}
		return false;
	}
	
}
