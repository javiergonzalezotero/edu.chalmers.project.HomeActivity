package edu.chalmers.project;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.chalmers.project.data.MatchPlayedDBAdapter;
import edu.chalmers.project.data.Player;


public class ChangeResultActivity extends Activity {

	public static final int HOST = 1;
	public static final int GUEST = 2;
	private int idMatch;
	private String playerUsername;
	private ArrayList<Player> hostTeam = new ArrayList<Player>();
	private ArrayList<Player> guestTeam = new ArrayList<Player>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_result);
		
		/*
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
		*/
		
		this.hostTeam = new ArrayList<Player>();
    	this.guestTeam = new ArrayList<Player>();
            
    	Bundle b = getIntent().getExtras();
    	this.idMatch = b.getInt("position_id_match");
    	this.playerUsername = b.getString("username");
 
    	MatchPlayedDBAdapter matchPlayedAdapter = new MatchPlayedDBAdapter(this);
        matchPlayedAdapter.open();
   
        hostTeam = matchPlayedAdapter.getTeam(HOST, idMatch);
        guestTeam = matchPlayedAdapter.getTeam(GUEST, idMatch);
        matchPlayedAdapter.close();
    	ListView lvListHost = (ListView) findViewById(R.id.listViewHostTeamChangeResult);
    	ListView lvListGuest = (ListView) findViewById(R.id.listViewGuestTeamChangeResult);
        lvListHost.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, hostTeam));
        lvListGuest.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1,guestTeam));

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
		
		
	}
	
	
    
  
    
}

