package b0538705.ncl.worldrpg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.Resources;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Item {


	public String name;
	public ArrayList<ActionMethod> methodsToRun;

	public LatLng position;

	public String useOn;
	public String description;

	public Integer numberPerSpawningLocation;

	public Agent agentToUseOn;

	public String iconName;
	public BitmapDescriptor itemIcon;

	public boolean agentCompatible;
	public boolean playerCompatible;

	private MarkerOptions markerOptions;
	private Marker marker;

	public boolean changed = true;



	private SpawningLocation parentSpawningLocation;

	public static HashMap<String, BitmapDescriptor> itemIcons = new HashMap<String, BitmapDescriptor>();

	//only used by the template items
	public Item()
	{

	}

	/*
	 * create an item as as copy of another source item
	 */
	public Item(SpawningLocation parent, Item source)
	{
		this.parentSpawningLocation = parent;

		this.agentCompatible = source.agentCompatible;
		this.playerCompatible = source.playerCompatible;
		this.description = source.description;
		this.iconName = source.iconName;
		this.name = source.name;
		this.methodsToRun = (ArrayList<ActionMethod>) source.methodsToRun.clone();
		this.position = source.position;

		//if the itemIcons map does not contain the icon for this specific item yet, create it and insert into the map
		if(!itemIcons.containsKey(iconName))
		{
			//get the resource with a given name
			int iconID = Support.currentContext.getResources().getIdentifier(iconName, "drawable", Support.currentContext.getPackageName());
			itemIcons.put(iconName,BitmapDescriptorFactory.fromResource(iconID));

		}

		//get the icon from the map
		this.itemIcon = itemIcons.get(iconName);

		//initialize the position of the item
		initializePosition();
	}



	private void initializePosition()
	{
		LatLng tempPosition = parentSpawningLocation.position;
		this.position = Support.transformPositionBy(tempPosition, Math.random()*Support.activeScenario.spawningLocationWidth*2-(Support.activeScenario.spawningLocationWidth)
				, Math.random()*Support.activeScenario.spawningLocationWidth*2-(Support.activeScenario.spawningLocationWidth));
		this.updateMarker();
	}

	//since items generally don't move, this method should only be called once. For simplicity's sake, this uses the Agent class implementation
	// and adds some flexibility in case I somehow want to move the item
	public void updateMarker()
	{
		//post a runnable for the new thread
		MainActivity.mainHandler.post(new Runnable() {

			@Override
			public void run() {
				for(SpawningLocation sl: Support.activeSpawningLocations)
				{
					for(Item i:sl.activeItems)
					{
						if(i.changed)
						{
							//this is the only place to safely call this function
							//it modifies the map, so it can only be run on the main thread
							i.updateMarkerOnMap();
							//set item changed to false
							i.changed=false;
						}
					}
				}

			}
		});
	}

	public void updateMarkerOnMap()
	{
		// if marker already exists, remove it
		if(marker!=null)
		{
			marker.remove();	
		}

		markerOptions = new MarkerOptions().position(this.position);

		///set the icon for the item
		markerOptions.icon(this.itemIcon);
		markerOptions.title(this.name);
		markerOptions.snippet(this.description);
		marker = MapHandler.mMap.addMarker(markerOptions);


	}


	public void useOnAgent(Agent agent)
	{
		useOn = "Agent";
		agentToUseOn = agent;
		this.use();
	}

	public void useOnPlayer()
	{
		useOn = "Player";
		this.use();
	}

	/*
	 * run all the methods associated with this item
	 */
	private void use()
	{
		for(ActionMethod am:methodsToRun)
		{
			try {
				Class agentClass = Class.forName("b0538705.ncl.worldrpg.Item");

				Class[] par=null;
				//get the parameter classes
				if(am.parameters!=null && am.parameters.length > 0)
				{
					//make a new array for the parameter classes
					par = new Class[am.parameters.length];
					//fill the array with all the parameter classes
					for(int i=0;i<am.parameters.length;i++)
					{
						par[i] = am.parameters[i].getClass();
					}
				}

				//get the method with given name and specify the classes of the parameters(if method does not take any
				//parameters,then par=null
				Method method = agentClass.getMethod(am.name, par);

				method.invoke(this, (Object[])am.parameters);


			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public void heal(Integer value)
	{
		if(useOn.equals("Agent"))
		{

		}else if(useOn.equals("Player"))
		{

		}
	}

}
