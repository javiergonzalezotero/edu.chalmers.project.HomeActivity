package edu.chalmers.project;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import edu.chalmers.project.data.Player;
import edu.chalmers.project.data.PlayerDBAdapter;


public class SearchFragment extends Fragment {

	private ArrayList<Player> players;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	this.players = new ArrayList<Player>();
    	PlayerDBAdapter adapter = new PlayerDBAdapter(container.getContext());
    	adapter.open();
    	players = adapter.getPlayerList();
    	adapter.close();
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.search_fragment, container, false);
    	view.setClickable(true);
            
    	ArrayAdapter<Player> arrayAdapter = new ArrayAdapter<Player>(this.getActivity(),
    			android.R.layout.simple_dropdown_item_1line,players);
    	AutoCompleteTextView acTextView = 
    			(AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextViewSearch);
    	acTextView.setThreshold(2);  	
    	acTextView.setAdapter(arrayAdapter);
        return view;
       
    }
	public ArrayList<Player> getPlayers() {
		return players;
	}
    
    
    
}