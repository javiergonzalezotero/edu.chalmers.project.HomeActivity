package edu.chalmers.project;


import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.chalmers.project.data.Goal;
import edu.chalmers.project.data.GoalDBAdapter;


public class ResultFragment extends Fragment {

	private ArrayList<Goal> listGoalTeamHost;
	private ArrayList<Goal> listGoalTeamGuest;
	private TextView textViewGoalHost;
	private TextView textViewGoalGuest;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.result_fragment, container, false);
    	view.setClickable(true);
            
    	Bundle b = getActivity().getIntent().getExtras();
        int idMatch = b.getInt("position_id_match");  
        this.listGoalTeamHost = new ArrayList<Goal>();
        
        this.textViewGoalHost = (TextView)view.findViewById(R.id.textViewGoalHost);
        this.textViewGoalGuest = (TextView)view.findViewById(R.id.textViewGoalGuest);
        
        GoalDBAdapter goalAdapter = new GoalDBAdapter(container.getContext());
        goalAdapter.open();
        
        this.listGoalTeamHost = goalAdapter.getGoalTeam(idMatch, 1);
        this.listGoalTeamGuest = goalAdapter.getGoalTeam(idMatch, 2);
                
        this.textViewGoalHost.setText("" + this.listGoalTeamHost.size());
        this.textViewGoalGuest.setText("" + this.listGoalTeamGuest.size());
        
        goalAdapter.close();
        
        
    	
        
        return view;
       
    }
}
