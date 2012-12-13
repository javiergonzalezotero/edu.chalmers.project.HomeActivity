package edu.chalmers.project;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.MatchPlayedDBAdapter;
import edu.chalmers.project.data.Player;


public class PlayersFragment extends Fragment {

	public static final int HOST = 1;
	public static final int GUEST = 2;
	private ArrayList<Player> hostTeam;
	private ArrayList<Player> guestTeam;
	Bundle b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	hostTeam = new ArrayList<Player>();
    	guestTeam = new ArrayList<Player>();
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.players_fragment, container, false);
    	view.setClickable(true);
            
    	b = getActivity().getIntent().getExtras();
    	int idMatch = b.getInt("position_id_match");
    	String playerUsername = b.getString("username");
    	
    	MatchDBAdapter matchAdapter = new MatchDBAdapter(container.getContext());
    	matchAdapter.open();
    	MatchPlayedDBAdapter matchPlayedAdapter = new MatchPlayedDBAdapter(container.getContext());
        matchPlayedAdapter.open();
        Cursor cursor = matchAdapter.getMatch(idMatch);
        int nPlayersTeam = cursor.getInt(7)/2; //Number of players of each team
        
        hostTeam = matchPlayedAdapter.getTeam(HOST, idMatch);
        guestTeam = matchPlayedAdapter.getTeam(GUEST, idMatch);
        matchPlayedAdapter.close();
        
        Button joinHostButton = (Button) view.findViewById(R.id.buttonJoinHost);
        Button joinGuestButton = (Button) view.findViewById(R.id.buttonJoinGuest);
        Button quitHostButton = (Button) view.findViewById(R.id.buttonQuitHost);
        Button quitGuestButton = (Button) view.findViewById(R.id.buttonQuitGuest);
        
        if(isFuture(cursor.getString(1))>=1){
	        if (isJoined(playerUsername, hostTeam)){
	        	joinHostButton.setVisibility(View.INVISIBLE);
	        	joinGuestButton.setVisibility(View.INVISIBLE);
	        	quitHostButton.setVisibility(View.VISIBLE);
	        }
	        if (isJoined(playerUsername, guestTeam)){
	        	joinHostButton.setVisibility(View.INVISIBLE);
	        	joinGuestButton.setVisibility(View.INVISIBLE);
	        	quitGuestButton.setVisibility(View.VISIBLE);
	        }
	        
	        if (isFull(hostTeam, nPlayersTeam))
	        	joinHostButton.setVisibility(View.INVISIBLE);
	        
	        if (isFull(guestTeam, nPlayersTeam))
	        	joinGuestButton.setVisibility(View.INVISIBLE);
        }
        else{
        	joinHostButton.setVisibility(View.INVISIBLE);
        	joinGuestButton.setVisibility(View.INVISIBLE);
        }
        cursor.close();
        matchAdapter.close();
        
    	ListView lvListHost = (ListView) view.findViewById(R.id.listViewHostTeam);
    	ListView lvListGuest = (ListView) view.findViewById(R.id.listViewGuestTeam);
        lvListHost.setAdapter(new PlayerListAdapter(this.getActivity(), hostTeam, R.layout.player_list_item));
        lvListGuest.setAdapter(new PlayerListAdapter(this.getActivity(), guestTeam, R.layout.player_list_item));

		lvListGuest.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				String otherUsername = guestTeam.get(position).getUsername();
				String username = b.getString("username");
				Intent intent = new Intent(view.getContext(), HomeActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("other_username", otherUsername);
				intent.putExtra("tab_position", 2);
				startActivity(intent);;

			}
		});

		return view;

	}


	public static int isFuture(String date){
		final String DATE_FORMAT_NOW = "yyyy-MM-dd";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		String now = sdf.format(cal.getTime());
		return (date.compareTo(now));
	}

	public boolean isJoined(String username, ArrayList<Player> team){
		Player player = new Player(username, null);
		return (team.contains(player) );
	}

	public boolean isFull(ArrayList<Player> team, int nPlayers){
		return team.size()==nPlayers;
	}

}
