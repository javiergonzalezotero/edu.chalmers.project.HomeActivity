package edu.chalmers.project;


import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PopupCalendarActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_calendar_date_of_birth);
		
		DatePicker dateOfBirth = (DatePicker)findViewById(R.id.datePickerDateOfBirth);
		int dayOfBirth = dateOfBirth.getDayOfMonth();
		int monthOfBirth = dateOfBirth.getMonth();
		int yearOfBirth = dateOfBirth.getYear();
		
	}
	
	public void confirmDateOfBirth(View view){
    	finish();
    }
	
}