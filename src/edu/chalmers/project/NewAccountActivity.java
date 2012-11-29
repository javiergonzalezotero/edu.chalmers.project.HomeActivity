package edu.chalmers.project;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class NewAccountActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);
    }

    /**
     * This method is called when the user press the Create button
     * @param view
     */
    public void confirmAccount(View view){
    	Intent intent = new Intent(this, LoginActivity.class);
    	startActivity(intent);
    }
}
