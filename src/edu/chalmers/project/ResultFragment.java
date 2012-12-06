package edu.chalmers.project;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.chalmers.project.data.GoalDBAdapter;


public class ResultFragment extends Fragment {


	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.result_fragment, container, false);
    	view.setClickable(true);
            
    	Bundle b = getActivity().getIntent().getExtras();
        int id_match = b.getInt("position_id_match");  
        
        GoalDBAdapter goalAdapter = new GoalDBAdapter(container.getContext());
        goalAdapter.open();
        
        //Cursor cursorGoal = goalAdapter.getGoal(id_match);
        
        goalAdapter.close();
        
        
    	
        
        return view;
       
    }
}
