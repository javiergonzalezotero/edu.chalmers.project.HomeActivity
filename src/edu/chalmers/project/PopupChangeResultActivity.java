package edu.chalmers.project;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.chalmers.project.data.GoalDBAdapter;
import edu.chalmers.project.data.MatchPlayedDBAdapter;
import edu.chalmers.project.data.PlayerDBAdapter;

public class PopupChangeResultActivity extends Activity {

	private String playerUsernameSelected;
	private int idMatch;
	private int nextGoalOk;
	private String currentUsernameLogin;
	private EditText editTextMinuteGoal;
	private Button presentYes;
	private Button presentNo;
	private TextView enterGoal;

	private Button finishButton;
	private Button nextGoal;
	private TextView present;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_change_result);

		Bundle b = getIntent().getExtras();
		this.idMatch = b.getInt("idMatch");
		this.playerUsernameSelected= b.getString("playerUsernameSelected");
		this.currentUsernameLogin= b.getString("currentUsernameLogin");
		this.nextGoalOk = b.getInt("nextGoal");

		this.setTitle(this.playerUsernameSelected);

		editTextMinuteGoal = (EditText)findViewById(R.id.editTextMinuteGoal);
		presentYes = (Button)findViewById(R.id.buttonPresentYes);
		presentNo = (Button)findViewById(R.id.buttonPresentNo);
		enterGoal = (TextView)findViewById(R.id.textViewEnterGoal);
		nextGoal = (Button)findViewById(R.id.buttonNextGoal);

		finishButton = (Button)findViewById(R.id.buttonFinish);
		present = (TextView)findViewById(R.id.textViewPresent);

		MatchPlayedDBAdapter matchAdapter = new MatchPlayedDBAdapter(this);
		matchAdapter.open();
		Cursor cursorPresent = matchAdapter.getPresent(playerUsernameSelected, idMatch);

		if(this.nextGoalOk == 0){

			enterGoal.setVisibility(View.INVISIBLE);
			editTextMinuteGoal.setVisibility(View.INVISIBLE);
			nextGoal.setVisibility(View.INVISIBLE);

			finishButton.setVisibility(View.INVISIBLE);
		}

		else{
			presentNo.setVisibility(View.INVISIBLE);
			presentYes.setClickable(false);
			enterGoal.setVisibility(View.VISIBLE);
			enterGoal.setText("Next Goal");
			editTextMinuteGoal.setVisibility(View.VISIBLE);
			nextGoal.setVisibility(View.VISIBLE);

			finishButton.setVisibility(View.VISIBLE);	

		}

		if(Integer.parseInt(cursorPresent.getString(1)) == 0){
			presentYes.setVisibility(View.INVISIBLE);
			presentNo.setClickable(false);
			enterGoal.setVisibility(View.INVISIBLE);
			editTextMinuteGoal.setVisibility(View.INVISIBLE);
			nextGoal.setVisibility(View.INVISIBLE);

			finishButton.setVisibility(View.VISIBLE);
		}
		if(Integer.parseInt(cursorPresent.getString(1)) == 1){
			presentNo.setVisibility(View.INVISIBLE);
			presentYes.setClickable(false);
			enterGoal.setVisibility(View.VISIBLE);
			editTextMinuteGoal.setVisibility(View.VISIBLE);
			nextGoal.setVisibility(View.VISIBLE);

			finishButton.setVisibility(View.VISIBLE);
		}
		matchAdapter.close();


	}

	/**
	 * Callback method used when the user taps in next goal button to add a goal.
	 * This method check if the user has actually written some number and if it's not repeated.
	 * If the conditions are OK, the goals are inserted in the database
	 * @param view
	 */
	public void addGoal(View view){
		GoalDBAdapter goalAdapter = new GoalDBAdapter(this);
		goalAdapter.open();

		if((editTextMinuteGoal.getText().toString().compareTo(""))!=0){
			Cursor cursorGoal = goalAdapter.getGoalMinuteUsername(playerUsernameSelected, idMatch, Integer.parseInt(editTextMinuteGoal.getText().toString()));
			if(cursorGoal.getCount()==0){

				if((editTextMinuteGoal.getText().toString().compareTo(""))!=0){

					goalAdapter.insertGoal(playerUsernameSelected, idMatch, Integer.parseInt(editTextMinuteGoal.getText().toString()));
					goalAdapter.close();
					Toast.makeText(this, "Goal correctly added ", Toast.LENGTH_SHORT).show();	
					Intent intent = new Intent(this, PopupChangeResultActivity.class);
					intent.putExtra("nextGoal", 1);	
					intent.putExtra("playerUsernameSelected", playerUsernameSelected);
					intent.putExtra("currentUsernameLogin", currentUsernameLogin);
					intent.putExtra("idMatch", idMatch);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				}
			}
			else{
				Toast.makeText(this, "This Goal is already added", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			goalAdapter.close();
			Toast.makeText(this, "Enter the minute of the goal", Toast.LENGTH_SHORT).show();
		}



	}

	/**
	 * Callback method activated when tapping the Yes button in the "present" section. 
	 * The database is updated as a result
	 * @param view
	 */
	public void isPresent(View view){
		MatchPlayedDBAdapter matchAdapter = new MatchPlayedDBAdapter(this);
		matchAdapter.open();

		presentNo.setVisibility(View.INVISIBLE);
		presentYes.setClickable(false);
		enterGoal.setVisibility(View.VISIBLE);
		editTextMinuteGoal.setVisibility(View.VISIBLE);
		nextGoal.setVisibility(View.VISIBLE);
		finishButton.setVisibility(View.VISIBLE);

		matchAdapter.updatePresent(playerUsernameSelected, idMatch, 1);
		Toast.makeText(this, "Present correctly updated", Toast.LENGTH_SHORT).show();
		matchAdapter.close();

		PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this);
		playerAdapter.open();
		playerAdapter.updateReliability(playerUsernameSelected, 1);
		playerAdapter.close();
	}

	/**
	 * Callback method activated when tapping the No button in the "present" section. 
	 * The database is updated as a result
	 * @param view
	 */
	public void isNotPresent(View view){
		presentYes.setVisibility(View.INVISIBLE);
		presentNo.setClickable(false);

		finishButton.setVisibility(View.VISIBLE);

		MatchPlayedDBAdapter matchAdapter = new MatchPlayedDBAdapter(this);
		matchAdapter.open();
		matchAdapter.updatePresent(playerUsernameSelected, idMatch, 0);
		Toast.makeText(this, "Present correctly updated", Toast.LENGTH_SHORT).show();
		matchAdapter.close();

		PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this);
		playerAdapter.open();
		playerAdapter.updateReliability(playerUsernameSelected, 0);
		playerAdapter.close();
	}

	/**
	 * Callback method activated when tapping the finish button in the popup. Insert the last
	 * goal entered if there exists one in the text view of the minute of the goal
	 * @param view
	 */
	public void finishPopup(View view){
		GoalDBAdapter goalAdapter = new GoalDBAdapter(this);
		goalAdapter.open();

		if((editTextMinuteGoal.getText().toString().compareTo(""))!=0){
			Cursor cursorGoal = goalAdapter.getGoalMinuteUsername(playerUsernameSelected, idMatch, Integer.parseInt(editTextMinuteGoal.getText().toString()));
			if(cursorGoal.getCount()==0){

				if((editTextMinuteGoal.getText().toString().compareTo(""))!=0){

					goalAdapter.insertGoal(playerUsernameSelected, idMatch, Integer.parseInt(editTextMinuteGoal.getText().toString()));
					goalAdapter.close();
					Toast.makeText(this, "Goal correctly added ", Toast.LENGTH_SHORT).show();	
				}
			}
			else{
				Toast.makeText(this, "This Goal is already added", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			goalAdapter.close();
			Toast.makeText(this, "Enter the minute of the goal", Toast.LENGTH_SHORT).show();
		}
		finish();
	}

}
