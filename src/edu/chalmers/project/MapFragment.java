package edu.chalmers.project;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		if (view==null){
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

		// Set Click Listener
        locateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
        		mapOverlays.clear();
        		locLstnr = new MyLocationListener();
        		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, locLstnr);
        		Criteria locationCritera = new Criteria();
        		locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
        		locationCritera.setAltitudeRequired(false);
        		locationCritera.setBearingRequired(false);
        		locationCritera.setCostAllowed(true);
        		locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
        		provider = locMgr.getBestProvider(locationCritera, true);
        		String provider2 = locMgr.NETWORK_PROVIDER;
        		Location location  = locMgr.getLastKnownLocation(provider);

        		Log.i("--- Latitude",""+location.getLatitude());
        		Log.i("--- Latitude",""+location.getLongitude());
        		
        		
    			String coordinates[] = {""+location.getLatitude(), ""+location.getLongitude()};
        		double lat = Double.parseDouble(coordinates[0]);
        		double lng = Double.parseDouble(coordinates[1]);
        		
        		GeoPoint myPosition = new GeoPoint((int)(lat * 1E6),(int)(lng * 1E6));//Myposition
        		OverlayItem overlayitem = new OverlayItem(myPosition, "My position", null);
        		if(itemizedoverlay.size()==0){ //first time  			
        			itemizedoverlay.addOverlay(overlayitem);
        		}
        		else{ //update our position
	        		itemizedoverlay.editOverlay(0, overlayitem);
        		}
        		matchList = HomeActivity.getMatchList();
        		for (Match m : matchList) {
        			GeoPoint fieldLocation = itemizedoverlay.getFieldLocation(m.getLocation());
        			if (fieldLocation!=null){
        				OverlayItem newOverlay = new OverlayItem(fieldLocation,m.getName(), m.getField() +
        						"\n" + m.getDate()+ " " + m.getTime() + "\n"  + m.getCost()+" € \n");
            			itemizedoverlay.addOverlay(newOverlay);
        			}
        					
				}
        		mapOverlays.add(itemizedoverlay);
        		mc.animateTo(myPosition);
        		mc.setZoom(15); //between 1 and 21
        		mapView.invalidate();
        		
            }
        });
		
		
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
		
		@Override
		public void onLocationChanged(Location loc){
			/*loc.getLatitude();
			loc.getLongitude();
			String Text = "My current location is: " +
					"Latitud = " + loc.getLatitude() +
					"Longitud = " + loc.getLongitude();
			Toast.makeText( getActivity().getApplicationContext(), Text, Toast.LENGTH_SHORT).show();*/

			
			
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
