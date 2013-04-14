package b0538705.ncl.worldrpg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Agent implements Observer {

	public static ArrayList<BitmapDescriptor> animationSpritesNormal = new ArrayList<BitmapDescriptor>();
	public static ArrayList<BitmapDescriptor> animationSpritesInfected = new ArrayList<BitmapDescriptor>();
	public static ArrayList<BitmapDescriptor> animationSpritesPanicked = new ArrayList<BitmapDescriptor>();

	public LatLng position;
	public ArrayList<ActionPackage> actionStack;
	public ActionPackage activeActionPackage;

	private MarkerOptions markerOptions;
	private Marker marker;

	public boolean changed=false;

	public String state;

	public ArrayList<BitmapDescriptor> animationSprites;

	private int currentSprite;

	public SpawningLocation parentSpawningLocation;

	
	public HashMap<String, Long> recentRunTimes;
	
	public Agent agentToRunAwayFrom;
	public Agent agentToFollow;

	public Agent()
	{

	}

	public Agent(SpawningLocation parent, String State)
	{
		actionStack = new ArrayList<ActionPackage>();
		recentRunTimes = new HashMap<String,Long>();
		this.parentSpawningLocation = parent;
		initializePosition();

		this.state = State;

		/*
		 * debug code
		 */
		//ActionPackage tempPackage = new ActionPackage();
		//tempPackage.methodsToRun.add(new ActionMethod("moveAbout", null));
		//activeActionPackage = tempPackage;

		if(state.equals("normal"))
		{
			activeActionPackage = Support.activeScenario.agentTemplate.getActionPackageContainingName("default");
		}else if(state.equals("infected"))
		{
			activeActionPackage = Support.activeScenario.agentTemplate.getActionPackageContainingName("infected");
		}else if(state.equals("panicked"))
		{
			activeActionPackage = Support.activeScenario.agentTemplate.getActionPackageContainingName("panicked");
		}

		currentSprite = (int)(Math.random()*3.5);

		//add itself as an observer
		Support.agentsNotifier.addObserver(this);

	}

	@SuppressWarnings("unchecked")
	public void useSprites(ArrayList<BitmapDescriptor> sprites)
	{
		this.animationSprites = (ArrayList<BitmapDescriptor>) sprites.clone();
		//reset the sprite counter
		currentSprite = (int)(Math.random()*(this.animationSprites.size()-1));
	}

	private void initializePosition()
	{
		LatLng tempPosition = parentSpawningLocation.position;
		this.position = Support.transformPositionBy(tempPosition, Math.random()*Support.activeScenario.spawningLocationWidth*2-(Support.activeScenario.spawningLocationWidth)
				, Math.random()*Support.activeScenario.spawningLocationWidth*2-(Support.activeScenario.spawningLocationWidth));
		this.updateMarker();

	}


	public void updateMarker()
	{
		//updates sprites where needed
		//first check if the animation sprites were initialized yet
		if(this.animationSprites!=null)
		{
			if(this.state.equals("normal") && !this.animationSprites.equals(Agent.animationSpritesNormal))
			{
				this.useSprites(Agent.animationSpritesNormal);
			}else if(this.state.equals("infected") && !this.animationSprites.equals(Agent.animationSpritesInfected))
			{
				this.useSprites(Agent.animationSpritesInfected);
			}else if(this.state.equals("panicked") && !this.animationSprites.equals(Agent.animationSpritesPanicked))
			{
				this.useSprites(Agent.animationSpritesPanicked);
			}
		}

		//post a runnable for the new thread
		MainActivity.mainHandler.post(new Runnable() {

			@Override
			public void run() {
				for(SpawningLocation sl: Support.activeSpawningLocations)
				{
					for(Agent a:sl.activeAgents)
					{
						if(a.changed)
						{
							//this is the only place to safely call this function
							//it modifies the map, so it can only be run on the main thread
							a.updateMarkerOnMap();
							//set agent changed to false
							a.changed=false;
						}
					}
				}

			}
		});
	}

	/*
	 * !!!!!!!!!
	 * run ONLY from main thread, never call directly!
	 * updates the map, must be run from the main thread
	 * !!!!!!!!!
	 */
	public void updateMarkerOnMap()
	{
		// if marker already exists, remove it
		if(marker!=null)
		{
			marker.remove();	
		}

		markerOptions = new MarkerOptions().position(this.position);


		//cycle through the animation sprites if there are any
		//if there is none, the default Google Marker icon will be used
		if(this.animationSprites.size()>0)
		{
			markerOptions.icon(this.animationSprites.get(currentSprite));
			currentSprite = (currentSprite+1)%3;
		}




		marker = MapHandler.mMap.addMarker(markerOptions);

	}

	public double getLatitude()
	{
		return position.latitude;
	}

	public double getLongtitude()
	{
		return position.longitude;
	}


	/*
	 * randomly move about
	 */
	public void moveAbout()
	{
		this.position = Support.transformPositionBy(this.position, Math.random()*4.0-2.0, Math.random()*4.0-2.0);
		this.changed = true;
		this.updateMarker();

	}
	
	public void infect(Agent agent)
	{
		agent.state="infected";
		agent.activeActionPackage=Support.activeScenario.agentTemplate.getActionPackageContainingName("infected");
		agent.changed=true;
		agent.updateMarker();
	}
	
	/*
	 * finds a nearest agent with a given state and within a given radius to follow
	 */
	public void updateAgentToFollow(Integer radius, String state)
	{
		this.agentToFollow = Support.returnAgentNearestLocation(this.position, radius, state);
	}

	/*
	 * follows an agent with a given state, that is within a given radius
	 */
	public void followAgentWithinRadius(String state, Integer radius)
	{

		//check if there is any agent to follow
		if(agentToFollow!=null)
		{
			//if yes, move closer
			this.position = Support.transformPositionBy(this.position,
					(this.position.latitude>agentToFollow.position.latitude)?(-1):(1),
							(this.position.longitude>agentToFollow.position.longitude)?(-1):(1));

			this.changed = true;
			this.updateMarker();

		}else
		{
			//if there isn't an agent to follow, move randomly
			this.moveAbout();

		}
	}
	
	/*
	 * infects the followed agent if it is closer than the given radius
	 */
	public void infectFollowedAgentIfWithinRadius(Integer radius)
	{
		//if the infected agent is within 
		if(agentToFollow!=null && Support.distanceBetweenTwoPoints(this.position, agentToFollow.position)<radius)
		{
			infect(agentToFollow);
			
			agentToFollow=null;
		}
	}
	
	
	/*
	 * infects everyone within certain radius
	 * run infrequently, since it's expensive to run
	 */
	public void infectAllWithinRadius(Integer radius)
	{
		for(Agent a:Support.returnAgentsWithinRadius(this.position, radius, "normal", 0))
		{
			this.infect(a);
		}
	}
	
	
	/*
	 * updates the agentToRunAwayFrom used to run away by panicking agents
	 * run infrequently, since the function is expensive to run
	 */
	public void updateAgentToRunAwayFrom(Integer radius, String state)
	{
		this.agentToRunAwayFrom = Support.returnAgentNearestLocation(this.position, radius, state);
	}
	
	
	/*
	 * runs away from the set agentToRunAwayFrom
	 */
	public void runAwayFromSetAgent()
	{
		if(this.agentToRunAwayFrom!=null)
		{
			this.position = Support.transformPositionBy(this.position,
					(this.position.latitude<agentToRunAwayFrom.position.latitude)?(-1):(1),
							(this.position.longitude<agentToRunAwayFrom.position.longitude)?(-1):(1));

			this.changed = true;
			this.updateMarker();
		}else
		{
			//if there is no agent to run away from,then just move randomly
			this.moveAbout();
		}
	}
	

	//prepares to remove the agent
	public void cleanUp()
	{

		//remove the marker off the map if this agent has one
		if(this.marker!=null)
		{
			this.marker.remove();
		}


		//remove itself from the list of observers
		Support.agentsNotifier.deleteObserver(this);



	}

	/*
	 * returns an action package that contains a given name
	 */
	public ActionPackage getActionPackageContainingName(String name)
	{
		for(ActionPackage ap:this.actionStack)
		{
			if(ap.name.toLowerCase().contains(name))
			{
				return ap;
			}
		}
		//if not found,return null
		return null;
	}

	/*
	 * update method called by the observable thread
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object data) {


		try {
			Class agentClass = Class.forName("b0538705.ncl.worldrpg.Agent");

			//run all methods in the current action package
			for(ActionMethod am: activeActionPackage.methodsToRun)
			{
				Class[] par=null;
				//get the parameter classes
				if(am.parameters!=null && am.parameters.length > 0)
				{
					par = new Class[am.parameters.length];
					for(int i=0;i<am.parameters.length;i++)
					{
						par[i] = am.parameters[i].getClass();
					}
				}

				//get the method with given name and specify the classes of the parameters
				Method method = agentClass.getMethod(am.name, par);

				//check if there is a record of running this method for the last time
				if(this.recentRunTimes.containsKey(am.name))
				{
					//check the delay, invoke only if the time since the last one is longer than the specified delay
					if((System.currentTimeMillis() - (long)this.recentRunTimes.get(am.name)) > am.delayBetweenActions)
					{
						//update the map
						this.recentRunTimes.put(am.name, System.currentTimeMillis());
						//invoke the method
						method.invoke(this, (Object[])am.parameters);
					}
				}else
				{
					this.recentRunTimes.put(am.name, System.currentTimeMillis());
					method.invoke(this, (Object[])am.parameters);
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("worldrpg","Specified method name has not been found");
			e.printStackTrace();
		}

	}





}
