package edu.chalmers.project;


import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.chalmers.project.data.Match;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.Player;


public class ResultFragment extends Fragment {


	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.result_fragment, container, false);
    	view.setClickable(true);
            
    	
        
        return view;
       
    }
    
}
