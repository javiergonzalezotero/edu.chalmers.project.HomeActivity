package edu.chalmers.project;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.chalmers.project.MapFragment.MyLocationListener;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.MatchPlayedDBAdapter;
import edu.chalmers.project.data.PlayerDBAdapter;


public class MatchInfoFragment extends Fragment {

	private View view;
	private MapView mapView;
	private MapController mc;
	LocationManager locMgr;
	MyLocationListener locLstnr;
	String provider;
	Location currentLocation;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null){// Inflate the layout for this fragment
	    	view = inflater.inflate(R.layout.match_info_fragment, container, false);
	    	view.setClickable(true);
        }
    	Bundle b = getActivity().getIntent().getExtras();
        int position = b.getInt("position_id_match");        
        
        TextView idOrganizer = (TextView)view.findViewById(R.id.textViewIdOrganizerMatch);
        TextView nameMatch = (TextView)view.findViewById(R.id.textViewNameOfMatch);
        TextView dateMatch = (TextView)view.findViewById(R.id.textViewDateOfMatch);
        TextView timeMatch = (TextView)view.findViewById(R.id.textViewTimeOfMatch);
        TextView placeMatch = (TextView)view.findViewById(R.id.textViewPlaceOfMatch);
        TextView fieldMatch = (TextView)view.findViewById(R.id.textViewFieldOfMatch);
        TextView costMatch = (TextView)view.findViewById(R.id.textViewCostOfMatch);
        TextView limitePlayersMatch = (TextView)view.findViewById(R.id.textViewLimitPlayersMatch);
        
        PlayerDBAdapter playerAdapter = new PlayerDBAdapter(container.getContext());
        playerAdapter.open();  
        
        MatchDBAdapter matchAdapter = new MatchDBAdapter(container.getContext());
        matchAdapter.open();
        Cursor cursorMatch = matchAdapter.getMatch(position);  
        
        int id_organizer = Integer.parseInt(cursorMatch.getString(8));
        Cursor cursorPlayer = playerAdapter.getPlayer(id_organizer);
        
        MatchPlayedDBAdapter adapter = new MatchPlayedDBAdapter(container.getContext());
        adapter.open();     
        
        idOrganizer.setText(cursorPlayer.getString(2));
        nameMatch.setText(cursorMatch.getString(3));
        dateMatch.setText(cursorMatch.getString(1));
        timeMatch.setText(cursorMatch.getString(2));
        placeMatch.setText(cursorMatch.getString(5));
        fieldMatch.setText(cursorMatch.getString(4));
        costMatch.setText(cursorMatch.getString(6) + " €");
        limitePlayersMatch.setText(adapter.getNumberPlayersJoined(cursorMatch.getLong(0))+
        		"/"+cursorMatch.getString(7));
        
		cursorPlayer.close();
        playerAdapter.close();
        adapter.close();
        
        ////// google maps ////////
        
        mapView = (MapView) view.findViewById(R.id.mapView1);
        mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		mapView.setSatellite(true);
		locLstnr = new MyLocationListener();
		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locLstnr);
		locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locLstnr);

		String networkProvider = LocationManager.NETWORK_PROVIDER;
		currentLocation = locMgr.getLastKnownLocation(networkProvider);

		
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = getResources().getDrawable(R.drawable.google_maps_marker);
		MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, getActivity());
		
		String coordinates[] = {""+currentLocation.getLatitude(), ""+currentLocation.getLongitude()};
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);

		GeoPoint myPosition = new GeoPoint((int)(lat * 1E6),(int)(lng * 1E6));//Myposition
		OverlayItem overlayitem = new OverlayItem(myPosition, "hey", "My location");
		itemizedoverlay.addOverlay(overlayitem);

		GeoPoint fieldLocation = itemizedoverlay.getFieldLocation(cursorMatch.getString(5));
		if (fieldLocation!=null){
			OverlayItem overlayItem2 = new OverlayItem(fieldLocation,
					cursorMatch.getString(3), cursorMatch.getString(4));
			itemizedoverlay.addOverlay(overlayItem2);		
			mc.animateTo(fieldLocation);
			mc.setZoom(16); //between 1 and 21
		}
		else{
			Toast.makeText(getActivity(), "Place not found", Toast.LENGTH_LONG).show();
		}
		mapOverlays.add(itemizedoverlay);
		mapView.invalidate();
        

        cursorMatch.close();
        matchAdapter.close();      
        return view;
       
    }
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This verification should be done during onStart() because the system calls
	    // this method when the user returns to the activity, which ensures the desired
	    // location provider is enabled each time the activity resumes from the stopped state.
	   locMgr = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
	    final boolean gpsEnabled = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    if (!gpsEnabled) {
	        // Build an alert dialog here that requests that the user enable
	        // the location services, then when the user clicks the "OK" button,
	        // call enableLocationSettings()
	    	enableLocationSettings();
	    }
	}


	private void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}	
	
	
	@Override
	public void onDetach() {
		super.onDetach();
		locMgr.removeUpdates(locLstnr);
	}

    
    
    
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
