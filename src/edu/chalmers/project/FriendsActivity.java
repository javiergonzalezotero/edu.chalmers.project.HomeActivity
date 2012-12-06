package edu.chalmers.project;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
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
   
        if (otherUsername==null)
        	this.friendList = friendAdapter.getFriendList(username);
        else {
			this.friendList = friendAdapter.getFriendList(otherUsername);
		}
        friendAdapter.close();
        
    	ListView lvList = (ListView) findViewById(R.id.listViewFriendsList);
        lvList.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, this.friendList));
        lvList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					
				String otherUsername = friendList.get(position).getUsername();
		    	String username = bundle.getString("username");
				Intent intent = new Intent(view.getContext(), HomeActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("other_username", otherUsername);
				intent.putExtra("tab_position", 2);
				startActivity(intent);

			}
        });
        
	}
}
