package edu.chalmers.project;

import com.google.android.maps.MapActivity;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.view.Menu;
import android.view.MenuItem;

public class MatchActivity extends MapActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_match);        
        
         ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab tab = actionBar.newTab()
                .setText("INFO")
                .setTabListener(new TabListener<MatchInfoFragment>(
                        this, "home", MatchInfoFragment.class));
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
                .setText("PLAYERS")
                .setTabListener(new TabListener<PlayersFragment>(
                        this, "map", PlayersFragment.class));
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
                .setText("RESULT")
                .setTabListener(new TabListener<ResultFragment>(
                        this, "profile", ResultFragment.class));
        actionBar.addTab(tab);
      
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
  
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case android.R.id.home:
			onBackPressed();
			return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    //Method need for the map
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
