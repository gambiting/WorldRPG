package b0538705.ncl.worldrpg;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Item {
	
	
	public String name;
	public ArrayList<ActionMethod> methodsToRun;
	
	public String useOn;
	
	public Agent agentToUseOn;
	
	public boolean agentCompatible;
	public boolean playerCompatible;

	public Item()
	{
		methodsToRun = new ArrayList<ActionMethod>();
	}
	
	
	public void useOnAgent(Agent agent)
	{
		useOn = "Agent";
		agentToUseOn = agent;
		this.use();
	}
	
	public void useOnPlayer()
	{
		useOn = "Player";
		this.use();
	}
	
	/*
	 * run all the methods associated with this item
	 */
	private void use()
	{
		for(ActionMethod am:methodsToRun)
		{
			try {
				Class agentClass = Class.forName("b0538705.ncl.worldrpg.Item");
				
				Class[] par=null;
				//get the parameter classes
				if(am.parameters!=null && am.parameters.length > 0)
				{
					//make a new array for the parameter classes
					par = new Class[am.parameters.length];
					//fill the array with all the parameter classes
					for(int i=0;i<am.parameters.length;i++)
					{
						par[i] = am.parameters[i].getClass();
					}
				}

				//get the method with given name and specify the classes of the parameters(if method does not take any
				//parameters,then par=null
				Method method = agentClass.getMethod(am.name, par);
				
				method.invoke(this, (Object[])am.parameters);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void heal(Integer value)
	{
		if(useOn.equals("Agent"))
		{
			
		}else if(useOn.equals("Player"))
		{
			
		}
	}
	
}
