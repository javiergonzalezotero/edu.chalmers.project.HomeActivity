package edu.chalmers.project;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.MapActivity;

import edu.chalmers.project.data.DBAdapter;
import edu.chalmers.project.data.PlayerDBAdapter;

public class HomeActivity extends MapActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
       

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab tab = actionBar.newTab()
                .setText("HOME")
                .setTabListener(new TabListener<HomeFragment>(
                        this, "home", HomeFragment.class));
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
                .setText("MAP")
                .setTabListener(new TabListener<MapFragment>(
                        this, "map", MapFragment.class));
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
                .setText("PROFILE")
                .setTabListener(new TabListener<ProfileFragment>(
                        this, "profile", ProfileFragment.class));
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
                .setText("SEARCH")
                .setTabListener(new TabListener<SearchFragment>(
                        this, "search", SearchFragment.class));
        actionBar.addTab(tab);
        
       /* final TextView view = (TextView) findViewById(R.id.textViewMatchPlayed);
        view.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
            Intent intent = new Intent (null, MatchActivity.class);
            startActivity(intent);
          }

        });*/
    }
  
	public void viewMatches(View view){
		Intent intent = new Intent (this, MatchActivity.class);
        startActivity(intent);
	}
	
	
	 public void seeFriends(View view){
	    	Intent intent = new Intent(this, FriendsActivity.class);
	    	Bundle bundle = this.getIntent().getExtras();
	    	intent.putExtras(bundle);
	    	startActivity(intent);
	 }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case android.R.id.home:
			onBackPressed();
			return true;
		
    	case R.id.menu_add_new_event: //Add event in home fragment
    		Bundle b2 = this.getIntent().getExtras();
    		Intent intent = new Intent(this,CreateEventActivity.class);
    		intent.putExtras(b2);
    		startActivity(intent);
    		return true;
    	case R.id.menu_edit_profile: //Edit profile in profile fragment
    		Bundle b = this.getIntent().getExtras();
    		Intent intent2 = new Intent(this,NewAccountActivity.class);
    		intent2.putExtras(b);
    		startActivity(intent2);
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

	class TabListener<T extends Fragment> implements ActionBar.TabListener {
	    private Fragment mFragment;
	    private final Activity mActivity;
	    private final String mTag;
	    private final Class<T> mClass;

	    /** Constructor used each time a new tab is created.
	      * @param activity  The host Activity, used to instantiate the fragment
	      * @param tag  The identifier tag for the fragment
	      * @param clz  The fragment's Class, used to instantiate the fragment
	      */
	    public TabListener(Activity activity, String tag, Class<T> clz) {
	        mActivity = activity;
	        mTag = tag;
	        mClass = clz;
	    }

	    /* The following are each of the ActionBar.TabListener callbacks */

	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	        // Check if the fragment is already initialized
	        if (mFragment == null) {
	            // If not, instantiate and add it to the activity
	            mFragment = Fragment.instantiate(mActivity, mClass.getName());
	            ft.add(android.R.id.content, mFragment, mTag);
	        } else {
	            // If it exists, simply attach it in order to show it
	            ft.attach(mFragment);
	        }
	    }

	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	        if (mFragment != null) {
	            // Detach the fragment, because another one is being attached
	            ft.detach(mFragment);
	        }
	    }

	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	        // User selected the already selected tab. Usually do nothing.
	    }
	    
	    
}
