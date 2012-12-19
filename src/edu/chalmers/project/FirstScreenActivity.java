package edu.chalmers.project;

import edu.chalmers.project.data.DBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FirstScreenActivity extends Activity {

	private DBAdapter dbAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_screen);
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		dbAdapter.close();
	}


	/**
	 *  Called when the user press login button. It starts the login activity
	 * @param view
	 */
	public void login(View view){
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	/**
	 * Called when the user press newAccount button. It starts the new account activity
	 * indicating that there isn't a value for the username yet.
	 * @param view
	 */
	public void createAccount(View view){
		Intent intent = new Intent(this, NewAccountActivity.class);
		intent.putExtra("username", "");
		startActivity(intent);
	}


}
