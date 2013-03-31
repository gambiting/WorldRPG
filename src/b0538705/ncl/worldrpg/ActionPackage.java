package b0538705.ncl.worldrpg;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ActionPackage {

	public int id;
	public String name;
	
	
	/*
	 * list of methods to run from the Agent object,
	 * the key is the name of the method
	 */
	public ArrayList<ActionMethod> methodsToRun;
	
	
	public ActionPackage()
	{
		methodsToRun = new ArrayList<ActionMethod>();
	}
}
