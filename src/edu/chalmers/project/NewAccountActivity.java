package edu.chalmers.project;


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

	EditText dateOfBirth;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);
        
        dateOfBirth = (EditText)findViewById(R.id.editTextDateOfBirth);
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
    		Intent intent = new Intent(this, FirstScreenActivity.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
    		return true;    
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

    /**
     * This method is called when the user press the Create button
     * @param view
     */
    public void confirmAccount(View view){
    	Intent intent = new Intent(this, LoginActivity.class);
    	startActivity(intent);
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
