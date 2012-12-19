package edu.chalmers.project;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.chalmers.project.data.Match;


public class MapFragment extends Fragment  {

	private MapView mapView;
	private View view;
	private MapController mc;
	LocationManager locMgr;
	MyLocationListener locLstnr;
	String provider;
	private ImageButton locateButton;
	List<Overlay> mapOverlays;
	MyItemizedOverlay itemizedoverlay;
	private ArrayList<Match> matchList = new ArrayList<Match>();
	Location currentLocation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		if (view==null){ //we can only inflate the map one time, if not crashes
			view = inflater.inflate(R.layout.map_fragment, container, false);
			view.setClickable(true);
		}
		mapView = (MapView) view.findViewById(R.id.mainMap);
		locateButton = (ImageButton) view.findViewById(R.id.imageButtonLocate);
		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		mapView.setSatellite(true);
		mapOverlays = mapView.getOverlays();
		Drawable drawable = getResources().getDrawable(R.drawable.google_maps_marker);
		itemizedoverlay = new MyItemizedOverlay(drawable, getActivity());
		locLstnr = new MyLocationListener();

		// Set Click Listener
		locateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapOverlays.clear();
				locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locLstnr);
				locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locLstnr);

				String networkProvider = LocationManager.NETWORK_PROVIDER;
				currentLocation = locMgr.getLastKnownLocation(networkProvider); //we uses the network for the first time

				Log.i("--- Latitude",""+currentLocation.getLatitude());
				Log.i("--- Latitude",""+currentLocation.getLongitude());       		

				String coordinates[] = {""+currentLocation.getLatitude(), ""+currentLocation.getLongitude()};
				double lat = Double.parseDouble(coordinates[0]);
				double lng = Double.parseDouble(coordinates[1]);

				GeoPoint myPosition = new GeoPoint((int)(lat * 1E6),(int)(lng * 1E6));//Myposition
				OverlayItem overlayitem = new OverlayItem(myPosition, "My position", null);
				if(itemizedoverlay.size()==0){ //first time  			
					itemizedoverlay.addOverlay(overlayitem);
					drawMatches(); // matches location does not change
				}
				else{ //update our position
					itemizedoverlay.editOverlay(0, overlayitem);
				}
				mapOverlays.add(itemizedoverlay);
				mc.animateTo(myPosition);
				mc.setZoom(15); //between 1 and 21
				mapView.invalidate();
			}
		});	

		return view;
	}

	/**
	 * We add to the overlay the location of the matches that are in the current list of matches
	 */
	public void drawMatches(){
		Drawable drawable = getResources().getDrawable(R.drawable.google_maps_marker);
		MyItemizedOverlay matchesOverlay = new MyItemizedOverlay(drawable, getActivity());
		matchList = HomeActivity.getMatchList();
		for (Match m : matchList) {
			GeoPoint fieldLocation = matchesOverlay.getFieldLocation(m.getLocation());
			if (fieldLocation!=null){
				OverlayItem newOverlay = new OverlayItem(fieldLocation,m.getName(), m.getField() +
						"\n" + m.getDate()+ " " + m.getTime() + "\n"  + m.getCost()+" € \n");
				matchesOverlay.addOverlay(newOverlay);
			}

		}
		mapOverlays.add(matchesOverlay);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This verification should be done during onAttach() because the system calls
		// this method when the user returns to the activity, which ensures the desired
		// location provider is enabled each time the activity resumes from the stopped state.
		locMgr = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			// Build an alert dialog here that requests that the user enable
			// the location services, then when the user clicks the "OK" button,
			// call enableLocationSettings()
			AlertDialog.Builder adb = new AlertDialog.Builder(this.getActivity());
			adb.setTitle("Enable GPS");
			adb.setMessage("Do you want to enable GPS?");
			adb.setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					enableLocationSettings();
				}
			});
			adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();
				}
			});
			adb.show();
		}
	}


	/**
	 * We start the activity of location settings
	 */
	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}	

	@Override
	public void onDetach() {
		super.onDetach();
		locMgr.removeUpdates(locLstnr);
	}


	/**
	 * Location listener that is going to check the best possible location
	 *
	 */
	public class MyLocationListener implements LocationListener{
		private static final int TWO_MINUTES = 1000 * 60 * 2;
		@Override
		public void onLocationChanged(Location loc){		
			Criteria locationCritera = new Criteria();
			locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
			locationCritera.setAltitudeRequired(false);
			locationCritera.setBearingRequired(false);
			locationCritera.setCostAllowed(true);
			locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
			provider = locMgr.getBestProvider(locationCritera, true);
			currentLocation  = locMgr.getLastKnownLocation(provider);
			if(isBetterLocation(loc, currentLocation)){
				currentLocation = loc;
			}

		}

		/** Determines whether one Location reading is better than the current Location fix
		 * @param location  The new Location that you want to evaluate
		 * @param currentBestLocation  The current Location fix, to which you want to compare the new one
		 */
		protected boolean isBetterLocation(Location location, Location currentBestLocation) {
			if (currentBestLocation == null) {
				// A new location is always better than no location
				return true;
			}

			// Check whether the new location fix is newer or older
			long timeDelta = location.getTime() - currentBestLocation.getTime();
			boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
			boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
			boolean isNewer = timeDelta > 0;

			// If it's been more than two minutes since the current location, use the new location
			// because the user has likely moved
			if (isSignificantlyNewer) {
				return true;
				// If the new location is more than two minutes older, it must be worse
			} else if (isSignificantlyOlder) {
				return false;
			}

			// Check whether the new location fix is more or less accurate
			int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
			boolean isLessAccurate = accuracyDelta > 0;
			boolean isMoreAccurate = accuracyDelta < 0;
			boolean isSignificantlyLessAccurate = accuracyDelta > 200;

			// Check if the old and new location are from the same provider
			boolean isFromSameProvider = isSameProvider(location.getProvider(),
					currentBestLocation.getProvider());

			// Determine location quality using a combination of timeliness and accuracy
			if (isMoreAccurate) {
				return true;
			} else if (isNewer && !isLessAccurate) {
				return true;
			} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
				return true;
			}
			return false;
		}

		/** Checks whether two providers are the same */
		private boolean isSameProvider(String provider1, String provider2) {
			if (provider1 == null) {
				return provider2 == null;
			}
			return provider1.equals(provider2);
		}

		@Override
		public void onProviderDisabled(String provider){
			Toast.makeText( getActivity().getApplicationContext(),
					"Gps Disabled",
					Toast.LENGTH_SHORT ).show();
		}

		@Override
		public void onProviderEnabled(String provider){
			Toast.makeText( getActivity().getApplicationContext(),
					"Gps Enabled",Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras){

		}

	}

}
