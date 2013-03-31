package b0538705.ncl.worldrpg;

import android.app.Activity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;

public class MapHandler implements OnMapClickListener {
	
	
	/*
	 * THE map used by this application. There should only be one
	 */
	public static GoogleMap mMap;
	
	
	public static void initMap(Activity activity)
	{
		MapHandler.mMap = ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.map)).getMap();

		MapHandler.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		MapHandler.mMap.setOnMapClickListener(new MapHandler());
	}

	@Override
	public void onMapClick(LatLng point) {
		Toast.makeText(Support.currentContext, "You clicked on: " + point.latitude + "; " + point.longitude, Toast.LENGTH_LONG).show();
		
	}

}
