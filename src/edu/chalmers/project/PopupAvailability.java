package edu.chalmers.project;


import java.util.ArrayList;
import java.util.ListIterator;
import java.util.StringTokenizer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.chalmers.project.data.Availability;
import edu.chalmers.project.data.AvailabilityDBAdapter;

public class PopupAvailability extends Activity {

	private EditText editTextMonday;
	private EditText editTextTuesday;
	private EditText editTextWednesday;
	private EditText editTextThursday;
	private EditText editTextFriday;
	private EditText editTextSaturday;
	private EditText editTextSunday;
	private String username;
	private ArrayList<Availability> timeAvailability;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_availability);

		this.editTextMonday = (EditText)findViewById(R.id.editTextMonday);
		this.editTextTuesday = (EditText)findViewById(R.id.editTextTuesday);
		this.editTextWednesday = (EditText)findViewById(R.id.editTextWednesday);
		this.editTextThursday = (EditText)findViewById(R.id.editTextThursday);
		this.editTextFriday = (EditText)findViewById(R.id.editTextFriday);
		this.editTextSaturday = (EditText)findViewById(R.id.editTextSaturday);
		this.editTextSunday = (EditText)findViewById(R.id.editTextSunday);

		Bundle b = getIntent().getExtras();
		this.username = b.getString("username");


		AvailabilityDBAdapter availabilityAdapter = new AvailabilityDBAdapter(this);
		availabilityAdapter.open();
		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "monday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "";
			while(it.hasNext()){
				if(it.hasNext()){
					stringToShow = stringToShow + it.next() + ",";
				}
				else{
					stringToShow = stringToShow + it.next();
				}
			}
			this.editTextMonday.setText(stringToShow);
		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "tuesday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + ",";
			}
			this.editTextTuesday.setText(stringToShow);
		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "wednesday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + ",";
			}
			this.editTextWednesday.setText(stringToShow);
		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "thursday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + ",";
			}
			this.editTextThursday.setText(stringToShow);
		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "friday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "";
			while(it.hasNext()){

				stringToShow = stringToShow + it.next() + ",";
			}
			this.editTextFriday.setText(stringToShow);
		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "saturday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + ",";
			}
			this.editTextSaturday.setText(stringToShow);
		}

		this.timeAvailability = availabilityAdapter.getAvailabilityBis(username, "sunday");
		if(!(this.timeAvailability.isEmpty())){

			ListIterator it = this.timeAvailability.listIterator();
			String stringToShow = "";
			while(it.hasNext()){
				stringToShow = stringToShow + it.next() + ",";
			}
			this.editTextSunday.setText(stringToShow);
		}




	}

	public void finishPopupAvailability(View v){
		String errorMonday = "";
		String errorTuesday = "";
		String errorWednesday = "";
		String errorThursday = "";
		String errorFriday = "";
		String errorSaturday = "";
		String errorSunday = "";

		errorMonday = verifyAndStoreAvailability(this.editTextMonday.getText().toString(), "monday");
		errorTuesday = verifyAndStoreAvailability(this.editTextTuesday.getText().toString(), "tuesday");
		errorWednesday = verifyAndStoreAvailability(this.editTextWednesday.getText().toString(), "wednesday");
		errorThursday = verifyAndStoreAvailability(this.editTextThursday.getText().toString(), "thursday");
		errorFriday = verifyAndStoreAvailability(this.editTextFriday.getText().toString(), "friday");
		errorSaturday = verifyAndStoreAvailability(this.editTextSaturday.getText().toString(), "saturday");
		errorSunday = verifyAndStoreAvailability(this.editTextSunday.getText().toString(), "sunday");

		finish();
	}
	public String verifyAndStoreAvailability(String daySelected, String day){
		StringTokenizer st = new StringTokenizer(daySelected,",");
		int numberTested;
		int previousNumber = -1;
		int test = 0;
		int error = 0;
		String stringTested;
		while (st.hasMoreTokens() ) {
			stringTested = st.nextToken();
			test = 1;
			StringTokenizer st2 = new StringTokenizer(stringTested,"-");
			while (st2.hasMoreTokens() ) {
				try{
					numberTested = Integer.parseInt(st2.nextToken());

					if(numberTested > previousNumber){

						previousNumber = numberTested;

						if(numberTested < 0){
						}
						else{
							if(numberTested > 24){

								error = 1;
							}
						}
					}
					else{

						error = 1;
					}
				}catch(NumberFormatException e){

					error = 1;
				}
			}
			if(error == 1){
				Toast.makeText(this, "An error has been detected", Toast.LENGTH_SHORT).show();
			}
			else{
				String stringToSave = stringTested;
				AvailabilityDBAdapter availabilityAdapter = new AvailabilityDBAdapter(this);
				availabilityAdapter.open();
				availabilityAdapter.createAvailability(username, day, stringToSave);
				availabilityAdapter.close();
			}
			error = 0;
			previousNumber = -1;
			numberTested = -1;
		}
		return "";
	}
}
