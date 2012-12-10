package edu.chalmers.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.chalmers.project.data.FriendDBAdapter;
import edu.chalmers.project.data.GoalDBAdapter;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.MatchPlayedDBAdapter;
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
	private ImageView profilePhoto;
	private Bitmap bmp;
	String imagePath;
	
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
    	profilePhoto = (ImageView) view.findViewById(R.id.imageViewProfile);
    	
    	Bundle b = getActivity().getIntent().getExtras();
        String username = b.getString("username");
        String otherUsername = b.getString("other_username");
        if (otherUsername==null)
        	inflatePlayer(username);
        else {
			inflatePlayer(otherUsername);
		}
        return view;
       
    }
    
    /*
     * Loads the information of the player from the db
     */
    private void inflatePlayer(String username){
    	PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this.getActivity());
    	playerAdapter.open();
    	MatchPlayedDBAdapter adapter = new MatchPlayedDBAdapter(this.getActivity());
    	adapter.open();
    	MatchDBAdapter matchAdapter = new MatchDBAdapter(this.getActivity());
    	matchAdapter.open();
    	GoalDBAdapter goalAdapter = new GoalDBAdapter(this.getActivity());
    	goalAdapter.open();
        Cursor cursor = playerAdapter.getPlayer(username);
        usernameTextView.setText(cursor.getString(0));
        firstNameTextView.setText(cursor.getString(2));
        familyNameTextView.setText(cursor.getString(3));
        matchPlayedTextView.setText(adapter.getMatchesPlayed(username));
        MVPNumberTextView.setText(matchAdapter.getNumberMVPs(cursor.getInt(9)));
        goalsScoredTextView.setText(goalAdapter.getGoalsScored(username));
        ageTextView.setText(calculateAge(cursor.getString(8)));
        reliabilityLevelTextView.setText(cursor.getString(5));
        positionTextView.setText(cursor.getString(6));
        if(!cursor.getString(10).equals("")){
	        profilePhoto.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(10)));
	        profilePhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        cursor.close();
        playerAdapter.close();
        adapter.close();
        matchAdapter.close();
        goalAdapter.close();
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
    	Bundle b = getActivity().getIntent().getExtras();
        String otherUsername = b.getString("other_username");
        String username = b.getString("username");
        if (otherUsername==null)
        	inflater.inflate(R.menu.profile_fragment, menu);
        else if (isMyFriend(username, otherUsername)){
			inflater.inflate(R.menu.friend_profile, menu);
		} 	
        else{
        	inflater.inflate(R.menu.other_profile_fragment, menu);
        }
    	
    }
    
    public boolean isMyFriend(String username, String otherUsername){
    	FriendDBAdapter adapter = new FriendDBAdapter(this.getActivity());
    	adapter.open();
    	Boolean b = adapter.isFriend(username, otherUsername);
    	adapter.close();
    	return b;
    }
   
}
