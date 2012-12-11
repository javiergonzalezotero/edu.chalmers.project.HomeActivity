package edu.chalmers.project;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.MatchPlayedDBAdapter;
import edu.chalmers.project.data.Player;
import edu.chalmers.project.data.PlayerDBAdapter;


public class ChangeResultActivity extends Activity {

	public static final int HOST = 1;
	public static final int GUEST = 2;
	private int idMatch;
	private String playerUsername;
	private String nameMvpSelected;
	private ArrayList<Player> hostTeam = new ArrayList<Player>();
	private ArrayList<Player> guestTeam = new ArrayList<Player>();
	private ArrayList<Player> spinnerArray = new ArrayList<Player>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_result);		
		
		this.hostTeam = new ArrayList<Player>();
    	this.guestTeam = new ArrayList<Player>();
            
    	Bundle b = getIntent().getExtras();
    	this.playerUsername = b.getString("username");
    	this.idMatch = b.getInt("position_id_match");
    	
    	MatchDBAdapter adapter = new MatchDBAdapter(this);
    	adapter.open();
    	Cursor cursor = adapter.getMatch(this.idMatch);
    	this.setTitle("Change result of " + cursor.getString(3));
    	cursor.close();
    	adapter.close();
 
    	MatchPlayedDBAdapter matchPlayedAdapter = new MatchPlayedDBAdapter(this);
        matchPlayedAdapter.open();
   
        hostTeam = matchPlayedAdapter.getTeam(HOST, idMatch);
        guestTeam = matchPlayedAdapter.getTeam(GUEST, idMatch);
       
    	ListView lvListHost = (ListView) findViewById(R.id.listViewHostTeamChangeResult);
    	ListView lvListGuest = (ListView) findViewById(R.id.listViewGuestTeamChangeResult);
        lvListHost.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, hostTeam));
        lvListGuest.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1,guestTeam));
        
        Spinner spinner = (Spinner)findViewById(R.id.spinnerMVP);
        
        spinnerArray = matchPlayedAdapter.getTeam(HOST, idMatch);
        spinnerArray.addAll(matchPlayedAdapter.getTeam(GUEST, idMatch));
        matchPlayedAdapter.close();
        ArrayAdapter<Player> spinnerArrayAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
      

        lvListHost.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){	
				String playerUsernameSelected = hostTeam.get(position).getUsername();
				//Toast.makeText(view.getContext(), "username " + playerUsernameSelected, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(view.getContext(), PopupChangeResultActivity.class);
				intent.putExtra("idMatch", idMatch);
				intent.putExtra("playerUsernameSelected", playerUsernameSelected);
				intent.putExtra("currentUsernameLogin", playerUsername);
				intent.putExtra("nextGoal", 0);
				startActivity(intent);

			}
		});
        
        lvListGuest.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){	
				String playerUsernameSelected = guestTeam.get(position).getUsername();
				//Toast.makeText(view.getContext(), "username " + playerUsernameSelected, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(view.getContext(), PopupChangeResultActivity.class);
				intent.putExtra("idMatch", idMatch);
				intent.putExtra("playerUsernameSelected", playerUsernameSelected);
				intent.putExtra("currentUsernameLogin", playerUsername);
				intent.putExtra("nextGoal", 0);
				startActivity(intent);

			}
		});
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	
        	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j){
        		nameMvpSelected = spinnerArray.get(i).getUsername();        		       		
        	}
        	
        	 public void onNothingSelected(AdapterView<?> adapterView) {
 		        return;
 		    } 
        
		});
		    
		
		
		
	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_change_result, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    		ActionBar actionBar = getActionBar();
    		actionBar.setDisplayHomeAsUpEnabled(true);
    		actionBar.setDisplayShowTitleEnabled(true);
    	}
        return true;
    }
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_change:
			PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this);
    		playerAdapter.open();
    		Cursor cursorPlayer = playerAdapter.getPlayer(nameMvpSelected);
    		int idMvp = Integer.parseInt(cursorPlayer.getString(9));
    		playerAdapter.close();
    		
    		MatchDBAdapter matchAdapter = new MatchDBAdapter(this);
    		matchAdapter.open();
    		
    		matchAdapter.updateMvp(idMatch, idMvp);
    		matchAdapter.close();
    		
    		Intent intent = new Intent(this, MatchActivity.class);
			intent.putExtra("position_id_match", idMatch);
			intent.putExtra("tab_position", 2);
			intent.putExtra("username", playerUsername);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
		
			startActivity(intent);
    		
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
    
  
    
}

