package edu.chalmers.project.data;

import java.sql.Date;
import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MatchDBAdapter {

	public static final String ROW_ID = "_id";
	public static final String DATE = "date";
	public static final String TIME = "time";
	public static final String NAME = "name";
	public static final String FIELD = "field";
	public static final String LOCATION = "location";
	public static final String COST = "cost";
	public static final String NUMBER_PLAYERS = "number_players";
	public static final String ID_ORGANIZER = "id_organizer";
	public static final String ID_MVP = "id_mvp";

	private static final String DATABASE_TABLE = "match";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private final Context mCtx;

	private ArrayList<Match> matchList = new ArrayList();

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public MatchDBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the cars database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public MatchDBAdapter open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * close return type: void
	 */
	public void close() {
		this.mDbHelper.close();
	}

	/**
	 * Create a new match. If the match is successfully created return the new
	 * rowId for that car, otherwise return a -1 to indicate failure.
	 * 
	 * @param id
	 * @param date
	 * @param name
	 * @param field
	 * @param location
	 * @param cost
	 * @param number_players
	 * @param id_organiezr
	 * @return rowId or -1 if failed
	 */
	public long createMatch(String date, String time, String name, String field, String location, int cost, int number_players, long id_organizer){
		ContentValues initialValues = new ContentValues();
		initialValues.put(DATE, date);
		initialValues.put(TIME, time);
		initialValues.put(NAME, name);
		initialValues.put(FIELD, field);
		initialValues.put(LOCATION, location);
		initialValues.put(COST, cost);
		initialValues.put(NUMBER_PLAYERS, number_players);
		initialValues.put(ID_ORGANIZER, id_organizer);
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public boolean updateMvp(long idMatch, int idMvp) 
	{
		ContentValues args = new ContentValues();
		args.put(ID_MVP, idMvp);
		return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + idMatch, null) > 0;
	}


	/**
	 * Return a Cursor positioned at the match that matches the given rowId
	 * @param rowId
	 * @return Cursor positioned to matching car, if found
	 * @throws SQLException if player could not be found/retrieved
	 */
	public Cursor getMatch(long rowId) throws SQLException {

		Cursor mCursor =

				this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, DATE, TIME,
						NAME, FIELD, LOCATION, COST, NUMBER_PLAYERS, ID_ORGANIZER}, ROW_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor getMvp(long rowId) throws SQLException {

		Cursor mCursor =

				this.mDb.query(true, DATABASE_TABLE, new String[] { ID_MVP}, ROW_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * @return Return the list of all the matches
	 */
	public ArrayList<Match> getMatchList() {

		int rowId = 1;
		Cursor cursor;
		cursor = this.getMatch(rowId);


		while(cursor.getCount() != 0){
			Match m = new Match(rowId, cursor.getString(3),cursor.getString(1),cursor.getString(2),cursor.getString(5),cursor.getString(4),
					Integer.parseInt(cursor.getString(6)),Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)));
			this.matchList.add(m);
			rowId = rowId + 1;
			cursor = this.getMatch(rowId);    	
		}
		cursor.close();

		return this.matchList;
	}

	
	public ArrayList<Match> getUpcomingEvents(String username){
		matchList = new ArrayList<Match>();
		String query = "SELECT "+ DATABASE_TABLE +"."+ ROW_ID + ", "+DATABASE_TABLE +"."+ DATE  
				+", "+DATABASE_TABLE +"."+ TIME + ", "+DATABASE_TABLE +"."+ NAME + ", "+
				DATABASE_TABLE +"."+ FIELD +", "+DATABASE_TABLE +"."+ LOCATION +", "+ 
				DATABASE_TABLE +"."+ COST +", "+DATABASE_TABLE +"."+ NUMBER_PLAYERS +", "+ 
				DATABASE_TABLE +"."+ ID_ORGANIZER +" FROM "+ DATABASE_TABLE +
				" join match_played on "+ "match_played."+ MatchPlayedDBAdapter.ID_MATCH+ " = "+ 
				DATABASE_TABLE+"."+ROW_ID + " WHERE "+ "match_played."+
				MatchPlayedDBAdapter.PLAYERUSERNAME + " = '"+ username +"'";
		Cursor cursor = this.mDb.rawQuery(query, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		while(!cursor.isAfterLast()){
			Match m = new Match(cursor.getInt(0), cursor.getString(3),cursor.getString(1),
					cursor.getString(2),cursor.getString(5),cursor.getString(4),
					Integer.parseInt(cursor.getString(6)),Integer.parseInt(cursor.getString(7)), 
					Integer.parseInt(cursor.getString(8)));
			this.matchList.add(m);
			cursor.moveToNext();   	
		}
		cursor.close();  	
		return this.matchList;
	}
	
	
	public String getNumberMVPs(long idPlayer){
		String query = "SELECT COUNT(*)"+ " FROM "+ DATABASE_TABLE + " WHERE "+ 
				DATABASE_TABLE + "." + ID_MVP + " = "+ idPlayer;
		Cursor cursor = this.mDb.rawQuery(query, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		Integer res = cursor.getInt(0);
		cursor.close();
		return res.toString();

	}

	public ArrayList<Match> getMyEvents(String username) {   	
		matchList = new ArrayList<Match>();
		String query = "SELECT "+ DATABASE_TABLE +"."+ ROW_ID + ", "+DATABASE_TABLE +"."+ DATE  
				+", "+DATABASE_TABLE +"."+ TIME + ", "+DATABASE_TABLE +"."+ NAME + ", "+
				DATABASE_TABLE +"."+ FIELD +", "+DATABASE_TABLE +"."+ LOCATION +", "+ 
				DATABASE_TABLE +"."+ COST +", "+DATABASE_TABLE +"."+ NUMBER_PLAYERS +", "+ 
				DATABASE_TABLE +"."+ ID_ORGANIZER +" FROM "+ DATABASE_TABLE +
				" join player on "+ "player."+ PlayerDBAdapter.ROW_ID+ " = "+ 
				DATABASE_TABLE+"."+ID_ORGANIZER + " WHERE "+ "player."+PlayerDBAdapter.USERNAME + 
				" = '"+ username +"'";
		Cursor cursor = this.mDb.rawQuery(query, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		while(!cursor.isAfterLast()){
			Match m = new Match(cursor.getInt(0), cursor.getString(3),cursor.getString(1),
					cursor.getString(2),cursor.getString(5),cursor.getString(4),
					Integer.parseInt(cursor.getString(6)),Integer.parseInt(cursor.getString(7)), 
					Integer.parseInt(cursor.getString(8)));
			this.matchList.add(m);
			cursor.moveToNext();   	
		}
		cursor.close();  	
		return this.matchList;
	}
	
	public Cursor getIdOrganizer(long idMatch) throws SQLException {

		Cursor mCursor =

				this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, ID_ORGANIZER}, ROW_ID + "=" + idMatch, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}


}