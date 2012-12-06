package edu.chalmers.project;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.MapActivity;

import edu.chalmers.project.data.MatchPlayedDBAdapter;

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
    
    
    public void joinHost(View view){
    	MatchPlayedDBAdapter adapter = new MatchPlayedDBAdapter(this);
    	adapter.open();
    	Bundle bundle = this.getIntent().getExtras();
    	String playerUsername = bundle.getString("username");
    	int idMatch = bundle.getInt("position_id_match");
    	adapter.joinMatch(playerUsername, idMatch, 1);
    	adapter.close();
    }
    
    public void joinGuest(View view){
    	MatchPlayedDBAdapter adapter = new MatchPlayedDBAdapter(this);
    	adapter.open();
    	Bundle bundle = this.getIntent().getExtras();
    	String playerUsername = bundle.getString("username");
    	int idMatch = bundle.getInt("position_id_match");
    	adapter.joinMatch(playerUsername, idMatch, 2);
    	adapter.close();
    }
    
    public void changeResult(View view){
    	Bundle bundle = this.getIntent().getExtras();
    	String playerUsername = bundle.getString("username");
    	int idMatch = bundle.getInt("position_id_match");
		Intent intent = new Intent(this, ChangeResultActivity.class);
		intent.putExtra("position_id_match", idMatch);
		intent.putExtra("username", playerUsername);
		startActivity(intent);
	}

    public void quitMatch(View view){
    	MatchPlayedDBAdapter adapter = new MatchPlayedDBAdapter(this);
    	adapter.open();
    	Bundle bundle = this.getIntent().getExtras();
    	String playerUsername = bundle.getString("username");
    	int idMatch = bundle.getInt("position_id_match");
    	adapter.quitMatch(playerUsername, idMatch);
    	adapter.close();
    }

}
