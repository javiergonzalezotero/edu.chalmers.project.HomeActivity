package edu.chalmers.project;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.chalmers.project.data.FriendDBAdapter;
import edu.chalmers.project.data.Player;

public class FriendsActivity extends Activity{

	private ArrayList<Player> friendList= new ArrayList<Player>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.friends_list); 
        Bundle b = this.getIntent().getExtras();
        String username = b.getString("username");
        FriendDBAdapter friendAdapter = new FriendDBAdapter(this);
        friendAdapter.open();
   
        this.friendList = friendAdapter.getFriendList(username);
    	ListView lvList = (ListView) findViewById(R.id.listViewFriendsList);
        lvList.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, this.friendList));
        
        friendAdapter.close();
        
	}
}
