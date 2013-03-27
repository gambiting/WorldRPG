package b0538705.ncl.worldrpg;

import com.google.android.gms.maps.model.LatLng;

public class Agent {
	
	public LatLng position;
	
	public ArrayList<ActionPackage> actionStack;
	
	public Agent()
	{
		actionStack = new ArrayList<ActionPackage>();
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
