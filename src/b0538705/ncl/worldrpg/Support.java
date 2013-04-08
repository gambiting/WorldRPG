package b0538705.ncl.worldrpg;

import java.util.ArrayList;

import org.apache.http.util.LangUtils;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;


public class Support {
	
	public static String PACKAGE_NAME;
	
	public static AgentsNotifier agentsNotifier;
	public static Scenario activeScenario;
	
	public static Context currentContext;
	
	public static DatabaseEngine databaseEngine;
	
	public static TextView debugView;
	public static ScrollView debugScroller;
	
	public static ArrayList<SpawningLocation> activeSpawningLocations = new ArrayList<SpawningLocation>();
	
	
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
		//Support.currentContext.deleteDatabase("worldrpg.db");
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
	
	public static void printDebug(String source, String message)
	{
		Support.debugView.append(source + " - " + message + "\n");
		Support.debugScroller.smoothScrollTo(0, Support.debugView.getBottom());
		
		Log.d(source, message);
	}
	
	public static double distanceBetweenTwoPoints(double latitude1, double longitude1, double latitude2, double longitude2)
	{
		Location l1 = new Location("point 1");
		l1.setLatitude(latitude1);
		l1.setLongitude(longitude1);
		
		Location l2 = new Location("point 2");
		l2.setLatitude(latitude2);
		l2.setLongitude(longitude2);
		
		return l1.distanceTo(l2);
	}
	
	public static double distanceBetweenTwoPoints(LatLng position1, LatLng position2)
	{
		return distanceBetweenTwoPoints(position1.latitude, position1.longitude, position2.latitude, position2.longitude);
	}

}
