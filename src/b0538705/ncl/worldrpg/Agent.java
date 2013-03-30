package b0538705.ncl.worldrpg;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Agent implements Observer {
	
	public LatLng position;
	
	public int id;
	
	public ArrayList<ActionPackage> actionStack;
	
	public Agent(int ID)
	{
		this.id = ID;
		actionStack = new ArrayList<ActionPackage>();
		
		Support.agentsNotifier.addObserver(this);
		
		int b;
	}
	
	public double getLatitude()
	{
		return position.latitude;
	}
	
	public double getLongtitude()
	{
		return position.longitude;
	}

	@Override
	public void update(Observable observable, Object data) {
		Log.d("worldrpg", "Agent no. " + id + " reporting in");
		
	}
	

}
