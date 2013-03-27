package b0538705.ncl.worldrpg;

import java.util.ArrayList;

public class Scenario {
	
	
	//agents active in this scenario
	public ArrayList<Agent> activeAgents;
	
	public Scenario()
	{
		activeAgents = new ArrayList<Agent>();
		
		
		/*
		 * at this point, the scenario should load an appropriate yaml file with scenario details
		 * not right now though
		 */
		
		createAgents();
	}
	
	private void createAgents()
	{
		for(int i=0;i<10;i++)
		{
			Agent tempAgent = new Agent();
			//tempAgent.position
		}
	}

}
