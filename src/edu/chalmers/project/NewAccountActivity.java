package edu.chalmers.project;


import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import edu.chalmers.project.data.AvailabilityDBAdapter;
import edu.chalmers.project.data.PlayerDBAdapter;

public class NewAccountActivity extends FragmentActivity {

	private static int RESULT_LOAD_IMAGE = 1;
	private String selectedImagePath="";
	
	private String username = "";
	private EditText editTextdateOfBirth;
	private EditText editTextUsername;
	private EditText editTextPassword;
	private EditText editTextMail;
	private EditText editTextFirstname;
	private EditText editTextFamilyName;
	private Spinner spinnerFieldPosition;
	private ImageButton profilePhoto;
	private EditText editTextCity;
	private Bundle b;
	private Button buttonChangeAvailability;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);
        
        b = getIntent().getExtras();
        username = b.getString("username");
        
        editTextdateOfBirth = (EditText)findViewById(R.id.editTextDateOfBirth);
        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextMail = (EditText)findViewById(R.id.editTextMail);
        editTextFirstname = (EditText)findViewById(R.id.editTextFirstname);
        editTextFamilyName = (EditText)findViewById(R.id.editTextFamilyName);
        spinnerFieldPosition = (Spinner)findViewById(R.id.spinnerFieldPosition);
        editTextCity = (EditText)findViewById(R.id.editTextCity);
        profilePhoto = (ImageButton) findViewById(R.id.imageButtonSelectPhoto);
        buttonChangeAvailability = (Button) findViewById(R.id.buttonChangeAvailability);
        editTextdateOfBirth.setKeyListener(null);    
        
        if (!username.equals("")){//Edit profile
        	showProfile(username);
        	this.setTitle("Edit profile");
        }
        else{
        	buttonChangeAvailability.setVisibility(View.INVISIBLE);
        }

    }
    
    public void selectPhoto(View view){
    	Intent i = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);

    	if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
    		Uri selectedImage = data.getData();
    		String[] filePathColumn = { MediaStore.Images.Media.DATA };

    		Cursor cursor = getContentResolver().query(selectedImage,
    				filePathColumn, null, null, null);
    		cursor.moveToFirst();

    		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
    		selectedImagePath = cursor.getString(columnIndex);
    		profilePhoto.setImageBitmap(ImageLoader.decodeSampledBitmapFromResource(selectedImagePath, 
					profilePhoto.getLayoutParams().width,profilePhoto.getLayoutParams().height));
    		cursor.close();
    	}
    	
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
    	Intent intent;
    	switch (item.getItemId()) {
    	case android.R.id.home:
			onBackPressed();
			return true;
    	case R.id.menu_create:
    		if(!checkMandatoryFields()){
	    		PlayerDBAdapter playerAdapter = new PlayerDBAdapter(this);
	            playerAdapter.open();
	            if (!username.equals("")){
	            	updatePlayer(playerAdapter);
	            	intent = new Intent(this, HomeActivity.class);
	            	intent.putExtras(b);
	            	intent.putExtra("tab_position", 2);
	            }
	            else{
	            	if(playerAdapter.usernameExists(editTextUsername.getText().toString())){
		            	playerAdapter.createPlayer(editTextUsername.getText().toString(), 
			            		editTextPassword.getText().toString(),editTextFirstname.getText().toString(),
			            		editTextFamilyName.getText().toString(),editTextMail.getText().toString(),
			            		50, spinnerFieldPosition.getSelectedItem().toString(), 
			            		editTextCity.getText().toString(), editTextdateOfBirth.getText().toString(),
			            		selectedImagePath);
			    		intent = new Intent(this, HomeActivity.class);
			        	intent.putExtra("username", editTextUsername.getText().toString());
			        	intent.putExtra("tab_position", 0);
	            	}
	            	else{
	            		Toast toast = Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT);
	    				toast.setGravity(Gravity.CENTER, 0, 0);
	    				toast.show();
	    				return true;
	            	}
	            }
	            playerAdapter.close();
	            
	            /* Initialize the availability data at null */
	            /*AvailabilityDBAdapter availabilityAdapter = new AvailabilityDBAdapter(this);
				availabilityAdapter.open();
				availabilityAdapter.createAvailability(editTextUsername.getText().toString(), "monday", "");
				availabilityAdapter.createAvailability(editTextUsername.getText().toString(), "tuesday", "");
				availabilityAdapter.createAvailability(editTextUsername.getText().toString(), "wednesday", "");
				availabilityAdapter.createAvailability(editTextUsername.getText().toString(), "thursday", "");
				availabilityAdapter.createAvailability(editTextUsername.getText().toString(), "friday", "");
				availabilityAdapter.createAvailability(editTextUsername.getText().toString(), "saturday", "");
				availabilityAdapter.createAvailability(editTextUsername.getText().toString(), "sunday", "");
				availabilityAdapter.close();
				*/
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);	    		
    		}
    		else {
    			Toast toast = Toast.makeText(this, "Fill mandatory fields", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

    		return true;    
    	default:
    		return super.onOptionsItemSelected(item);
    	}

    }
    
    private boolean checkMandatoryFields(){
    	return((editTextUsername.getText().toString().equals("")) 
    			|| (editTextPassword.getText().toString().equals("")));
    }
    
    /*
     * Load the information of the player to show in the editProfile screen
     */
    private void showProfile(String username){
    	PlayerDBAdapter adapter = new PlayerDBAdapter(this);
    	adapter.open();
    	Cursor cursor = adapter.getPlayer(username);
    	editTextUsername.setText(username);
    	editTextPassword.setText(cursor.getString(1));
    	editTextFirstname.setText(cursor.getString(2));
    	editTextFamilyName.setText(cursor.getString(3));
    	editTextMail.setText(cursor.getString(4));
    	spinnerFieldPosition.setSelection(spinnerPosition(cursor.getString(6)));
    	editTextCity.setText(cursor.getString(7));
    	editTextdateOfBirth.setText(cursor.getString(8));
    	if(!cursor.getString(10).equals("")){
    		selectedImagePath = cursor.getString(10);
    		profilePhoto.setImageBitmap(ImageLoader.decodeSampledBitmapFromResource(cursor.getString(10), 
    				profilePhoto.getLayoutParams().width,profilePhoto.getLayoutParams().height));
    	}
    	cursor.close();
    	adapter.close();
    }
    
    
    
    
    /*
     * Get the position in the StringArray of the spinner to show his current position
     */
    private int spinnerPosition(String position){
    	if(position.equals("Goalkeeper")) 
			return 1;
    	else if(position.equals("Defender"))
			return 2;
    	else if(position.equals("Midfielder"))
			return 3;
    	else if(position.equals("Fordward"))
			return 4;
    	else return 0;
    }
    
    private void updatePlayer(PlayerDBAdapter adapter){
    	adapter.updatePlayer(editTextUsername.getText().toString(), 
    			editTextPassword.getText().toString(), editTextFirstname.getText().toString(),
    			editTextFamilyName.getText().toString(), editTextMail.getText().toString(), 
    			spinnerFieldPosition.getSelectedItem().toString(), editTextCity.getText().toString(),
    			editTextdateOfBirth.getText().toString(), selectedImagePath);
    }

    public void showCalendar(View view){
    	Intent intent = new Intent(this, PopupChangeResultActivity.class);
    	startActivity(intent);
    }
    
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this.editTextdateOfBirth);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        
    }
    
    public void changeAvailability(View v){
    	Intent intent = new Intent(this, PopupAvailability.class);
    	intent.putExtra("username", username);
    	startActivity(intent);
    }
    
   
}
