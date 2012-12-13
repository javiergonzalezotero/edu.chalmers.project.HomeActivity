package edu.chalmers.project;

import java.util.List;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class MapFragment extends Fragment  {
	
	private MapView mapView;
	private View view;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 // Inflate the layout for this fragment
		if (view==null){
	    	view = inflater.inflate(R.layout.map_fragment, container, false);
	    	view.setClickable(true);
		}
		mapView = (MapView) view.findViewById(R.id.mainMap);
		mapView.setBuiltInZoomControls(true);
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.black);
		MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, this.getActivity());
		GeoPoint point = new GeoPoint(19240000,-99120000);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
		itemizedoverlay.addOverlay(overlayitem);
		GeoPoint point2 = new GeoPoint(35410000, 139460000);
		OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!", "I'm in Japan!");
		itemizedoverlay.addOverlay(overlayitem2);
		mapOverlays.add(itemizedoverlay);
		return view;
	}
/*	private MapController mapController;
	private LocationManager locMgr;
	private MyLocationListener locLstnr;
    private GeoPoint p;
    List<Overlay> listOfOverlays ;
    private MapView map;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.map_fragment, container, false);
    	view.setClickable(true);
        map = (MapView) view.findViewById(R.id.mainMap); 
        map.setBuiltInZoomControls(true);
        mapController=map.getController();
        locMgr = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locLstnr = new MyLocationListener();
       // locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locLstnr);
        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String providerName = locMgr.getBestProvider(locationCritera, true);
        Location location  = locMgr.getLastKnownLocation(providerName);

        Log.i("--- Latitude",""+location.getLatitude());
        Log.i("--- Latitude",""+location.getLongitude());
        

     
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, new GeoUpdateHandler());
        
        return view;
       
    }
    
    public class MyLocationListener implements LocationListener{
    
    	@Override
    public void onLocationChanged(Location loc){
    loc.getLatitude();
    loc.getLongitude();
    String Text = "My current location is: " +
    "Latitud = " + loc.getLatitude() +
    "Longitud = " + loc.getLongitude();
    Toast.makeText( getActivity(), Text, Toast.LENGTH_SHORT).show();
    String coordinates[] = {""+loc.getLatitude(), ""+loc.getLongitude()};
    double lat = Double.parseDouble(coordinates[0]);
    double lng = Double.parseDouble(coordinates[1]);

    GeoPoint p = new GeoPoint(
    (int) (lat * 1E6),
    (int) (lng * 1E6));

    mapController.animateTo(p);
    mapController.setZoom(7);
    map.invalidate();
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    Toast.makeText( getActivity(),
    "Gps Disabled",
    Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    Toast.makeText( getActivity(),
    "Gps Enabled",
    Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    }
*/

/*
class MapOverlay extends com.google.android.maps.Overlay
{
    private GeoPoint p;
    private int res;

    public MapOverlay(GeoPoint p,int res){
        this.p=p;
        this.res=res;
    }

@Override
 public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
 {
     super.draw(canvas, mapView, shadow);                   

     //---translate the GeoPoint to screen pixels---
     Point screenPts = new Point();
     mapView.getProjection().toPixels(p, screenPts);

     //---add the marker---
     Bitmap bmp = BitmapFactory.decodeResource(getResources(),res);            
     canvas.drawBitmap(bmp, screenPts.x, screenPts.y-20, null);         
     return true;
 }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) 
    {   
        //---when user lifts his finger---
        if (event.getAction() == 1) {                
             GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),(int) event.getY());
             mapController.animateTo(p);
             MapOverlay mapOverlay = new MapOverlay(p,R.drawable.ic_launcher);
             listOfOverlays.add(mapOverlay);        
             mapView.invalidate();
        }                            
        return false;
    }        
}

class GeoUpdateHandler implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        int lat=(int)(location.getLatitude()*1E6);
        int lng=(int)(location.getLongitude()*1E6);         
        GeoPoint p=new GeoPoint(lat,lng);
        mapController.animateTo(p);
        MapOverlay mapOverlay = new MapOverlay(p,R.drawable.ic_menu_save);
        listOfOverlays.add(mapOverlay);        
        map.invalidate();

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

}*/
}
