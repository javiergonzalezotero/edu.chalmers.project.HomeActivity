package edu.chalmers.project;


import java.util.ArrayList;
import java.util.ListIterator;
import java.util.StringTokenizer;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.chalmers.project.data.Availability;
import edu.chalmers.project.data.AvailabilityDBAdapter;
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
				else if(i==4)
					matchList = myAvailableMatches(username, view);

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
	 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
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

	public ArrayList<Match> myAvailableMatches(String username, View view){
		ArrayList<Match> upcomingMatchList = new ArrayList();
		ArrayList<Match> availableMatchList = new ArrayList();
		ArrayList<Availability> timeAvailability = new ArrayList<Availability>();

		matchAdapter.open();
		upcomingMatchList = matchAdapter.getUpcomingEvents(username);

		matchAdapter.close();

		for(int i=0; i<upcomingMatchList.size(); i++){

			String newDateFormat = upcomingMatchList.get(i).getDate().replace("/", "-");
			String dayDate = getDayDate(newDateFormat);
			String[] tokensTime = upcomingMatchList.get(i).getTime().split(":");

			AvailabilityDBAdapter availabilityAdapter = new AvailabilityDBAdapter(view.getContext());
			availabilityAdapter.open();
			timeAvailability = availabilityAdapter.getAvailabilityBis(username, dayDate);
			int conditionOk = 0;
			if(!(timeAvailability.isEmpty())){

				ListIterator it = timeAvailability.listIterator();
				while(it.hasNext()){
					String currentString = "" + it.next();
					StringTokenizer st = new StringTokenizer(currentString,"-");
					String stringTested;

					int first=1;
					int condition1 = 0;
					int condition2 = 0;

					while (st.hasMoreTokens() ) {
						stringTested = st.nextToken();
						if(first == 1){ //first time that we are in the loop
							if(Integer.parseInt(tokensTime[0]) >= Integer.parseInt(stringTested)){
								condition1 = 1;	
							}
							first = 2;
						}
						else{
							if(Integer.parseInt(tokensTime[0]) < Integer.parseInt(stringTested)){
								condition2 = 1;
							}
						}					
					}
					if((condition1 == 1) && (condition2 == 1)){
						conditionOk = 1;
					}
				}
			}

			if(conditionOk == 1){
				//Toast.makeText(view.getContext(), "okkkk", Toast.LENGTH_SHORT).show();
				Match m = new Match(upcomingMatchList.get(i).getId(), upcomingMatchList.get(i).getName(), 
						upcomingMatchList.get(i).getDate(), upcomingMatchList.get(i).getTime(), upcomingMatchList.get(i).getLocation(), 
						upcomingMatchList.get(i).getField(), upcomingMatchList.get(i).getCost(), 
						upcomingMatchList.get(i).getNumberPlayers(), upcomingMatchList.get(i).getId_organizer());

				availableMatchList.add(m);
			}

		}

		return availableMatchList;
	}

	public String getDayDate(String dayDate){
		matchAdapter.open();
		Cursor cursorDate = matchAdapter.getDayDate(dayDate);
		matchAdapter.close();
		String returnDayDate = "";
		if(cursorDate.getInt(cursorDate.getPosition()) == 0 ){
			returnDayDate = "sunday";
		}
		else if(cursorDate.getInt(cursorDate.getPosition()) == 1 ){
			returnDayDate = "monday";
		}
		else if(cursorDate.getInt(cursorDate.getPosition()) == 2 ){
			returnDayDate = "tuesday";
		}
		else if(cursorDate.getInt(cursorDate.getPosition()) == 3 ){
			returnDayDate = "wednesday";
		}
		else if(cursorDate.getInt(cursorDate.getPosition()) == 4 ){
			returnDayDate = "thursday";
		}
		else if(cursorDate.getInt(cursorDate.getPosition()) == 5 ){
			returnDayDate = "friday";
		}
		else if(cursorDate.getInt(cursorDate.getPosition()) == 6 ){
			returnDayDate = "saturday";
		}

		return returnDayDate;

	}

}
