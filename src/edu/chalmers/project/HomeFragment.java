package edu.chalmers.project;


import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.chalmers.project.data.Match;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.PlayerDBAdapter;


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
		this.registerForContextMenu(lvList);
		matchSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
		    	matchAdapter.open();
		        if(i==0)
		        	matchList = matchAdapter.getMatchList();
		        else if(i==1)
		        	matchList = matchAdapter.getUpcomingEvents(username);
		        else if(i==2)
		        	matchList = matchAdapter.getMyEvents(username);
		        else if(i==3)
		        	matchList = matchAdapter.getPastEvents();
		        matchAdapter.close();
		        
				lvList.setAdapter(new MatchListAdapter(getActivity(), matchList, R.layout.match_list_item));
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
		

		return view;

	}
	
	 @Override
	 public void onCreateContextMenu(ContextMenu menu, View v,
	     ContextMenuInfo menuInfo) {
	     AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	     int idMatch = matchList.get(info.position).getId();
	     MatchDBAdapter adapter = new MatchDBAdapter(this.getActivity());
	     PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this.getActivity());
	     playerAdapter.open();
	     adapter.open();
	     Cursor cursor = adapter.getIdOrganizer(idMatch);
	     Cursor playerCursor = playerAdapter.getPlayer(username);
	     if(cursor.getLong(1)==playerCursor.getLong(9)){
		     String[] menuItems = getResources().getStringArray(R.array.ContextMenu);
		     for (int i = 0; i<menuItems.length; i++) {
		       menu.add(Menu.NONE, i, i, menuItems[i]);
		     }
	     }
	     else{
	    	 Toast.makeText(getActivity(),"You are not the organizer", Toast.LENGTH_LONG).show();
	     }
	     cursor.close();
	     playerCursor.close();
	     adapter.close();
	     playerAdapter.close();
	 }
	 
	 @Override
	 public boolean onContextItemSelected(MenuItem item) {
	   AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	   int idMatch = matchList.get(info.position).getId();
	   MatchDBAdapter adapter = new MatchDBAdapter(this.getActivity());
	   adapter.open();
	   if (item.getItemId()==0){ //Edit match
		    Intent intent = new Intent(this.getActivity(),CreateEventActivity.class);
			intent.putExtras(bundle);
			intent.putExtra("idMatch", idMatch);
			startActivity(intent);
	   }
	   else if(item.getItemId()==1){ //Delete match
		    adapter.deleteMatch(idMatch);
		    Intent intent = new Intent(this.getActivity(),HomeActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
	   }
	   adapter.close();
	   return true;
	 }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.activity_home, menu);

	}

}
