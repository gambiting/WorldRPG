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
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	private GoogleMap mMap;

	private MarkerOptions mr;

	private Marker marker;

	private MarkerOptions playerMarkerOptions;
	private Marker playerMarker;

	private BitmapDescriptor playerImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Support.PACKAGE_NAME = getApplicationContext().getPackageName();

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



		playerImage = BitmapDescriptorFactory.fromResource(R.drawable.player);


		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		mMap.setOnMapClickListener(new myMapListener());

		mr = new MarkerOptions().position(new LatLng(0, 0));


		marker = mMap.addMarker(mr);

		Player.instance = new Player();
		//Player.instance.position.latitude = 
		
		
		playerMarkerOptions = new MarkerOptions().icon(playerImage);
		playerMarkerOptions.position(new LatLng(0, 0));
		playerMarker = mMap.addMarker(playerMarkerOptions);



		//location

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new myLocationListener());
		 
		//Location startingLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		//LatLng latLng = new LatLng(startingLocation.getLatitude(), startingLocation.getLongitude());
		//mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		
		
		Support.initializeThreads();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class myLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location location) {

			//Log.d("worldrpg", String.valueOf(location.getLatitude()) + " ; " +  String.valueOf(location.getLongitude()));
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

			playerMarker.remove();
			playerMarkerOptions.position(latLng);
			playerMarker = mMap.addMarker(playerMarkerOptions);

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

	private class myMapListener implements OnMapClickListener
	{

		@Override
		public void onMapClick(LatLng point) {
			//Toast.makeText(getApplicationContext(), "You clicked on: " + point.latitude + "; " + point.longitude, Toast.LENGTH_LONG).show();

			marker.remove();
			mr.position(new LatLng(point.latitude, point.longitude));
			mr.icon(playerImage);
			marker = mMap.addMarker(mr);

		}

	}

}
