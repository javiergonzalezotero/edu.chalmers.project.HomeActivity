package edu.chalmers.project;

import edu.chalmers.project.data.PlayerDBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
    
    public void confirmLogin(View view){
    	PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this);
        playerAdapter.open();
        Cursor cursor = playerAdapter.getPassword(usernameEditText.getText().toString());
        if (cursor.getString(0).equals(passworEditText.getText().toString())){
        	Toast.makeText(this, "Match", Toast.LENGTH_LONG).show();
        }
        else {
			Toast.makeText(this, "NO MATCH ", Toast.LENGTH_LONG).show();
		}
    	Intent intent = new Intent(this, HomeActivity.class);
    	startActivity(intent);
    }
}
