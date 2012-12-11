package edu.chalmers.project;


import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import edu.chalmers.project.data.Match;
import edu.chalmers.project.data.MatchDBAdapter;


public class HomeFragment extends Fragment {

	private ArrayList<Match> matchList = new ArrayList();
	Bundle bundle;
	MatchDBAdapter matchAdapter;
	String username;
	ListView lvList ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		view.setClickable(true);
		this.setHasOptionsMenu(true);
		
		Spinner matchSelectionSpinner =(Spinner) view.findViewById(R.id.spinnerMatchSelection);
		String matchSelection = matchSelectionSpinner.getSelectedItem().toString();
		String[] spinnerSelections = getResources().getStringArray(R.array.spinner_matches);
		bundle = getActivity().getIntent().getExtras();
		username = bundle.getString("username");
		matchAdapter = new MatchDBAdapter(container.getContext());
		lvList = (ListView) view.findViewById(R.id.listViewHomeMatches);
	
		matchSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
		    	matchAdapter.open();
		        if(i==0)
		        	matchList = matchAdapter.getMatchList();
		        else if(i==1)
		        	matchList = matchAdapter.getUpcomingEvents(username);
		        else if(i==2)
		        	matchList = matchAdapter.getMyEvents(username);
		        matchAdapter.close();
		        
				lvList.setAdapter(new ArrayAdapter<Match>(getActivity(), android.R.layout.simple_list_item_1, matchList));
				lvList.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id){
						
						int idMatch = matchList.get(position).getId();
				    	String username = bundle.getString("username");
						Intent intent = new Intent(getActivity(), MatchActivity.class);
						intent.putExtra("username", username);
						intent.putExtra("position_id_match", idMatch);
						startActivity(intent);

					}
				});
		    } 
		    
		    public void onNothingSelected(AdapterView<?> adapterView) {
		        return;
		    } 
		}); 
		/*if (matchSelection.equals(spinnerSelections[0])){
			this.matchList = matchAdapter.getMatchList();
		}
		else if(matchSelection.equals(spinnerSelections[2])){
			this.matchList = matchAdapter.getMyEvents(username);
		}*/
		
		

		

		return view;

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.activity_home, menu);

	}

}
