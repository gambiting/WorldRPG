package b0538705.ncl.worldrpg;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class SpawningLocation {

	LatLng position;
	public long id;

	int normal=0;
	int infected=0;
	int panicked=0;

	public BlockingQueue<Agent> activeAgents;

	public BlockingQueue<Polygon> activePolygons;

	public SpawningLocation(long ID,LatLng pos)
	{
		this.id = ID;
		this.position = pos;

		this.normal = Support.activeScenario.normal;
		this.infected = Support.activeScenario.infected;
		this.panicked = Support.activeScenario.panicked;


		this.init();
	}

	public SpawningLocation(long ID, LatLng pos, int Normal, int Infected,int Panicked)
	{
		this.id = ID;
		this.position = pos;

		this.normal = Normal;
		this.infected = Infected;
		this.panicked = Panicked;

		this.init();

	}

	private void init()
	{
		this.activeAgents = new LinkedBlockingQueue<Agent>();
		this.activePolygons = new LinkedBlockingQueue<Polygon>();

		//add the number of normal agents
		for(int i=0;i<this.normal;i++)
		{
			Agent tempAgent = new Agent(this, "normal");
			tempAgent.useSprites(Agent.animationSpritesNormal);
			tempAgent.parentSpawningLocation = this;
			this.activeAgents.add(tempAgent);
		}

		//add the number of normal agents
		for(int i=0;i<this.infected;i++)
		{
			Agent tempAgent = new Agent(this, "infected");
			tempAgent.useSprites(Agent.animationSpritesInfected);
			tempAgent.parentSpawningLocation = this;
			this.activeAgents.add(tempAgent);
		}

		//add the number of normal agents
		for(int i=0;i<this.panicked;i++)
		{
			Agent tempAgent = new Agent(this, "panicked");
			tempAgent.useSprites(Agent.animationSpritesPanicked);
			tempAgent.parentSpawningLocation = this;
			this.activeAgents.add(tempAgent);
		}




		//DEBUG
		this.addDebugLines();
	}
	
	/*
	 * updates the count of each agent type for this spawning location
	 */
	public void updateAgentsCount()
	{
		int normalTemp=0, infectedTemp=0, panickedTemp=0;
		for(Agent a:this.activeAgents)
		{
			if(a.state.equals("normal"))
			{
				normalTemp++;
			}else if(a.state.equals("infected"))
			{
				infectedTemp++;
			}else if(a.state.equals("panicked"))
			{
				panickedTemp++;
			}
		}
		
		this.normal = normalTemp;
		this.infected = infectedTemp;
		this.panicked = panickedTemp;
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
		
		
		//update the database
		Support.databaseEngine.updatePointInTheDatabase(this);
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
