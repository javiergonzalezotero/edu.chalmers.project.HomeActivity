package edu.chalmers.project;


import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.chalmers.project.data.MatchDBAdapter;
import edu.chalmers.project.data.MatchPlayedDBAdapter;
import edu.chalmers.project.data.PlayerDBAdapter;

public class CreateEventActivity extends FragmentActivity {

	private EditText editTextNameEvent;
	private EditText editTextPlace;
	private EditText editTextCost;
	private EditText editTextPlayersLimit;
	private EditText editTextField;
	private EditText editTextOrganizer;
	private EditText editTextTime;
	private EditText editTextDate;
	private Bundle b;
	private String username;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event);

		this.b = getIntent().getExtras();
		this.username = this.b.getString("username");

		editTextNameEvent = (EditText)findViewById(R.id.editTextNameEvent);
		editTextPlace = (EditText)findViewById(R.id.editTextPlace);
		editTextField = (EditText)findViewById(R.id.editTextField);
		editTextCost = (EditText)findViewById(R.id.editTextCost);
		editTextPlayersLimit = (EditText)findViewById(R.id.editTextPlayersLimit);
		editTextDate = (EditText)findViewById(R.id.editTextDate);
		editTextTime = (EditText)findViewById(R.id.editTextTime);
		editTextNameEvent.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_event_list, menu);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;

		case R.id.menu_create:

			MatchDBAdapter matchAdapter = new MatchDBAdapter(this);
			matchAdapter.open();
			MatchPlayedDBAdapter adapter = new MatchPlayedDBAdapter(this);
			adapter.open();
			PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this);
			playerAdapter.open();
			Cursor cursorPlayer = playerAdapter.getPlayer(this.username);

			if(matchAdapter.matchNameExists(editTextNameEvent.getText().toString())){
				if((editTextNameEvent.getText().toString().compareTo("")!=0) && (editTextDate.getText().toString().compareTo("")!=0)
						&& (editTextTime.getText().toString().compareTo("")!=0) && (editTextField.getText().toString().compareTo("")!=0) 
						&& (editTextPlace.getText().toString().compareTo("")!=0) && (editTextCost.getText().toString().compareTo("")!=0)
						&& (editTextPlayersLimit.getText().toString().compareTo("")!=0)){
					if((Integer.parseInt(editTextPlayersLimit.getText().toString()) % 2) == 0){
						long rowId= matchAdapter.createMatch(editTextDate.getText().toString(), editTextTime.getText().toString(), 
								editTextNameEvent.getText().toString(), editTextField.getText().toString(),
								editTextPlace.getText().toString(), Integer.parseInt(editTextCost.getText().toString()), 
								Integer.parseInt(editTextPlayersLimit.getText().toString()),
								Integer.parseInt(cursorPlayer.getString(9)));
						adapter.joinMatch(b.getString("username"), rowId, 1);//Join host team automatically

						adapter.close();
						matchAdapter.close();
						cursorPlayer.close();
						playerAdapter.close();

						Intent intent = new Intent(this, HomeActivity.class);
						intent.putExtra("tab_position", 0);
						intent.putExtras(b);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					else{
						Toast.makeText(this, "You have to enter an even number", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(this, "You have to enter all the fields", Toast.LENGTH_LONG).show();
				}
			}
			else{
				Toast.makeText(this, "Name of the match already exists", Toast.LENGTH_LONG).show();

			}

			return true;    
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment(this.editTextDate);
		newFragment.show(getSupportFragmentManager(), "datePicker");  
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment(this.editTextTime);
		newFragment.show(getSupportFragmentManager(), "timePicker");  
	}

}
