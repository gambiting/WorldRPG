package b0538705.ncl.worldrpg;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomAgentView extends LinearLayout {

	private Button button1;
	private TextView textView1;
	private int a=0;
	private Agent agent;

	public CustomAgentView(Context context, Agent agent)
	{
		this(context);
		this.agent = agent;
	}

	public CustomAgentView(Context context) {
		super(context);

		LayoutInflater  mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflatedView = mInflater.inflate(R.layout.custom_agent_view, this, true);

		this.button1 = (Button)inflatedView.findViewById(R.id.button1);
		this.textView1 = (TextView)inflatedView.findViewById(R.id.textView1);

		this.setWillNotDraw(false);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		if(agent==null)
		{
			this.textView1.setText("No agent to show");
		}else
		{
			StringBuilder tempString = new StringBuilder();
			tempString.append("Agent state: " + agent.state);
			
			//if the agent is infected,show the time left and the heal button
			if(agent.state.equals("infected"))
			{
				long time = Support.activeScenario.diseaseTimeLimit*1000 - (System.currentTimeMillis() - agent.infectionTime );
				if(time<0) time = 0;
				tempString.append("\n Time left: " + String.valueOf(time/1000));
				
				this.button1.setVisibility(View.VISIBLE);
				this.button1.setEnabled(false);
			}else
			{
				this.button1.setVisibility(View.INVISIBLE);
				this.button1.setEnabled(false);
			}
			
			//check if the player has any items with healing properties
			
			this.textView1.setText(tempString);
		}

	}



}
