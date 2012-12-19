package edu.chalmers.project;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.chalmers.project.data.FriendDBAdapter;
import edu.chalmers.project.data.Player;

public class FriendsActivity extends Activity{

	private ArrayList<Player> friendList= new ArrayList<Player>();
	private Bundle bundle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.friends_list); 
		bundle = this.getIntent().getExtras();
		String username = bundle.getString("username");
		String otherUsername = bundle.getString("other_username");
		FriendDBAdapter friendAdapter = new FriendDBAdapter(this);
		friendAdapter.open();

		if (otherUsername==null) // My friends
			this.friendList = friendAdapter.getFriendList(username);
		else { // Friends of another player
			this.friendList = friendAdapter.getFriendList(otherUsername);
			this.setTitle("Friends of " + otherUsername);
		}
		friendAdapter.close();

		ListView lvList = (ListView) findViewById(R.id.listViewFriendsList);
		lvList.setAdapter(new PlayerListAdapter(this, this.friendList, R.layout.big_player_list_item));
		lvList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				String otherUsername = friendList.get(position).getUsername();
				String username = bundle.getString("username");
				Intent intent = new Intent(view.getContext(), HomeActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("other_username", otherUsername);
				intent.putExtra("tab_position", 2); //When selected a friend, starting in the profile fragment
				startActivity(intent);
			}
		});        
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
