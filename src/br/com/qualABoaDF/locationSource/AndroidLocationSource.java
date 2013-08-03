package br.com.qualABoaDF.locationSource;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;

public class AndroidLocationSource implements LocationSource {
	private OnLocationChangedListener listener;
	
	@Override
	public void activate(OnLocationChangedListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void deactivate() {
		this.listener = null;
	}
	
	public void setLocation(Location location) {
		if(this.listener != null) {
			this.listener.onLocationChanged(location);
		}
	}
	
	public void setLocation(LatLng latLng) {
		Location location = new Location(LocationManager.GPS_PROVIDER);
		location.setLatitude(latLng.latitude);
		location.setLongitude(latLng.longitude);
		if(this.listener != null) {
			this.listener.onLocationChanged(location);
		}
	}
}