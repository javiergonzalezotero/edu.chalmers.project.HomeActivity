package edu.chalmers.project;

import edu.chalmers.project.data.PlayerDBAdapter;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileFragment extends Fragment {

	private TextView usernameTextView;
	private TextView ageTextView;
	private TextView reliabilityLevelTextView;
	private TextView MVPNumberTextView;
	private TextView matchPlayedTextView;
	private TextView goalsScoredTextView;
	private TextView positionTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.profile_fragment, container, false);
    	view.setClickable(true);
    	
    	usernameTextView = (TextView) view.findViewById(R.id.textViewUsernameProfile);
    	ageTextView = (TextView) view.findViewById(R.id.textViewAgeProfile);
    	reliabilityLevelTextView = (TextView) view.findViewById(R.id.textViewReliabilityLevelProfile);
    	MVPNumberTextView = (TextView) view.findViewById(R.id.textViewMVPNumberProfile);
    	matchPlayedTextView = (TextView) view.findViewById(R.id.textViewMatches);
    	goalsScoredTextView = (TextView) view.findViewById(R.id.textViewGoals);
    	positionTextView = (TextView) view.findViewById(R.id.textViewPositionProfile);
    	Bundle b = getActivity().getIntent().getExtras();
        String username = b.getString("username");
        inflatePlayer(username);
        Toast.makeText(this.getActivity(), username, Toast.LENGTH_LONG).show();
        return view;
       
    }
    
    private void inflatePlayer(String username){
    	PlayerDBAdapter adapter = new PlayerDBAdapter(this.getActivity());
    	adapter.open();
        Cursor cursor = adapter.getPlayer(username);
        usernameTextView.setText(cursor.getString(0));
        positionTextView.setText(cursor.getString(6));
        adapter.close();
    }
    
}
