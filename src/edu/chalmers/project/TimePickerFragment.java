package edu.chalmers.project;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;



public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	public EditText activity_edittext;

	public TimePickerFragment(EditText edit_text) {
		activity_edittext = edit_text;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if(hourOfDay <= 9){
			if(minute <= 9){
				activity_edittext.setText("0" + String.valueOf(hourOfDay) + ":0" + String.valueOf(minute));
			}
			else{
				activity_edittext.setText("0" + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
			}
		}
		else{
			if(minute <= 9){
				activity_edittext.setText(String.valueOf(hourOfDay) + ":0" + String.valueOf(minute));
			}
			else{
				activity_edittext.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
			}
		}

	}
}
