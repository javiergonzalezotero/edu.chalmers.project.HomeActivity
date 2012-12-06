package edu.chalmers.project;


import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    	
    	MatchPlayedDBAdapter matchPlayedAdapter = new MatchPlayedDBAdapter(container.getContext());
        matchPlayedAdapter.open();
   
        hostTeam = matchPlayedAdapter.getTeam(HOST, idMatch);
        guestTeam = matchPlayedAdapter.getTeam(GUEST, idMatch);
        matchPlayedAdapter.close();
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
    
  
    
}
