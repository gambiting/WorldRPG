package b0538705.ncl.worldrpg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

	public static BlockingQueue<SpawningLocation> activeSpawningLocations = new LinkedBlockingQueue<SpawningLocation>();


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

		//initialize local database
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

		//load a scenario
		Support.activeScenario = YamlHandler.loadScenario(Support.currentContext.getResources().openRawResource(R.raw.scenario_disease_1));
	}

	public static void initializeAssets()
	{
		//animation sprites for the agent(default)
		Agent.animationSpritesNormal.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_1_1));
		Agent.animationSpritesNormal.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_1_2));
		Agent.animationSpritesNormal.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_1_3));

		Agent.animationSpritesInfected.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_2_1));
		Agent.animationSpritesInfected.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_2_2));
		Agent.animationSpritesInfected.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_2_3));

		Agent.animationSpritesPanicked.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_3_1));
		Agent.animationSpritesPanicked.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_3_2));
		Agent.animationSpritesPanicked.add(BitmapDescriptorFactory.fromResource(R.drawable.npc_3_3));
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

	/*
	 * removes all spawn points and their associated agents that are further away than a given distance
	 */
	public static void clipSpawningLocations(double distance)
	{
		Iterator<SpawningLocation> it = Support.activeSpawningLocations.iterator();

		//iterate through all active spawning location elements
		while(it.hasNext())
		{
			SpawningLocation tempSpawningLocation = it.next();
			//if the distance between the spawning location and the player is smaller than the given distance,remove the spawning location
			if(distanceBetweenTwoPoints(tempSpawningLocation.position, Player.instance.position) > distance)
			{
				//call clean up first
				tempSpawningLocation.cleanUp();
				//remove the element
				it.remove();
			}
		}
	}
	
	/*
	 * returns all agents withing a certain radius with a given state
	 */
	public static BlockingQueue<Agent> returnAgentsWithinRadius(LatLng origin, double radius, String state, int limit)
	{
		BlockingQueue<Agent> tempAgents = new LinkedBlockingQueue<Agent>();
		
		for(SpawningLocation sl:Support.activeSpawningLocations)
		{
			for(Agent a:sl.activeAgents)
			{
				if(a.state.equals(state) && (Support.distanceBetweenTwoPoints(origin, a.position)<radius))
				{
					tempAgents.add(a);
					
					//if the limit was reached, then quit the function
					// if the limit is equal to 0 then return all agents within radius
					if(limit!=0 && tempAgents.size()==limit)
					{
						return tempAgents;
					}
				}
			}
		}
		
		return tempAgents;
	}
}
