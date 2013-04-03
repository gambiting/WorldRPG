package b0538705.ncl.worldrpg;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;


public class Support {
	
	public static String PACKAGE_NAME;
	
	public static AgentsNotifier agentsNotifier;
	public static Scenario activeScenario;
	
	public static Context currentContext;
	
	public static DatabaseEngine databaseEngine;
	
	
	//update frequency in seconds
	public static int agentsUpdateFrequency=1000;
	
	public static LatLng locationToLatLng(Location l)
	{
		return new LatLng(l.getLatitude(),l.getLongitude());
	}
	
	
	/*
	 * moves the position by that number of meters
	 * returns the new position as a new LatLng object
	 */
	public static LatLng transformPositionBy(LatLng origin,double metersLat, double metersLon)
	{
		
		/*
		 * converts from meters to feet
		 * and then from feet to geographical coordinates(seconds)
		 */
		double tempLat = origin.latitude + ((metersLat*3.2808399)/3.64)*0.00001;
		double tempLng = origin.longitude + ((metersLon*3.2808399)/2.22)*0.00001;
		
		LatLng temp = new LatLng(tempLat, tempLng);
		
		return temp;
	}
	
	public static void initializeGeneral()
	{
		//debug
		Support.currentContext.deleteDatabase("worldrpg.db");
		Support.databaseEngine = new DatabaseEngine(Support.currentContext);
	}
	
	
	/*
	 * adds the notifiers to new threads and starts them
	 */
	public static void initializeThreads()
	{
		
		Support.agentsNotifier = new AgentsNotifier(agentsUpdateFrequency);
		
		Thread agentsNotifierThread = new Thread(Support.agentsNotifier);
		agentsNotifierThread.start();
		
		//creates a new scenario
		//TODO move somewhere else
		Support.activeScenario = new Scenario();
	}
	
	public static void initializeAssets()
	{
		//animation sprites for the agent(default)
		Agent.animationSprites1.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_1_1));
		Agent.animationSprites1.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_1_2));
		Agent.animationSprites1.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_1_3));
	}

}
