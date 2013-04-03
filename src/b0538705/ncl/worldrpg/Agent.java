package b0538705.ncl.worldrpg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Currency;
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
	
	public static ArrayList<BitmapDescriptor> animationSprites1 = new ArrayList<BitmapDescriptor>();
	
	public LatLng position;
	
	public int id;
	
	public ArrayList<ActionPackage> actionStack;
	public ActionPackage activeActionPackage;
	
	private MarkerOptions markerOptions;
	private Marker marker;
	
	public boolean changed=false;
	
	public ArrayList<BitmapDescriptor> animationSprites;
	
	private int currentSprite;
	
	
	public Agent(int ID)
	{
		this.id = ID;
		actionStack = new ArrayList<ActionPackage>();
		
		initializePosition();
		
		/*
		 * debug code
		 */
		ActionPackage tempPackage = new ActionPackage();
		tempPackage.methodsToRun.add(new ActionMethod("moveAbout", null));
		activeActionPackage = tempPackage;
		
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
		LatLng tempPosition = Player.instance.position;
		this.position = Support.transformPositionBy(tempPosition, Math.random()*100.0-50.0, Math.random()*100.0-50.0);
		this.updateMarker();
		
	}
	
	
	public void updateMarker()
	{
		MainActivity.mainHandler.post(new Runnable() {
			
			@Override
			public void run() {
				for(Agent a:Support.activeScenario.activeAgents)
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
		});
	}
	
	//run ONLY from main thread, never call directly
	//updates the map, must be run from the main thread
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
	
	
	public void moveAbout()
	{
		Log.d("worldrpg", "Agent no. " + id + " moving about");
		this.position = Support.transformPositionBy(this.position, Math.random()*4.0-2.0, Math.random()*4.0-2.0);
		this.changed = true;
		this.updateMarker();
	}

	@Override
	public void update(Observable observable, Object data) {
		
		
		try {
			Class agentClass = Class.forName("b0538705.ncl.worldrpg.Agent");
			
			//run all methods in the current action package
			for(ActionMethod am: activeActionPackage.methodsToRun)
			{
				//get that method
				Method method = agentClass.getMethod(am.name, null);
				method.invoke(this, (Object[])am.parameters);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("worldrpg","Specified method name has not been found");
			e.printStackTrace();
		}
		
	}
	
	
	
	

}
