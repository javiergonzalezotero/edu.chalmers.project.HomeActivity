package edu.chalmers.project;


import java.util.ArrayList;

import android.R.integer;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.MatchPlayedDBAdapter;
import edu.chalmers.project.data.Player;


public class PlayersFragment extends Fragment {

	public static final int HOST = 1;
	public static final int GUEST = 2;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	ArrayList<Player> hostTeam = new ArrayList<Player>();
    	ArrayList<Player> guestTeam = new ArrayList<Player>();
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.players_fragment, container, false);
    	view.setClickable(true);
            
    	Bundle b = getActivity().getIntent().getExtras();
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
        
    	ListView lvListHost = (ListView) view.findViewById(R.id.listViewHostTeam);
    	ListView lvListGuest = (ListView) view.findViewById(R.id.listViewGuestTeam);
        lvListHost.setAdapter(new ArrayAdapter<Player>(container.getContext(), android.R.layout.simple_list_item_1, hostTeam));
        lvListGuest.setAdapter(new ArrayAdapter<Player>(container.getContext(), android.R.layout.simple_list_item_1,guestTeam));

        
      /*  lvList.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        			Intent intent = new Intent(getActivity(), MatchActivity.class);
        			intent.putExtra("position_match", position);
        			startActivity(intent);
        		
        	}
        });*/
        
        return view;
       
    }
    
  
    public boolean isJoined(String username, ArrayList<Player> team){
    	Player player = new Player(username, null);
    	return (team.contains(player) );
    }
    
    public boolean isFull(ArrayList<Player> team, int nPlayers){
    	return team.size()==nPlayers;
    }
    
}
