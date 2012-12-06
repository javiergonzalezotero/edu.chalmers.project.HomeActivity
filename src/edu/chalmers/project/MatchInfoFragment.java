package edu.chalmers.project;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.PlayerDBAdapter;


public class MatchInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.match_info_fragment, container, false);
    	view.setClickable(true);
            
    	Bundle b = getActivity().getIntent().getExtras();
        int position = b.getInt("position_id_match");        
        
        TextView idOrganizer = (TextView)view.findViewById(R.id.textViewIdOrganizerMatch);
        TextView nameMatch = (TextView)view.findViewById(R.id.textViewNameOfMatch);
        TextView dateMatch = (TextView)view.findViewById(R.id.textViewDateOfMatch);
        TextView timeMatch = (TextView)view.findViewById(R.id.textViewTimeOfMatch);
        TextView placeMatch = (TextView)view.findViewById(R.id.textViewPlaceOfMatch);
        TextView fieldMatch = (TextView)view.findViewById(R.id.textViewFieldOfMatch);
        TextView costMatch = (TextView)view.findViewById(R.id.textViewCostOfMatch);
        TextView limitePlayersMatch = (TextView)view.findViewById(R.id.textViewLimitPlayersMatch);
        
        PlayerDBAdapter playerAdapter = new PlayerDBAdapter(container.getContext());
        playerAdapter.open();
       
        
        MatchDBAdapter matchAdapter = new MatchDBAdapter(container.getContext());
        matchAdapter.open();
        Cursor cursorMatch = matchAdapter.getMatch(position);
        
        
        int id_organizer = Integer.parseInt(cursorMatch.getString(8));
        Cursor cursorPlayer = playerAdapter.getPlayer(id_organizer);
        
        
        idOrganizer.setText(cursorPlayer.getString(2));
        nameMatch.setText(cursorMatch.getString(3));
        dateMatch.setText(cursorMatch.getString(1));
        timeMatch.setText(cursorMatch.getString(2));
        placeMatch.setText(cursorMatch.getString(5));
        fieldMatch.setText(cursorMatch.getString(4));
        costMatch.setText(cursorMatch.getString(6));
        limitePlayersMatch.setText(cursorMatch.getString(7));
        
        cursorPlayer.close();
        playerAdapter.close();
        cursorMatch.close();
        matchAdapter.close();      
        
        return view;
       
    }
    
}
