package edu.chalmers.project;


import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
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
import edu.chalmers.project.data.Match;
import edu.chalmers.project.data.MatchDBAdapter;


public class HomeFragment extends Fragment {

	private ArrayList<Match> matchList = new ArrayList();
	Bundle bundle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		view.setClickable(true);
		this.setHasOptionsMenu(true);
		bundle = getActivity().getIntent().getExtras();
		MatchDBAdapter matchAdapter = new MatchDBAdapter(container.getContext());
		matchAdapter.open();

		this.matchList = matchAdapter.getMatchList();
		matchAdapter.close();
		ListView lvList = (ListView) view.findViewById(R.id.listViewHomeMatches);
		lvList.setAdapter(new ArrayAdapter<Match>(container.getContext(), android.R.layout.simple_list_item_1, this.matchList));

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

		return view;

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.activity_home, menu);

	}

}
