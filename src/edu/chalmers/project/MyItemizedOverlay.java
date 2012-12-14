package edu.chalmers.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.R.drawable;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem>{
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}
	
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	public void editOverlay(int i, OverlayItem overlay){
		mOverlays.remove(i);
		mOverlays.add(i, overlay);
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}
	
	@Override
	public int size() {
	  return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	
	public GeoPoint getFieldLocation (String strAddress){
    	Geocoder coder = new Geocoder(mContext, Locale.ENGLISH);
    	List<Address> address;

    	try {
    	    address = coder.getFromLocationName(strAddress,5);
    	    if (address == null || address.size()==0) {
    	        return null;
    	    }
    	    Address location = address.get(0);
    	    location.getLatitude();
    	    location.getLongitude();

    	    GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6),
    	                      (int) (location.getLongitude() * 1E6));
    	     return point;
    	}
    	catch(IOException e){
    		return null;
    	}
    }
}
