package edu.chalmers.project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.chalmers.project.data.PlayerDBAdapter;

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
        Cursor cursor;
        cursor = playerAdapter.getPlayer(usernameEditText.getText().toString());
        if (cursor.getCount()==0){
        	Toast.makeText(this, "Username incorrect ", Toast.LENGTH_LONG).show();
        	return;
        }
        if (cursor.getString(1).equals(passworEditText.getText().toString())){
        	Intent intent = new Intent(this, HomeActivity.class);
        	intent.putExtra("username", cursor.getString(0));
        	startActivity(intent);
        }
        else {
			Toast.makeText(this, "Password incorrect ", Toast.LENGTH_LONG).show();
		}
        playerAdapter.close();
    }
}
