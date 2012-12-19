package edu.chalmers.project;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.chalmers.project.data.Player;

/**
 * Class that acts like an array adapter for lists of player in order to show the profile
 * photo in the list
 *
 */
public class PlayerListAdapter extends ArrayAdapter<Player> {

	private ArrayList<Player> playerList;
	private Context context;
	private int layout;
	PlayerListAdapter(Context context, ArrayList<Player> playerList, int layout) {
		super( context, R.layout.player_list_item, R.id.playerName, playerList);
		this.playerList = playerList;
		this.context = context;
		this.layout = layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			row=inflater.inflate(this.layout, parent, false);
			ImageView icon=(ImageView)row.findViewById(R.id.imagePlayerList);
			TextView playerName = (TextView) row.findViewById(R.id.playerName);
			playerName.setText(this.playerList.get(position).getUsername());
			String imgPath = this.playerList.get(position).getImgPath();
			if (!imgPath.equals("")){
				icon.setImageBitmap(ImageLoader.decodeSampledBitmapFromResource(imgPath, 
						icon.getLayoutParams().width,icon.getLayoutParams().height));
			}
		}
		return(row);
	}
}
