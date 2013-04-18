package b0538705.ncl.worldrpg;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Player {

	public LatLng position;
	
	public static Player instance;
	
	public static BitmapDescriptor playerIcon;
	
	private MarkerOptions markerOptions;
	private Marker marker;
	
	private Circle circle;
	private int circleFill;
	private int circleOutline;
	public int range=30;
	
	public Player()
	{
		circleFill = Color.argb(80, 102, 204, 255);
		circleOutline = Color.argb(200, 40, 40, 255);
		
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
		
		//middle point,at the player's location
		Support.databaseEngine.addPointToDatabase(location);
		
		
		//calculate latitude offset
		double latOffset = ((Support.activeScenario.spawningLocationWidth*2*3.2808399)/3.64)*0.00001;
		//calculate longitude offset
		double lonOffset = ((Support.activeScenario.spawningLocationWidth*2*3.2808399)/2.22)*0.00001;
		
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
		
		if(circle!=null)
			circle.remove();
		
		//add the circle for the range
		circle = MapHandler.mMap.addCircle(new CircleOptions().center(location)
				.fillColor(circleFill)
				.strokeColor(circleOutline)
				.radius(range));
		
		
		//add the marker for the player
		if(marker!= null)
			marker.remove();
		
		markerOptions = new MarkerOptions().position(location)
				.icon(Player.playerIcon)
				.anchor(0.5f, 0.5f);
		marker = MapHandler.mMap.addMarker(markerOptions);
		
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
