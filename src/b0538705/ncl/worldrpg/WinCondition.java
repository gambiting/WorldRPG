package b0538705.ncl.worldrpg;

import java.lang.reflect.Method;
import java.util.Observable;

import android.app.Dialog;
import android.util.Log;

public class WinCondition  implements Runnable{


	//check win conditions every 5 seconds
	public int updateFrequency=5000;
	public String name;
	public Object[] parameters;

	public String description;
	
	private boolean satisfied=false;

	public WinCondition()
	{

	}

	public WinCondition( int miliseconds)
	{
		this.updateFrequency = miliseconds;
	}

	@Override
	public void run() {

		//run until killed
		while(!satisfied)
		{
			//Log.d("worldrpg", Support.returnCountOfAgentsWithState("infected").toString());
			synchronized(this)
			{
				//use Java Reflections API to run specified win condition method
				try {
					Class conditionClass = Class.forName("b0538705.ncl.worldrpg.WinCondition");


					Class[] par=null;
					//get the parameter classes
					if(parameters!=null && parameters.length > 0)
					{
						//make a new array for the parameter classes
						par = new Class[parameters.length];
						//fill the array with all the parameter classes
						for(int i=0;i<parameters.length;i++)
						{
							par[i] = parameters[i].getClass();
						}
					}

					//get the method with given name and specify the classes of the parameters(if method does not take any
					//parameters,then par=null
					Method method = conditionClass.getMethod(name, par);

					
					//only run if there are any spawning locations active - to wait for the game to be fully initialized
					if(Support.activeSpawningLocations.size()>0)
					{
						method.invoke(this, (Object[])parameters);
					}



				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("worldrpg","Specified method name has not been found");
					e.printStackTrace();
				}


				//sleep for the pre-defined period
				try {
					wait(updateFrequency);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}


	}

	/*
	 * display the screen congratulating the player
	 */
	public void showWinScreen()
	{



		Support.currentActivity.runOnUiThread(new Runnable() {
			public void run() {
				final Dialog dialog = new Dialog(Support.currentActivity);
				dialog.setTitle("Congratulations, you have completed the scenario!");
				dialog.show();
			}
		});
	}


	/*
	 * win condition if there is less than a specified number of infected agents
	 */
	public void winIfLessInfectedThan(Integer limit)
	{

		if(Support.returnCountOfAgentsWithState("infected")<limit)
		{

			//win!
			this.satisfied=true;
			this.showWinScreen();
		}
	}

}
