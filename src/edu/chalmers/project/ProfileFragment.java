package edu.chalmers.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
	private String imagePath;
	private LinearLayout linearLayoutAvailability;
	private String username;
	private String otherUsername;
	private Button buttonShowAvailability;
	private int buttonPressed;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;
	private TextView tv6;
	private TextView tv7;
	private TextView tvSpace;

	private ArrayList<Availability> timeAvailability;
	private ImageView arrowReliability;
	
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
    	matchPlayedTextView = (TextView) view.findViewById(R.id.textViewMatches1);
    	goalsScoredTextView = (TextView) view.findViewById(R.id.textViewGoals);
    	positionTextView = (TextView) view.findViewById(R.id.textViewPositionProfile);
    	arrowReliability = (ImageView) view.findViewById(R.id.imageViewReliability);
    	profilePhoto = (ImageView) view.findViewById(R.id.imageViewProfile);
    	linearLayoutAvailability = (LinearLayout) view.findViewById(R.id.linearLayoutAvailability);
    	buttonShowAvailability = (Button) view.findViewById(R.id.buttonShowAvailability);
    	
    	this.buttonPressed = 0;


		Bundle b = getActivity().getIntent().getExtras();
		this.username = b.getString("username");
		this.otherUsername = b.getString("other_username");
		if ((otherUsername==null) || (username.equals(otherUsername)))
			inflatePlayer(username);
		else {
			inflatePlayer(otherUsername);
		}


		buttonShowAvailability.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(buttonPressed == 0){

					showAvailability(v);
					buttonPressed=1;
					buttonShowAvailability.setText("Hide Availability");
				}
				else{
					linearLayoutAvailability.removeView(tv1);
					linearLayoutAvailability.removeView(tv2);
					linearLayoutAvailability.removeView(tv3);
					linearLayoutAvailability.removeView(tv4);
					linearLayoutAvailability.removeView(tv5);
					linearLayoutAvailability.removeView(tv6);
					linearLayoutAvailability.removeView(tv7);
					linearLayoutAvailability.removeView(tvSpace);
					buttonPressed=0;
					buttonShowAvailability.setText("Show Availability");

				}
			}
		});



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
        arrowReliability.setImageResource(selectArrow(cursor.getString(5)));
        if(!cursor.getString(10).equals("")){
        	int height = profilePhoto.getLayoutParams().height;
        	int width = profilePhoto.getLayoutParams().width;
        	profilePhoto.setImageBitmap(ImageLoader.decodeSampledBitmapFromResource(cursor.getString(10), width, height));

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
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

	
	public int selectArrow(String reliability){
		int level = Integer.parseInt(reliability);
		if (level<21) {
			return getActivity().getResources().getIdentifier("black", "drawable", getActivity().getPackageName());
		}
		else if (level<41) {
			return getActivity().getResources().getIdentifier("blue", "drawable", getActivity().getPackageName());
		}
		else if (level<61) {
			return getActivity().getResources().getIdentifier("green", "drawable", getActivity().getPackageName());
		}
		else if (level<81){
			return getActivity().getResources().getIdentifier("yellow", "drawable", getActivity().getPackageName());
		}
		else{
			return getActivity().getResources().getIdentifier("red", "drawable", getActivity().getPackageName());
		}
	}
	

	public void showAvailability(View view){
		this.timeAvailability = new ArrayList<Availability>();
		AvailabilityDBAdapter availabilityAdapter = new AvailabilityDBAdapter(view.getContext());
		availabilityAdapter.open();

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "monday");

		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Monday : ";
			while(it.hasNext()){
				String currentString = "" + it.next();
				if(it.hasNext()){
					stringToShow = stringToShow + currentString + " | ";
				}
				else{
					stringToShow = stringToShow + currentString;
				}

			}
			LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tv1=new TextView(view.getContext());
			tv1.setLayoutParams(lparams);
			tv1.setText(stringToShow);
			this.linearLayoutAvailability.addView(tv1);
		}



		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "tuesday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Tuesday : ";
			while(it.hasNext()){
				String currentString = "" + it.next();
				if(it.hasNext()){
					stringToShow = stringToShow + currentString + " | ";
				}
				else{
					stringToShow = stringToShow + currentString;
				}

			}

			if(!(stringToShow.equals("Tuesday : "))){
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv2=new TextView(view.getContext());
				tv2.setLayoutParams(lparams);
				tv2.setText(stringToShow);
				this.linearLayoutAvailability.addView(tv2);
			}

		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "wednesday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Wednesday : ";
			while(it.hasNext()){
				String currentString = "" + it.next();
				if(it.hasNext()){
					stringToShow = stringToShow + currentString + " | ";
				}
				else{
					stringToShow = stringToShow + currentString;
				}

			}

			if(!(stringToShow.equals("Wednesday : "))){
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv3=new TextView(view.getContext());
				tv3.setLayoutParams(lparams);
				tv3.setText(stringToShow);
				this.linearLayoutAvailability.addView(tv3);
			}

		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "thursday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Thursday : ";
			while(it.hasNext()){
				String currentString = "" + it.next();
				if(it.hasNext()){
					stringToShow = stringToShow + currentString + " | ";
				}
				else{
					stringToShow = stringToShow + currentString;
				}

			}

			if(!(stringToShow.equals("Thurday : "))){
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv4=new TextView(view.getContext());
				tv4.setLayoutParams(lparams);
				tv4.setText(stringToShow);
				this.linearLayoutAvailability.addView(tv4);
			}

		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "friday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Friday : ";
			while(it.hasNext()){
				String currentString = "" + it.next();
				if(it.hasNext()){
					stringToShow = stringToShow + currentString + " | ";
				}
				else{
					stringToShow = stringToShow + currentString;
				}

			}

			if(!(stringToShow.equals("Friday : "))){
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv5=new TextView(view.getContext());
				tv5.setLayoutParams(lparams);
				tv5.setText(stringToShow);
				this.linearLayoutAvailability.addView(tv5);
			}

		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "saturday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Saturday : ";
			while(it.hasNext()){
				String currentString = "" + it.next();
				if(it.hasNext()){
					stringToShow = stringToShow + currentString + " | ";
				}
				else{
					stringToShow = stringToShow + currentString;
				}

			}

			if(!(stringToShow.equals("Saturday : "))){
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv6=new TextView(view.getContext());
				tv6.setLayoutParams(lparams);
				tv6.setText(stringToShow);
				this.linearLayoutAvailability.addView(tv6);
			}

		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "sunday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "Sunday : ";
			while(it.hasNext()){
				String currentString = "" + it.next();
				if(it.hasNext()){
					stringToShow = stringToShow + currentString + " | ";
				}
				else{
					stringToShow = stringToShow + currentString;
				}

			}

			if(!(stringToShow.equals("Sunday : "))){
				LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tv7=new TextView(view.getContext());
				tv7.setLayoutParams(lparams);
				tv7.setText(stringToShow);
				this.linearLayoutAvailability.addView(tv7);
			}

		}


		availabilityAdapter.close();

		LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tvSpace=new TextView(view.getContext());
		tvSpace.setLayoutParams(lparams);
		tvSpace.setText("");
		this.linearLayoutAvailability.addView(tvSpace);
	}

}
