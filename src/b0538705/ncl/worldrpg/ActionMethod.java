package b0538705.ncl.worldrpg;

public class ActionMethod {

	public String name; //name of the method to call
	public Object[] parameters; //parameters to pass to this method
	public Integer delayBetweenActions = 0; //delay between actions, in milliseconds

	
	public ActionMethod()
	{
		
	}
	
	public ActionMethod(String Name, Object[] Parameters)
	{
		this.name = Name;
		this.parameters = Parameters;
	}
	
}
