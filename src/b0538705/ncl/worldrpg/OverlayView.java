package b0538705.ncl.worldrpg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

public class OverlayView extends View implements SensorEventListener  { 

	public static final String DEBUG_TAG = "OverlayView Log"; 
	String accelData = "Accelerometer Data"; 
	String compassData = "Compass Data"; 
	String gyroData = "Gyro Data"; 

	public OverlayView(Context context) { 
		super(context);     
	} 

	@Override 
	protected void onDraw(Canvas canvas) { 
		super.onDraw(canvas); 

		Paint contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG); 
		contentPaint.setTextAlign(Align.CENTER); 
		contentPaint.setTextSize(20); 
		contentPaint.setColor(Color.RED); 
		canvas.drawText(accelData, canvas.getWidth()/2, canvas.getHeight()/4, contentPaint); 
		canvas.drawText(compassData, canvas.getWidth()/2, canvas.getHeight()/2, contentPaint); 
		canvas.drawText(gyroData, canvas.getWidth()/2, (canvas.getHeight()*3)/4, contentPaint); 
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		SensorManager sensors = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE); 
		Sensor accelSensor = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
		Sensor compassSensor = sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); 
		Sensor gyroSensor = sensors.getDefaultSensor(Sensor.TYPE_GYROSCOPE); 

		boolean isAccelAvailable = sensors.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL); 
		boolean isCompassAvailable = sensors.registerListener(this, compassSensor, SensorManager.SENSOR_DELAY_NORMAL);       
		boolean isGyroAvailable = sensors.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);    

	}

	@Override
	public void onSensorChanged(SensorEvent event) {         
		StringBuilder msg = new StringBuilder(event.sensor.getName()).append(" "); 
		for(float value: event.values) 
		{ 
			msg.append("[").append(value).append("]"); 
		} 

		switch(event.sensor.getType()) 
		{ 
		case Sensor.TYPE_ACCELEROMETER: 
			accelData = msg.toString(); 
			break; 
		case Sensor.TYPE_GYROSCOPE: 
			gyroData = msg.toString(); 
			break; 
		case Sensor.TYPE_MAGNETIC_FIELD: 
			compassData = msg.toString(); 
			break;               
		} 

		this.invalidate();     
	} 
} 