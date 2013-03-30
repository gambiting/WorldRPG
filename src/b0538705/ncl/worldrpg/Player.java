package b0538705.ncl.worldrpg;

import com.google.android.gms.maps.model.LatLng;

public class Player {

	public LatLng position;
	
	public static Player instance;
	
	public Player()
	{
		
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
