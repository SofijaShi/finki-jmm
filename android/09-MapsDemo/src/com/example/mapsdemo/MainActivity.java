package com.example.mapsdemo;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity implements LocationListener {

	private GoogleMap mMap;

	// Declaring a Location Manager
	protected LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadGoogleMap();

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean netEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (!gpsEnabled && !netEnabled) {
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}
	}

	@Override
	protected void onStart() {
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000, 10, this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 10, this);
		super.onStart();
	}

	@Override
	protected void onStop() {
		locationManager.removeUpdates(this);
		super.onStop();
	}

	private void loadGoogleMap() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {

		GeocodingTask task = new GeocodingTask(this,
				new GeocoderAddressListener() {

					@Override
					public void onAddressObtained(Address address) {
						if (address != null) {
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									new LatLng(address.getLatitude(), address
											.getLongitude()), 10));
						} else {
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									new LatLng(42.0062403, 21.3559413), 10));
						}

					}
				});

		task.execute("Skopje");

		mMap.addMarker(new MarkerOptions().position(
				new LatLng(42.0062403, 21.3559413)).title("Marker"));

		mMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(final LatLng point) {

				GeocodingTask task = new GeocodingTask(MainActivity.this,
						new GeocoderAddressListener() {

							@Override
							public void onAddressObtained(Address address) {
								if (address != null) {
									mMap.addMarker(new MarkerOptions()
											.position(point)
											.title(address.getAddressLine(0))
											.snippet(
													address.getCountryCode()
															+ " - "
															+ address
																	.getCountryName())
											.icon(BitmapDescriptorFactory
													.fromResource(R.drawable.ic_launcher))
											.infoWindowAnchor(0.5f, 0.5f));
								}

							}
						});

				task.execute(point);
			}
		});
	}

	@Override
	public void onLocationChanged(Location location) {
		mMap.addMarker(new MarkerOptions()
				.position(
						new LatLng(location.getLatitude(), location
								.getLongitude())).title(location.getProvider())
				.snippet(location.getAccuracy() + "m"));

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

}
