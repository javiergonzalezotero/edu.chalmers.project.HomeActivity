package edu.chalmers.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;

import android.R.integer;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import edu.chalmers.project.data.Availability;
import edu.chalmers.project.data.AvailabilityDBAdapter;

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
	private TextView textViewMonday;
	private LinearLayout content;
	private ArrayList<Availability> timeAvailability;
	
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
    	
    	content = (LinearLayout) view.findViewById(R.id.content);
    	
    	Bundle b = getActivity().getIntent().getExtras();
        String username = b.getString("username");
        String otherUsername = b.getString("other_username");
        if ((otherUsername==null) || (username.equals(otherUsername)))
        	inflatePlayer(username);
        else {
			inflatePlayer(otherUsername);
		}


		this.timeAvailability = new ArrayList<Availability>();
		AvailabilityDBAdapter availabilityAdapter = new AvailabilityDBAdapter(container.getContext());
		availabilityAdapter.open();
		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "monday");
		if(!(this.timeAvailability.isEmpty())){
			
			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Monday : ";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + " | ";
			}
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TextView tv=new TextView(container.getContext());
			tv.setLayoutParams(lparams);
			tv.setText(stringToShow);
			this.content.addView(tv);
			
		}
		
		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "tuesday");
		if(!(this.timeAvailability.isEmpty())){
			
			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Tuesday : ";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + " | ";
			}
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TextView tv=new TextView(container.getContext());
			tv.setLayoutParams(lparams);
			tv.setText(stringToShow);
			this.content.addView(tv);
			
		}
		
		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "wednesday");
		if(!(this.timeAvailability.isEmpty())){
			
			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Wednesday : ";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + " | ";
			}
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TextView tv=new TextView(container.getContext());
			tv.setLayoutParams(lparams);
			tv.setText(stringToShow);
			this.content.addView(tv);
			
		}
		
		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "thursday");
		if(!(this.timeAvailability.isEmpty())){
			
			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Thursday : ";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + " | ";
			}
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TextView tv=new TextView(container.getContext());
			tv.setLayoutParams(lparams);
			tv.setText(stringToShow);
			this.content.addView(tv);
			
		}
		
		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "friday");
		if(!(this.timeAvailability.isEmpty())){
			
			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Friday : ";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + " | ";
			}
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TextView tv=new TextView(container.getContext());
			tv.setLayoutParams(lparams);
			tv.setText(stringToShow);
			this.content.addView(tv);
			
		}
		
		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "saturday");
		if(!(this.timeAvailability.isEmpty())){
			
			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Saturday : ";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + " | ";
			}
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TextView tv=new TextView(container.getContext());
			tv.setLayoutParams(lparams);
			tv.setText(stringToShow);
			this.content.addView(tv);
			
		}
		
		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "sunday");
		if(!(this.timeAvailability.isEmpty())){
			
			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Sunday : ";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + " | ";
			}
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TextView tv=new TextView(container.getContext());
			tv.setLayoutParams(lparams);
			tv.setText(stringToShow);
			this.content.addView(tv);
			
		}
		availabilityAdapter.close();



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
        	int height = profilePhoto.getLayoutParams().height;
        	int width = profilePhoto.getLayoutParams().width;
        	profilePhoto.setImageBitmap(ImageLoader.decodeSampledBitmapFromResource(cursor.getString(10), 
					width, height));
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
        if ((otherUsername==null) || (username.equals(otherUsername)))
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
