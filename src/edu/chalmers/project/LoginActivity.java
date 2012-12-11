package edu.chalmers.project;

import edu.chalmers.project.data.PlayerDBAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText usernameEditText;
	private EditText passworEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText)findViewById(R.id.editTextUsernameLogin);
        passworEditText = (EditText)findViewById(R.id.editTextPasswordLogin);
    }
    
    public void confirmLogin(View view){
    	PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this);
        playerAdapter.open();
        Cursor cursor;
        cursor = playerAdapter.getPlayer(usernameEditText.getText().toString());
        if (cursor.getCount()==0){
        	Toast.makeText(this, "Username incorrect ", Toast.LENGTH_LONG).show();
        	return;
        }
        if (cursor.getString(1).equals(passworEditText.getText().toString())){
        	Intent intent = new Intent(this, HomeActivity.class);
        	intent.putExtra("username", cursor.getString(0));
        	intent.putExtra("tab_position", 0);
        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); 
    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(intent);
        }
        else {
			Toast.makeText(this, "Password incorrect ", Toast.LENGTH_LONG).show();
		}
        cursor.close();
        playerAdapter.close();
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
