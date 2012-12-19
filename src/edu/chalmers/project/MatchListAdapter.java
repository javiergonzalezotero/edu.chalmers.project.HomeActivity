package edu.chalmers.project;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.chalmers.project.data.Match;

/**
 * Array Adapter used to show the lists of matches with detailed information
 *
 */
public class MatchListAdapter extends ArrayAdapter<Match>{
	private ArrayList<Match> matchList;
	private Context context;
	private int layout;

	MatchListAdapter(Context context, ArrayList<Match> matchList, int layout) {
		super( context, R.layout.match_list_item, R.id.playerName, matchList);
		this.matchList = matchList;
		this.context = context;
		this.layout = layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			row=inflater.inflate(this.layout, parent, false);
			TextView date=(TextView)row.findViewById(R.id.dateMatchList);
			TextView matchName = (TextView) row.findViewById(R.id.nameMatchList);
			TextView matchPlace = (TextView) row.findViewById(R.id.placeMatchList);
			TextView matchTime = (TextView) row.findViewById(R.id.timeMatchList);
			matchPlace.setText(this.matchList.get(position).getField());
			matchName.setText(this.matchList.get(position).getName());
			date.setText(this.matchList.get(position).getDate());
			matchTime.setText(this.matchList.get(position).getTime());
		}
		return(row);
	}
}