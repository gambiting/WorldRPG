package b0538705.ncl.worldrpg;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;


public class Support {
	
	public static String PACKAGE_NAME;
	
	public static AgentsNotifier agentsNotifier;
	public static Scenario activeScenario;
	
	
	//update frequency in seconds
	public static int agentsUpdateFrequency=1000;
	
	public static LatLng locationToLatLng(Location l)
	{
		return new LatLng(l.getLatitude(),l.getLongitude());
	}
	
	
	public static void initializeThreads()
	{
		
		
		
		
		Support.agentsNotifier = new AgentsNotifier(agentsUpdateFrequency);
		
		Thread agentsNotifierThread = new Thread(Support.agentsNotifier);
		agentsNotifierThread.start();
		
		
		Support.activeScenario = new Scenario();
	}

}
