package b0538705.ncl.worldrpg;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Construct;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private GoogleMap mMap;

	private MarkerOptions mr;

	private Marker marker;

	private MarkerOptions playerMarkerOptions;
	private Marker playerMarker;

	private BitmapDescriptor playerImage;
	
	public static Handler mainHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Support.PACKAGE_NAME = getApplicationContext().getPackageName();
		
		Support.currentContext = getApplicationContext();
		
		
		Support.initializeGeneral();
		

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		
		/*
		 * yaml experimentation
		 */
		
		Yaml yaml = new Yaml();
		String document = "\n- Hesperiidae\n- Papilionidae\n- Apatelodidae\n- Epiplemidae";
		InputStream basicScenarioStream = getResources().openRawResource(R.raw.basic);
		@SuppressWarnings("unchecked")
		Map<String, Object> object = (Map<String, Object>) yaml.load(basicScenarioStream);
		
		
		//Log.d("worldrpg", object.toString());
		
		/*
		setContentView(R.layout.camera_view);
		FrameLayout arViewPane = (FrameLayout) findViewById(R.id.ar_view_pane); 

		ArDisplayView arDisplay = new ArDisplayView(getApplicationContext(), this);
		arViewPane.addView(arDisplay); 
		
		OverlayView arContent = new OverlayView(getApplicationContext()); 
		arViewPane.addView(arContent); 
		*/

		
		setContentView(R.layout.activity_main);
		Player.instance = new Player();
		MapHandler.initMap(this);
		Support.initializeAssets();
		
		Support.debugView = (TextView) findViewById(R.id.debug_text);
		Support.debugScroller = (ScrollView) findViewById(R.id.scroller);
		
		Button centreOnPlayerButton = (Button) findViewById(R.id.centreOnPlayerButton);
		centreOnPlayerButton.setOnClickListener(new centreOnPlayerClickListener());

		playerImage = BitmapDescriptorFactory.fromResource(R.drawable.player);

		mr = new MarkerOptions().position(new LatLng(0, 0));


		marker = MapHandler.mMap.addMarker(mr);

		
		//Player.instance.position.latitude = 
		
		
		playerMarkerOptions = new MarkerOptions().icon(playerImage);
		playerMarkerOptions.position(new LatLng(0, 0));
		playerMarker = MapHandler.mMap.addMarker(playerMarkerOptions);



		//location

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new myLocationListener());
		 
		//Location startingLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		//LatLng latLng = new LatLng(startingLocation.getLatitude(), startingLocation.getLongitude());
		//mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		
		
		
		
		final ProgressDialog waitingForLocationDialog = ProgressDialog.show(this, "WorldRPG", "Obtaining location, please wait", true);
		
		/*
		 * create a new handler for running things on main thread
		 */
		MainActivity.mainHandler = new Handler(){
			  @Override
			  public void handleMessage(Message msg ) 
			  {
				  if(((String)msg.obj).equals("location_obtained"))
				  {
					  waitingForLocationDialog.dismiss();
				  }
			  }
			};

	}

	@Override
	protected void onPause() {
		
		System.exit(0);
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private class centreOnPlayerClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			MapHandler.mMap.animateCamera(CameraUpdateFactory.newLatLng(Player.instance.position), 500, null);
		}
		
	}

	private class myLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location location) {

			//Log.d("worldrpg", String.valueOf(location.getLatitude()) + " ; " +  String.valueOf(location.getLongitude()));
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			
			//only update if the location changed or if the distance between the current location and the old one is larger than 20 metres
			if(Player.instance.position==null || Support.distanceBetweenTwoPoints(latLng, Player.instance.position) > 20)
			{
				MapHandler.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
				
				
	
				
				//need a way to check whatever the game is running yet or not
				//there is room for improvement here
				//TODO move somewhere else
				if(Support.activeScenario==null)
				{
					Support.initializeThreads();
					
					
				}
				
				Player.instance.locationUpdate(Support.locationToLatLng(location));
				
				playerMarker.remove();
				playerMarkerOptions.position(latLng);
				playerMarker = MapHandler.mMap.addMarker(playerMarkerOptions);
				
				
				Message msg = MainActivity.mainHandler.obtainMessage(1, "location_obtained");
				MainActivity.mainHandler.dispatchMessage(msg);
				
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

	

}
