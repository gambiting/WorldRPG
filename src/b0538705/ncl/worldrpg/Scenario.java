package b0538705.ncl.worldrpg;

import java.util.ArrayList;

public class Scenario  {
	

	//agents active in this scenario
	public ArrayList<Agent> activeAgents;
	
	//initial number of agents:
	public int initialNumberOfAgents = 20;
	
	public Scenario()
	{
		activeAgents = new ArrayList<Agent>();
		
		for(int i=0;i<initialNumberOfAgents;i++)
		{
			Agent tempAgent = new Agent(i);
			tempAgent.useSprites(Agent.animationSprites1);
			activeAgents.add(tempAgent);
		}
	}


}
