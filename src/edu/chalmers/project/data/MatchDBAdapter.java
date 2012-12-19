package edu.chalmers.project.data;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Adapter for accessing the database, concretely the Match table.
 * This table will have ten rows: ROW_ID, DATE, TIME, NAME, FIELD, LOCATION, COST,
 * NUMBER_PLAYERS, ID_ORGANIZER and ID_MVP
 *
 */
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
	 * Open the match database. If it cannot be opened, try to create a new
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
	 * @param date
	 * @param time
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

	/**
	 * Delete the match with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteMatch(long rowId) {

		return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; 
	}


	/**
	 * Updates the information of a match given his id
	 * @param id
	 * @param date
	 * @param time
	 * @param name
	 * @param field
	 * @param location
	 * @param cost
	 * @param numberPlayers
	 * @return
	 */
	public boolean updateMatch( long id, String date, String time, String name, String field,
			String location, int cost, int numberPlayers){
		ContentValues args = new ContentValues();
		args.put(DATE, date);
		args.put(TIME, time);
		args.put(NAME, name);
		args.put(FIELD, field);
		args.put(LOCATION, location);
		args.put(COST, cost);
		args.put(NUMBER_PLAYERS, numberPlayers);

		return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + id, null) >0; 
	}

	/**
	 * Updates the MVP of one match
	 * @param idMatch
	 * @param idMvp
	 * @return
	 */
	public boolean updateMvp(long idMatch, int idMvp){
		ContentValues args = new ContentValues();
		args.put(ID_MVP, idMvp);
		return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + idMatch, null) > 0;
	}


	/**
	 * Return a Cursor positioned at the match that matches the given rowId
	 * @param rowId
	 * @return Cursor positioned to matching match, if found
	 * @throws SQLException if match could not be found/retrieved
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

	/**
	 * Return a Cursor positioned at the MVP of the match given by its rowId.
	 * @param rowId
	 * @return
	 * @throws SQLException
	 */
	public Cursor getMvp(long rowId) throws SQLException {
		Cursor mCursor =

				this.mDb.query(true, DATABASE_TABLE, new String[] { ID_MVP}, ROW_ID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Auxiliary method that is going to execute a specific query and return a list of matches 
	 * as a result of this query.
	 * @param query
	 * @return
	 */
	public ArrayList<Match> execQuery(String query){
		matchList = new ArrayList<Match>();
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
		Collections.sort(this.matchList, new MatchComparable());
		return this.matchList;
	}

	/**
	 * @return Return the list of all the future matches
	 */
	public ArrayList<Match> getMatchList() {


		String query = "SELECT distinct "+ DATABASE_TABLE +"."+ ROW_ID + ", "+DATABASE_TABLE +"."+ DATE  
				+", "+DATABASE_TABLE +"."+ TIME + ", "+DATABASE_TABLE +"."+ NAME + ", "+
				DATABASE_TABLE +"."+ FIELD +", "+DATABASE_TABLE +"."+ LOCATION +", "+ 
				DATABASE_TABLE +"."+ COST +", "+DATABASE_TABLE +"."+ NUMBER_PLAYERS +", "+ 
				DATABASE_TABLE +"."+ ID_ORGANIZER +" FROM "+ DATABASE_TABLE +
				" WHERE date('now') < " + DATABASE_TABLE + "." + DATE;
		return execQuery(query);

	}

	/**
	 * Return the list of all past matches
	 * @return
	 */
	public ArrayList<Match> getPastEvents(){
		String query = "SELECT distinct "+ DATABASE_TABLE +"."+ ROW_ID + ", "+DATABASE_TABLE +"."+ DATE  
				+", "+DATABASE_TABLE +"."+ TIME + ", "+DATABASE_TABLE +"."+ NAME + ", "+
				DATABASE_TABLE +"."+ FIELD +", "+DATABASE_TABLE +"."+ LOCATION +", "+ 
				DATABASE_TABLE +"."+ COST +", "+DATABASE_TABLE +"."+ NUMBER_PLAYERS +", "+ 
				DATABASE_TABLE +"."+ ID_ORGANIZER +" FROM "+ DATABASE_TABLE +
				" WHERE date('now') > " + DATABASE_TABLE + "." + DATE;
		return execQuery(query);
	}

	/**
	 * Return the list of all the future matches that a user is joined to
	 * @param username
	 * @return
	 */
	public ArrayList<Match> getUpcomingEvents(String username){
		String query = "SELECT distinct "+ DATABASE_TABLE +"."+ ROW_ID + ", "+DATABASE_TABLE +"."+ DATE  
				+", "+DATABASE_TABLE +"."+ TIME + ", "+DATABASE_TABLE +"."+ NAME + ", "+
				DATABASE_TABLE +"."+ FIELD +", "+DATABASE_TABLE +"."+ LOCATION +", "+ 
				DATABASE_TABLE +"."+ COST +", "+DATABASE_TABLE +"."+ NUMBER_PLAYERS +", "+ 
				DATABASE_TABLE +"."+ ID_ORGANIZER +" FROM "+ DATABASE_TABLE +
				" join match_played on "+ "match_played."+ MatchPlayedDBAdapter.ID_MATCH+ " = "+ 
				DATABASE_TABLE+"."+ROW_ID + " WHERE "+ "match_played."+
				MatchPlayedDBAdapter.PLAYERUSERNAME + " = '"+ username +"'" + " AND " +
				"date('now') <= " + DATABASE_TABLE + "." + DATE;
		return execQuery(query);
	}

	/**
	 * Returns the list of all matches that match with the availability of one user
	 * @param username
	 * @return
	 */
	public ArrayList<Match> getEventAvailability(String username){
		String query = "SELECT distinct "+ DATABASE_TABLE +"."+ ROW_ID + ", "+DATABASE_TABLE +"."+ DATE  
				+", "+DATABASE_TABLE +"."+ TIME + ", "+DATABASE_TABLE +"."+ NAME + ", "+
				DATABASE_TABLE +"."+ FIELD +", "+DATABASE_TABLE +"."+ LOCATION +", "+ 
				DATABASE_TABLE +"."+ COST +", "+DATABASE_TABLE +"."+ NUMBER_PLAYERS +", "+ 
				DATABASE_TABLE +"."+ ID_ORGANIZER +" FROM "+ DATABASE_TABLE +
				" join match_played on "+ "match_played."+ MatchPlayedDBAdapter.ID_MATCH+ " = "+ 
				DATABASE_TABLE+"."+ROW_ID + " WHERE "+ "match_played."+
				MatchPlayedDBAdapter.PLAYERUSERNAME + " = '"+ username +"'" + " AND " +
				"date('now') <= " + DATABASE_TABLE + "." + DATE;
		return execQuery(query);
	}

	/**
	 * Gets the number of MVP's of one player in all history.
	 * @param idPlayer
	 * @return A string with the number of MVP's
	 */
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

	/**
	 * Gets the list of all the matches created by one user (future and past)
	 * @param username
	 * @return
	 */
	public ArrayList<Match> getMyEvents(String username) {   	
		String query = "SELECT distinct "+ DATABASE_TABLE +"."+ ROW_ID + ", "+DATABASE_TABLE +"."+ DATE  
				+", "+DATABASE_TABLE +"."+ TIME + ", "+DATABASE_TABLE +"."+ NAME + ", "+
				DATABASE_TABLE +"."+ FIELD +", "+DATABASE_TABLE +"."+ LOCATION +", "+ 
				DATABASE_TABLE +"."+ COST +", "+DATABASE_TABLE +"."+ NUMBER_PLAYERS +", "+ 
				DATABASE_TABLE +"."+ ID_ORGANIZER +" FROM "+ DATABASE_TABLE +
				" join player on "+ "player."+ PlayerDBAdapter.ROW_ID+ " = "+ 
				DATABASE_TABLE+"."+ID_ORGANIZER + " WHERE "+ "player."+PlayerDBAdapter.USERNAME + 
				" = '"+ username +"'";
		return execQuery(query);
	}

	/**
	 * Return a cursor positioned at a selection of one row in match with ROW_ID and 
	 * ID_ORGANIZER as the only columns.
	 * @param idMatch
	 * @return
	 * @throws SQLException
	 */
	public Cursor getIdOrganizer(long idMatch) throws SQLException {

		Cursor mCursor =

				this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, ID_ORGANIZER}, ROW_ID + "=" + idMatch, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Method that checks if a name for a match is already used in another row.
	 * @param matchName
	 * @return
	 */
	public boolean matchNameExists(String matchName){
		Cursor mCursor =
				this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID}, 
						NAME + "= '" + matchName+ "'", null, null, null, null, null);
		if (mCursor != null ) {
			mCursor.moveToFirst();
		}
		boolean res = mCursor.isAfterLast();
		mCursor.close();
		return res;
	}

	/**
	 * Returns the day of the week for one date
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public Cursor getDayDate(String date) throws SQLException {
		String query = "SELECT strftime( '%w' , '" + date + "')";
		Cursor cursor = this.mDb.rawQuery(query, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}



}
