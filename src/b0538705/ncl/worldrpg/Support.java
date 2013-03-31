package b0538705.ncl.worldrpg;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;


public class Support {
	
	public static String PACKAGE_NAME;
	
	public static AgentsNotifier agentsNotifier;
	public static Scenario activeScenario;
	
	public static Context currentContext;
	
	
	//update frequency in seconds
	public static int agentsUpdateFrequency=1000;
	
	public static LatLng locationToLatLng(Location l)
	{
		return new LatLng(l.getLatitude(),l.getLongitude());
	}
	
	
	/*
	 * moves the position by that number of meters
	 */
	public static LatLng transformPositionBy(LatLng origin,double metersX, double metersY)
	{
		
		
		double tempLat = origin.latitude + ((metersX*3.2808399)/3.64)*0.00001;
		double tempLng = origin.longitude + ((metersY*3.2808399)/2.22)*0.00001;
		
		LatLng temp = new LatLng(tempLat, tempLng);
		
		return temp;
	}
	
	public static void initializeThreads()
	{
		
		Support.agentsNotifier = new AgentsNotifier(agentsUpdateFrequency);
		
		Thread agentsNotifierThread = new Thread(Support.agentsNotifier);
		agentsNotifierThread.start();
		
		
		Support.activeScenario = new Scenario();
	}

}
