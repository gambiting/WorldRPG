package b0538705.ncl.worldrpg;

import java.util.ArrayList;

public class Scenario  {
	
	
	public int scenario;
	public String name;
	//each number is given per spawning location
	public int normal=10;
	public int infected=5;
	public int panicked=5;
	public int diseaseTimeLimit=60;
	
	//width(and height) of the spawning location, in meters
	public int spawningLocationWidth = 50;
	
	public Agent agentTemplate;
	
	public Scenario()
	{
		//TODO yaml file loading
	}


}
