package edu.chalmers.project;


import edu.chalmers.project.data.PlayerDBAdapter;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewAccountActivity extends FragmentActivity {

	private EditText dateOfBirth;
	private EditText editTextUsername;
	private EditText editTextPassword;
	private EditText editTextMail;
	private EditText editTextFirstname;
	private EditText editTextFamilyName;
	private EditText editTextFieldPosition;
	private EditText editTextDateOfBirth;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);
        dateOfBirth = (EditText)findViewById(R.id.editTextDateOfBirth);
        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextMail = (EditText)findViewById(R.id.editTextMail);
        editTextFirstname = (EditText)findViewById(R.id.editTextFirstname);
        editTextFamilyName = (EditText)findViewById(R.id.editTextFamilyName);
        editTextFieldPosition = (EditText)findViewById(R.id.editTextFieldPosition);
        dateOfBirth.setKeyListener(null);       
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
    		PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this);
            playerAdapter.open();
            playerAdapter.createPlayer(editTextUsername.getText().toString(), 
            		editTextPassword.getText().toString(),editTextFirstname.getText().toString(),
            		editTextFamilyName.getText().toString(),"mail",50, editTextFieldPosition.getText().toString(), 
            		"Goteborg", dateOfBirth.getText().toString());
            Toast.makeText(this, playerAdapter.getPlayer(1).getString(3), Toast.LENGTH_LONG).show();
    		Intent intent = new Intent(this, FirstScreenActivity.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
    		return true;    
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

    public void showCalendar(View view){
    	Intent intent = new Intent(this, PopupCalendarActivity.class);
    	startActivity(intent);
    }
    
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this.dateOfBirth);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        
    }
}
