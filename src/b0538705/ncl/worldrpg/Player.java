package b0538705.ncl.worldrpg;

import com.google.android.gms.maps.model.LatLng;

public class Player {

	public LatLng position;
	
	public static Player instance;
	
	public Player()
	{
		
	}
	
	public void locationUpdate(LatLng location)
	{
		
		Player.instance.position = location;
		
		/*
		 * add points in the 3x3 grid around the player
		 * xxx
		 * xxx
		 * xxx
		 */
		Support.databaseEngine.addPointToDatabase(location);
		
		
		//calculate latitude offset
		double latOffset = ((DatabaseEngine.rangeOfInfluence*2*3.2808399)/3.64)*0.00001;
		//calculate longitude offset
		double lonOffset = ((DatabaseEngine.rangeOfInfluence*2*3.2808399)/3.64)*0.00001;
		
		//temporary variable for new locations
		LatLng tempLocation;
		
		//first point - left upper corner
		tempLocation = new LatLng(location.latitude-latOffset, location.longitude-lonOffset);
		Support.databaseEngine.addPointToDatabase(tempLocation);
		//second - upper middle
		tempLocation = new LatLng(location.latitude, location.longitude-lonOffset);
		Support.databaseEngine.addPointToDatabase(tempLocation);
		//third - upper right corner
		tempLocation = new LatLng(location.latitude+latOffset, location.longitude-lonOffset);
		Support.databaseEngine.addPointToDatabase(tempLocation);
		//fourth - left middle
		tempLocation = new LatLng(location.latitude-latOffset, location.longitude);
		Support.databaseEngine.addPointToDatabase(tempLocation);
		//fifth - right middle
		tempLocation = new LatLng(location.latitude+latOffset, location.longitude);
		Support.databaseEngine.addPointToDatabase(tempLocation);
		//sixth - bottom left corner
		tempLocation = new LatLng(location.latitude-latOffset, location.longitude+lonOffset);
		Support.databaseEngine.addPointToDatabase(tempLocation);
		//seventh - bottom middle
		tempLocation = new LatLng(location.latitude, location.longitude+lonOffset);
		Support.databaseEngine.addPointToDatabase(tempLocation);
		//eight - bottom right corner
		tempLocation = new LatLng(location.latitude+latOffset, location.longitude+lonOffset);
		Support.databaseEngine.addPointToDatabase(tempLocation);
		
	}
	
	public double getLatitude()
	{
		return position.latitude;
	}
	
	public double getLongtitude()
	{
		return position.longitude;
	}
}
