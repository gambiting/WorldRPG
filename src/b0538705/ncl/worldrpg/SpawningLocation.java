package b0538705.ncl.worldrpg;

import java.util.ArrayList;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class SpawningLocation {

	LatLng position;
	public long id;

	int normal=0;
	int infected=0;

	public ArrayList<Agent> activeAgents;
	
	public ArrayList<Polygon> activePolygons;

	public SpawningLocation(long ID,LatLng pos)
	{
		this.id = ID;
		this.position = pos;

		this.normal = Support.activeScenario.normal;
		this.infected = Support.activeScenario.infected;


		this.init();
	}

	public SpawningLocation(long ID, LatLng pos, int Normal, int Infected)
	{
		this.id = ID;
		this.position = pos;
		
		this.normal = Normal;
		this.infected = Infected;
		
		this.init();

	}

	private void init()
	{
		this.activeAgents = new ArrayList<Agent>();
		this.activePolygons = new ArrayList<Polygon>();

		//add the number of normal agents
		for(int i=0;i<this.normal;i++)
		{
			Agent tempAgent = new Agent(this);
			tempAgent.useSprites(Agent.animationSprites1);
			this.activeAgents.add(tempAgent);
		}

		//TODO add infected and other types


		//DEBUG
		this.addDebugLines();
	}
	
	public void cleanUp()
	{
		//clean up the agents
		for(Agent a:this.activeAgents)
		{
			a.cleanUp();
		}
		
		//remove the polygons if there are any
		for(Polygon p:this.activePolygons)
		{
			p.remove();
		}
	}

	/*
	 * draws debug lines around the spawn point - a rectangle as big as the spawning area
	 */
	public void addDebugLines()
	{
		double latOffset = ((Support.activeScenario.spawningLocationWidth*3.2808399)/3.64)*0.00001;
		//calculate longitude offset
		double lonOffset = ((Support.activeScenario.spawningLocationWidth*3.2808399)/2.22)*0.00001;

		LatLng temp1 = new LatLng(this.position.latitude-latOffset,this.position.longitude-lonOffset);
		LatLng temp2 = new LatLng(this.position.latitude+latOffset,this.position.longitude-lonOffset);
		LatLng temp3 = new LatLng(this.position.latitude+latOffset,this.position.longitude+lonOffset);
		LatLng temp4 = new LatLng(this.position.latitude-latOffset,this.position.longitude+lonOffset);


		Polygon polygon = MapHandler.mMap.addPolygon(new PolygonOptions()
		.add(temp1,temp2,temp3,temp4)
		.strokeColor(Color.RED)
		.fillColor(Color.TRANSPARENT));
		
		this.activePolygons.add(polygon);

	}

}