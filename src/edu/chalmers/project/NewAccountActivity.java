package edu.chalmers.project;


import edu.chalmers.project.data.PlayerDBAdapter;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewAccountActivity extends FragmentActivity {

	private String username = "";
	private EditText editTextdateOfBirth;
	private EditText editTextUsername;
	private EditText editTextPassword;
	private EditText editTextMail;
	private EditText editTextFirstname;
	private EditText editTextFamilyName;
	private Spinner spinnerFieldPosition;
	private EditText editTextCity;
	private Bundle b;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);
        
        b = getIntent().getExtras();
        username = b.getString("username");
        
        editTextdateOfBirth = (EditText)findViewById(R.id.editTextDateOfBirth);
        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextMail = (EditText)findViewById(R.id.editTextMail);
        editTextFirstname = (EditText)findViewById(R.id.editTextFirstname);
        editTextFamilyName = (EditText)findViewById(R.id.editTextFamilyName);
        spinnerFieldPosition = (Spinner)findViewById(R.id.spinnerFieldPosition);
        editTextCity = (EditText)findViewById(R.id.editTextCity);
        editTextdateOfBirth.setKeyListener(null);    
        
        if (!username.equals("")){//Edit profile
        	showProfile(username);
        }

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
    	Intent intent;
    	switch (item.getItemId()) {
    	case android.R.id.home:
			onBackPressed();
			return true;
    	case R.id.menu_create:
    		if(checkMandatoryFields()){
	    		PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this);
	            playerAdapter.open();
	            if (!username.equals("")){
	            	updatePlayer(playerAdapter);
	            	intent = new Intent(this, HomeActivity.class);
	            	intent.putExtras(b);
	            	intent.putExtra("tab_position", 2);
	            }
	            else{
	            playerAdapter.createPlayer(editTextUsername.getText().toString(), 
		            		editTextPassword.getText().toString(),editTextFirstname.getText().toString(),
		            		editTextFamilyName.getText().toString(),editTextMail.getText().toString(),
		            		50, spinnerFieldPosition.getSelectedItem().toString(), 
		            		editTextCity.getText().toString(), editTextdateOfBirth.getText().toString());
		    		intent = new Intent(this, FirstScreenActivity.class);
		    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
	            }
	            playerAdapter.close();
	            startActivity(intent);	    		
    		}
    		else {
    			Toast.makeText(this, "Fill mandatory fields ", Toast.LENGTH_LONG).show();
			}

    		return true;    
    	default:
    		return super.onOptionsItemSelected(item);
    	}

    }
    
    private boolean checkMandatoryFields(){
    /*	return((editTextUsername.getText().toString().equals("Username*")) 
    			|| (editTextPassword.getText().toString().equals("Password*")));*/
    	return true;
    }
    
    /*
     * Load the information of the player to show in the editProfile screen
     */
    private void showProfile(String username){
    	PlayerDBAdapter adapter = new PlayerDBAdapter(this);
    	adapter.open();
    	Cursor cursor = adapter.getPlayer(username);
    	editTextUsername.setText(username);
    	editTextPassword.setText(cursor.getString(1));
    	editTextFirstname.setText(cursor.getString(2));
    	editTextFamilyName.setText(cursor.getString(3));
    	editTextMail.setText(cursor.getString(4));
    	spinnerFieldPosition.setSelection(spinnerPosition(cursor.getString(6)));
    	editTextCity.setText(cursor.getString(7));
    	editTextdateOfBirth.setText(cursor.getString(8));
    	cursor.close();
    	adapter.close();
    }
    
    /*
     * Get the position in the StringArray of the spinner to show his current position
     */
    private int spinnerPosition(String position){
    	if(position.equals("Goalkeeper")) 
			return 1;
    	else if(position.equals("Defender"))
			return 2;
    	else if(position.equals("Midfielder"))
			return 3;
    	else if(position.equals("Fordward"))
			return 4;
    	else return 0;
    }
    
    private void updatePlayer(PlayerDBAdapter adapter){
    	adapter.updatePlayer(username, 
    			editTextPassword.getText().toString(), editTextFirstname.getText().toString(),
    			editTextFamilyName.getText().toString(), editTextMail.getText().toString(), 
    			spinnerFieldPosition.getSelectedItem().toString(), editTextCity.getText().toString(),
    			editTextdateOfBirth.getText().toString());
    }

    public void showCalendar(View view){
    	Intent intent = new Intent(this, PopupChangeResultActivity.class);
    	startActivity(intent);
    }
    
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this.editTextdateOfBirth);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        
    }
}
