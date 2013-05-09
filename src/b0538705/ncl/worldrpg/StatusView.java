package b0538705.ncl.worldrpg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatusView extends LinearLayout {

	TextView statusText;

	public StatusView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public StatusView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public StatusView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void init(Context context)
	{
		statusText = new TextView(context);


		//Log.d("worldrpg", "child count: " + this.getChildCount());
		//statusText = (TextView)this.findViewById(R.id.statusText);
		this.addView(statusText);

		LinearLayout.LayoutParams textParams = (LinearLayout.LayoutParams)statusText.getLayoutParams();

		textParams.setMargins(10, 10, 10, 10);
		statusText.setTextSize(20.0f);
		statusText.setTextColor(Color.WHITE);

		statusText.setLayoutParams(textParams);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);

		StringBuilder temp = new StringBuilder();
		temp.append("Current game status:\n");
		temp.append("Normal: " + Support.currentNoOfNormalAgents + "\n");
		temp.append("Infected: " + Support.currentNoOfInfectedAgents + "\n");
		temp.append("Panicked: " + Support.currentNoOfPanickedAgents + "\n");
		if(Support.activeScenario!=null && Support.activeScenario.winConditions.size()>0)
		{
			temp.append("Win conditions:\n");
			for(WinCondition wc:Support.activeScenario.winConditions)
			{
				temp.append(" - " + wc.description + "\n");
			}
		}
		
		if(Support.activeScenario!=null && Support.activeScenario.failConditions.size()>0)
		{
			temp.append("Fail conditions:\n");
			for(FailCondition fc:Support.activeScenario.failConditions)
			{
				temp.append(" - " + fc.description + "\n");
			}
		}

		statusText.setText(temp);



	}



}
