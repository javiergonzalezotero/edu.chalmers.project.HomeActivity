package edu.chalmers.project;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.chalmers.project.data.GoalDBAdapter;

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

		editTextMinuteGoal = (EditText)findViewById(R.id.editTextMinuteGoal);
		presentYes = (Button)findViewById(R.id.buttonPresentYes);
		presentNo = (Button)findViewById(R.id.buttonPresentNo);
		enterGoal = (TextView)findViewById(R.id.textViewEnterGoal);
		nextGoal = (Button)findViewById(R.id.buttonNextGoal);
		
		finishButton = (Button)findViewById(R.id.buttonFinish);
		present = (TextView)findViewById(R.id.textViewPresent);

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

	}

	public void addGoal(View view){
		if((editTextMinuteGoal.getText().toString().compareTo(""))!=0){
			GoalDBAdapter goalAdapter = new GoalDBAdapter(this);
			goalAdapter.open();
			goalAdapter.insertGoal(playerUsernameSelected, idMatch, Integer.parseInt(editTextMinuteGoal.getText().toString()));
			Toast.makeText(this, "Goal correctly added", Toast.LENGTH_SHORT).show();	
			Intent intent = new Intent(this, PopupChangeResultActivity.class);
			intent.putExtra("nextGoal", 1);	
			intent.putExtra("playerUsernameSelected", playerUsernameSelected);
			intent.putExtra("currentUsernameLogin", currentUsernameLogin);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
		else 
			Toast.makeText(this, "Enter the minute of the goal", Toast.LENGTH_SHORT).show();

	}

	public void isPresent(View view){
		presentNo.setVisibility(View.INVISIBLE);
		presentYes.setClickable(false);
		enterGoal.setVisibility(View.VISIBLE);
		editTextMinuteGoal.setVisibility(View.VISIBLE);
		nextGoal.setVisibility(View.VISIBLE);
		
		finishButton.setVisibility(View.VISIBLE);
	}

	public void isNotPresent(View view){
		presentYes.setVisibility(View.INVISIBLE);
		presentNo.setClickable(false);
		
		finishButton.setVisibility(View.VISIBLE);
	}

	public void finishPopup(View view){
		if((editTextMinuteGoal.getText().toString().compareTo(""))!=0){
			GoalDBAdapter goalAdapter = new GoalDBAdapter(this);
			goalAdapter.open();
			goalAdapter.insertGoal(playerUsernameSelected, idMatch, Integer.parseInt(editTextMinuteGoal.getText().toString()));
			Toast.makeText(this, "Goal correctly added", Toast.LENGTH_SHORT).show();
		}
		finish();
	}

}