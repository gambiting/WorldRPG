package b0538705.ncl.worldrpg;

import java.util.ArrayList;

import android.location.Location;

public class Agent {
	
	public Location position;
	
	public ArrayList<ActionPackage> actionStack;
	
	public Agent()
	{
		actionStack = new ArrayList<ActionPackage>();
	}
	
	public double getLatitude()
	{
		return position.getLatitude();
	}
	
	public double getLongtitude()
	{
		return position.getLongitude();
	}
	

}
