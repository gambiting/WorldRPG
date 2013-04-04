package b0538705.ncl.worldrpg;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapHandler implements OnMapClickListener, OnCameraChangeListener {
	
	
	/*
	 * THE map used by this application. There should only be one.
	 */
	public static GoogleMap mMap;
	
	/*
	 * current projection, for on-screen reference
	 */
	public static Projection currentProjection;
	
	
	public static void initMap(Activity activity)
	{
		MapHandler.mMap = ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.map)).getMap();

		MapHandler.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		MapHandler.mMap.setOnMapClickListener(new MapHandler());
		
		MapHandler.mMap.setOnCameraChangeListener(new MapHandler());
	}

	@Override
	public void onMapClick(LatLng point) {
		//Toast.makeText(Support.currentContext, "You clicked on: " + point.latitude + "; " + point.longitude, Toast.LENGTH_LONG).show();
		int tempX = MapHandler.currentProjection.toScreenLocation(point).x;
		int tempY = MapHandler.currentProjection.toScreenLocation(point).y;
		Support.printDebug("worldrpg-camera", "You clicked on: " + tempX + "," + tempY );
	}
	

	@Override
	public void onCameraChange(CameraPosition position) {
		//get a new projection each time the projection changes
		MapHandler.currentProjection = MapHandler.mMap.getProjection();
		
	}

}
