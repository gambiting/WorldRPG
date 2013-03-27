package b0538705.ncl.worldrpg;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;


public class Support {
	
	public static LatLng locationToLatLng(Location l)
	{
		return new LatLng(l.getLatitude(),l.getLongitude());
	}

}
