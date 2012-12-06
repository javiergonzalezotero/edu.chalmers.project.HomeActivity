package edu.chalmers.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.chalmers.project.data.PlayerDBAdapter;


public class ProfileFragment extends Fragment {

	private TextView usernameTextView;
	private TextView firstNameTextView;
	private TextView familyNameTextView;
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
    	this.setHasOptionsMenu(true);//for the split action bar
    	
    	usernameTextView = (TextView) view.findViewById(R.id.textViewUsernameProfile);
    	firstNameTextView = (TextView) view.findViewById(R.id.textViewFirstName);
    	familyNameTextView = (TextView) view.findViewById(R.id.textViewFamilyName);
    	ageTextView = (TextView) view.findViewById(R.id.textViewAgeProfile);
    	reliabilityLevelTextView = (TextView) view.findViewById(R.id.textViewReliabilityLevelProfile);
    	MVPNumberTextView = (TextView) view.findViewById(R.id.textViewMVPNumberProfile);
    	matchPlayedTextView = (TextView) view.findViewById(R.id.textViewMatches);
    	goalsScoredTextView = (TextView) view.findViewById(R.id.textViewGoals);
    	positionTextView = (TextView) view.findViewById(R.id.textViewPositionProfile);
    	
    	Bundle b = getActivity().getIntent().getExtras();
        String username = b.getString("username");
        inflatePlayer(username);
        return view;
       
    }
    
    /*
     * Loads the information of the player from the db
     */
    private void inflatePlayer(String username){
    	PlayerDBAdapter adapter = new PlayerDBAdapter(this.getActivity());
    	adapter.open();
        Cursor cursor = adapter.getPlayer(username);
        usernameTextView.setText(cursor.getString(0));
        firstNameTextView.setText(cursor.getString(2));
        familyNameTextView.setText(cursor.getString(3));
        ageTextView.setText(calculateAge(cursor.getString(8)));
        reliabilityLevelTextView.setText(cursor.getString(5));
        positionTextView.setText(cursor.getString(6));
        cursor.close();
        adapter.close();
    }
    /*
     * Calculate the age of the player from his birthdate
     */
    private String calculateAge(String birthdate){
    	Calendar cal = Calendar.getInstance();
    	Calendar current = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
        try {
			cal.setTime(sdf.parse(birthdate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long ageInMillis = current.getTimeInMillis() - cal.getTimeInMillis();
		cal.setTimeInMillis(ageInMillis);
		int age = cal.get(Calendar.YEAR)-1970;
		if(age==0)
			return "N/D years";
    	return Integer.toString(age) + " years";
    }
    
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
    	inflater.inflate(R.menu.profile_fragment, menu);
    	
    }
    
   
}
