package edu.chalmers.project;


import java.util.ArrayList;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import edu.chalmers.project.data.Goal;
import edu.chalmers.project.data.GoalDBAdapter;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.PlayerDBAdapter;


public class ResultFragment extends Fragment {

	private ArrayList<Goal> listGoalTeamHost;
	private ArrayList<Goal> listGoalTeamGuest;
	private TextView textViewGoalHost;
	private TextView textViewGoalGuest;
	private Button buttonChangeResult;
	private TableLayout tableLayoutResult;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.result_fragment, container, false);
    	view.setClickable(true);
            
    	Bundle b = getActivity().getIntent().getExtras();
        int idMatch = b.getInt("position_id_match");  
        String playerUsername = b.getString("username");
        
        this.listGoalTeamHost = new ArrayList<Goal>();
        
        this.textViewGoalHost = (TextView)view.findViewById(R.id.textViewGoalHost);
        this.textViewGoalGuest = (TextView)view.findViewById(R.id.textViewGoalGuest);
        this.buttonChangeResult = (Button)view.findViewById(R.id.buttonChangeResult);
        this.tableLayoutResult = (TableLayout)view.findViewById(R.id.tableLayoutResult);
        
        this.buttonChangeResult.setVisibility(View.INVISIBLE);
        
        GoalDBAdapter goalAdapter = new GoalDBAdapter(container.getContext());
        goalAdapter.open();
        
        this.listGoalTeamHost = goalAdapter.getGoalTeam(idMatch, 1);
        this.listGoalTeamGuest = goalAdapter.getGoalTeam(idMatch, 2);
                
        this.textViewGoalHost.setText("" + this.listGoalTeamHost.size());
        this.textViewGoalGuest.setText("" + this.listGoalTeamGuest.size());
        
        goalAdapter.close();
        
        MatchDBAdapter matchAdapter = new MatchDBAdapter(container.getContext());
        matchAdapter.open();
        
        Cursor cursorOrganizer = matchAdapter.getIdOrganizer(idMatch);
        
        PlayerDBAdapter playerAdapter = new PlayerDBAdapter(container.getContext());
        playerAdapter.open();
        
        Cursor cursorPlayer = playerAdapter.getPlayer(playerUsername);

        
        if(Integer.parseInt(cursorOrganizer.getString(1)) == Integer.parseInt(cursorPlayer.getString(9))){
        	this.buttonChangeResult.setVisibility(View.VISIBLE);
        }
        
        
        /* Create a new row to be added. */
        TableRow tr = new TableRow(container.getContext());
        tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
             /* Create a Button to be the row-content. */
             TextView newGoal = new TextView(container.getContext());
             newGoal.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
             newGoal.setText("test");
             newGoal.setGravity(Gravity.LEFT);
             newGoal.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
             
             TextView newGoal2 = new TextView(container.getContext());
             newGoal2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
             newGoal2.setText("test 2");
             newGoal2.setGravity(Gravity.RIGHT);
             newGoal2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
             /* Add Button to row. */
             tr.addView(newGoal);
             tr.addView(newGoal2);
   /* Add row to TableLayout. */
             this.tableLayoutResult.addView(tr);
    	
        
        return view;
       
    }
}
