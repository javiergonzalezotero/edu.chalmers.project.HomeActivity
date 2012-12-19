package edu.chalmers.project;


import java.util.ArrayList;
import java.util.ListIterator;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import edu.chalmers.project.data.Goal;
import edu.chalmers.project.data.GoalDBAdapter;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.MatchPlayedDBAdapter;
import edu.chalmers.project.data.Player;
import edu.chalmers.project.data.PlayerDBAdapter;


public class ResultFragment extends Fragment {

	private ArrayList<Goal> listGoalTeamHost;
	private ArrayList<Goal> listGoalTeamGuest;
	private ArrayList<Player> listPlayersNotPresent;
	private ArrayList<String> listPlayersNotPresentString;
	private TextView textViewGoalHost;
	private TextView textViewGoalGuest;
	private TextView textViewMVPName;
	private Button buttonChangeResult;
	private TableLayout tableLayoutResult;
	private ListView listViewGoalsHost;
	private ListView listViewGoalsGuest;
	private ListView listViewPlayersNotPresent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.result_fragment, container, false);
		view.setClickable(true);

		Bundle b = getActivity().getIntent().getExtras();
		int idMatch = b.getInt("position_id_match");  
		String playerUsername = b.getString("username");

		this.listGoalTeamHost = new ArrayList<Goal>();
		this.listPlayersNotPresent = new ArrayList<Player>();
		this.listPlayersNotPresentString = new ArrayList<String>();

		this.textViewGoalHost = (TextView)view.findViewById(R.id.textViewGoalHost);
		this.textViewGoalGuest = (TextView)view.findViewById(R.id.textViewGoalGuest);
		this.buttonChangeResult = (Button)view.findViewById(R.id.buttonChangeResult);
		this.tableLayoutResult = (TableLayout)view.findViewById(R.id.tableLayoutResult);
		this.textViewMVPName = (TextView)view.findViewById(R.id.textViewMVPName);
		this.listViewGoalsHost = (ListView)view.findViewById(R.id.listViewGoalsHost);
		this.listViewGoalsGuest = (ListView)view.findViewById(R.id.listViewGoalsGuest);
		this.listViewPlayersNotPresent = (ListView)view.findViewById(R.id.listViewPlayersNotPresent);

		this.buttonChangeResult.setVisibility(View.INVISIBLE);

		GoalDBAdapter goalAdapter = new GoalDBAdapter(container.getContext());
		goalAdapter.open();

		this.listGoalTeamHost = goalAdapter.getGoalTeam(idMatch, 1);
		this.listGoalTeamGuest = goalAdapter.getGoalTeam(idMatch, 2);

		this.textViewGoalHost.setText("" + this.listGoalTeamHost.size());
		this.textViewGoalGuest.setText("" + this.listGoalTeamGuest.size());

		goalAdapter.close();

		listViewGoalsHost.setAdapter(new ArrayAdapter<Goal>(container.getContext(), android.R.layout.simple_list_item_1, this.listGoalTeamHost));
		listViewGoalsGuest.setAdapter(new ArrayAdapter<Goal>(container.getContext(), android.R.layout.simple_list_item_1, this.listGoalTeamGuest));
		listViewGoalsHost.setClickable(false);
		listViewGoalsGuest.setClickable(false);

		MatchDBAdapter matchAdapter = new MatchDBAdapter(container.getContext());
		matchAdapter.open();

		Cursor cursorOrganizer = matchAdapter.getIdOrganizer(idMatch);

		matchAdapter.close();

		PlayerDBAdapter playerAdapter = new PlayerDBAdapter(container.getContext());
		playerAdapter.open();

		Cursor cursorPlayer = playerAdapter.getPlayer(playerUsername);

		matchAdapter.open();
		Cursor cursorIdMvp = matchAdapter.getMvp(idMatch);
		if(cursorIdMvp.getString(0) != null){
			int idMvp = Integer.parseInt(cursorIdMvp.getString(0));
			Cursor cursorMvp = playerAdapter.getPlayer(idMvp);
			if(cursorMvp.getCount() != 0){
				String mvpUsername = cursorMvp.getString(2);
				textViewMVPName.setText(mvpUsername);
			}
		}

		playerAdapter.close();
		Cursor cursor = matchAdapter.getMatch(idMatch);
		if((Integer.parseInt(cursorOrganizer.getString(1)) == Integer.parseInt(cursorPlayer.getString(9))) &&
				!(PlayersFragment.isFuture(cursor.getString(1))>=1)){
			this.buttonChangeResult.setVisibility(View.VISIBLE);
		}
		cursor.close();
		matchAdapter.close();

		MatchPlayedDBAdapter matchPlayedAdapter = new MatchPlayedDBAdapter(container.getContext());
		matchPlayedAdapter.open();
		this.listPlayersNotPresent = matchPlayedAdapter.getPlayersNotPresent(idMatch);
		matchPlayedAdapter.close();

		ListIterator it = this.listPlayersNotPresent.listIterator();

		while(it.hasNext()){
			this.listPlayersNotPresentString.add(it.next() + " was not present");
		}

		listViewPlayersNotPresent.setAdapter(new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_list_item_1, this.listPlayersNotPresentString));





		return view;

	}
}
