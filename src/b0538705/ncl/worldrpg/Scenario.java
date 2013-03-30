package b0538705.ncl.worldrpg;

import java.util.ArrayList;

public class Scenario  {
	

	//agents active in this scenario
	public ArrayList<Agent> activeAgents;
	
	public Scenario()
	{
		activeAgents = new ArrayList<Agent>();
		
		for(int i=0;i<10;i++)
		{
			activeAgents.add(new Agent(i));
		}
	}


}
