package b0538705.ncl.worldrpg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Observable;
import java.util.Observer;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Agent implements Observer {
	
	public LatLng position;
	
	public int id;
	
	public ArrayList<ActionPackage> actionStack;
	public ActionPackage activeActionPackage;
	
	public Agent(int ID)
	{
		this.id = ID;
		actionStack = new ArrayList<ActionPackage>();
		
		
		/*
		 * debug code
		 */
		ActionPackage tempPackage = new ActionPackage();
		tempPackage.methodsToRun.add(new ActionMethod("moveAbout", null));
		activeActionPackage = tempPackage;
		
		
		
		//add itself as an observer
		Support.agentsNotifier.addObserver(this);
		
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
