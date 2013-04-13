package b0538705.ncl.worldrpg;

import java.util.Observable;
import java.util.Timer;

import android.util.Log;
import android.widget.SlidingDrawer;

public class Notifier extends Observable implements Runnable{


	public int updateFrequency;

	public Notifier( int miliseconds)
	{
		this.updateFrequency = miliseconds;
	}

	@Override
	public void run() {

		//run until killed
		while(true)
		{
			synchronized(this)
			{

				//set the flag on this observable to changed
				this.setChanged();
				//notify the observers
				this.notifyObservers();
				

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

}
