package edu.chalmers.project.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Adapter for accessing the database, concretely the MatchPlayed table.
 * This table will have four rows: ROW_ID, PLAYERUSERNAME, ID_MATCH, TEAM and PRESENT
 *
 */
public class MatchPlayedDBAdapter {
	public static final String ROW_ID = "_id";
	public static final String PLAYERUSERNAME = "playerUsername";
	public static final String ID_MATCH = "idMatch";
	public static final String TEAM = "team";
	public static final String PRESENT = "present";

	private static final String DATABASE_TABLE = "match_played";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private final Context mCtx;

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
	public MatchPlayedDBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the MatchPlayed database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public MatchPlayedDBAdapter open() throws SQLException {
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
	 * Create a new matchPlayed entry. If the row is successfully created return the new
	 * rowId for that entry, otherwise return a -1 to indicate failure.
	 * 
	 * @param playerUsername
	 * @param idMatch
	 * @param team
	 */
	public long joinMatch(String playerUsername, long idMatch, int team){
		ContentValues initialValues = new ContentValues();
		initialValues.put(PLAYERUSERNAME, playerUsername);
		initialValues.put(ID_MATCH, idMatch);
		initialValues.put(TEAM, team);
		initialValues.put(PRESENT, 2);
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Update the value of present in a row for one player in one match
	 * @param playerUsername
	 * @param idMatch
	 * @param present
	 * @return
	 */
	public boolean updatePresent(String playerUsername, long idMatch, int present){
		ContentValues args = new ContentValues();
		args.put(PRESENT, present);
		return this.mDb.update(DATABASE_TABLE, args, PLAYERUSERNAME + "=" + "'" + playerUsername + "'" + 
				" AND " + ID_MATCH + "=" + idMatch, null) > 0;
	}

	/**
	 * Returns a cursor positioned at a row with the value of the Present column for one player
	 * in one match
	 * @param playerUsername
	 * @param idMatch
	 * @return
	 */
	public Cursor getPresent(String playerUsername, int idMatch){
		Cursor mCursor =

				this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, PRESENT}, PLAYERUSERNAME + "=" + "'" + 
						playerUsername + "'" + " AND " + ID_MATCH + "=" + idMatch, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}


	/**
	 * Get the number of players joined to a match
	 * @param idMatch
	 * @return A string with the value of number of players
	 */
	public String getNumberPlayersJoined(long idMatch){
		String query = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " +
				DATABASE_TABLE+ "." + ID_MATCH +" = " + idMatch;
		Cursor mCursor = this.mDb.rawQuery(query, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		Integer count = mCursor.getInt(0);
		mCursor.close();
		return count.toString();

	}

	/**
	 * Delete a row in the table when the user wants to quit the match
	 * @param playerUsername
	 * @param idMatch
	 * @return
	 */
	public boolean quitMatch(String playerUsername, long idMatch){
		return this.mDb.delete(DATABASE_TABLE, 
				PLAYERUSERNAME + "= '" + playerUsername + "' AND " + ID_MATCH +" = "+idMatch, null) > 0; 
	}

	/**
	 * Get the list of players for one team in one match
	 * @param team
	 * @param idMatch
	 * @return
	 */
	public ArrayList<Player> getTeam(int team, long idMatch){
		ArrayList<Player> teamList = new ArrayList<Player>();
		String query = "SELECT DISTINCT "+ DATABASE_TABLE+"."+PLAYERUSERNAME + ", player." + 
				PlayerDBAdapter.PASSWORD + ", player." + PlayerDBAdapter.FIRSTNAME + 
				", player." + PlayerDBAdapter.FAMILYNAME + ", player." + 
				PlayerDBAdapter.MAIL + ", player." + PlayerDBAdapter.RELIABILITY + ", player." + 
				PlayerDBAdapter.POSITION +", player." + PlayerDBAdapter.CITY +", player." +
				PlayerDBAdapter.BIRTHDATE+", player." + PlayerDBAdapter.IMGPATH + " FROM " + 
				DATABASE_TABLE +" JOIN player on " + DATABASE_TABLE+"."+PLAYERUSERNAME + " = player." +
				PlayerDBAdapter.USERNAME + " WHERE " + DATABASE_TABLE +"." + ID_MATCH + " = " + 
				idMatch + " AND " + DATABASE_TABLE + "." + TEAM + " = " + team;
		Cursor mCursor = this.mDb.rawQuery(query, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		while(!(mCursor.isAfterLast())){
			Player newPlayer = new Player(mCursor.getString(0), mCursor.getString(1), 
					mCursor.getString(2), mCursor.getString(3), mCursor.getString(4), mCursor.getInt(5),
					mCursor.getString(6), mCursor.getString(7), mCursor.getString(8),
					mCursor.getString(9));
			teamList.add(newPlayer);
			mCursor.moveToNext();
		}
		mCursor.close();
		return teamList;
	}

	/**
	 * Get the number of matches played by a user in all the history
	 * @param username
	 * @return A String containing the number of matches played
	 */
	public String getMatchesPlayed(String username){
		String query = "SELECT "+ "count(*) FROM " + "match join "+ DATABASE_TABLE + 
				" ON match."+MatchDBAdapter.ROW_ID + " = " + DATABASE_TABLE+"."+MatchPlayedDBAdapter.ID_MATCH
				+ " WHERE " + MatchPlayedDBAdapter.PLAYERUSERNAME + " = '"+ username+"'" +" AND " + 			 
				"date('now') > match."+MatchDBAdapter.DATE + " AND " + 
				DATABASE_TABLE + "." + PRESENT + " = 1"; 
		Cursor mCursor = this.mDb.rawQuery(query, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		Integer res = mCursor.getInt(0);
		mCursor.close();
		return res.toString();    	
	}

	/**
	 * Gets the list of players that were not present at one match
	 * @param idMatch
	 * @return
	 */
	public ArrayList<Player> getPlayersNotPresent(long idMatch){
		ArrayList<Player> playersNotPresentList = new ArrayList<Player>();
		Cursor mCursor = this.mDb.query(true, DATABASE_TABLE, new String[] {PLAYERUSERNAME},
				ID_MATCH + " = "  + idMatch + " AND "+ PRESENT +" = 0", null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		while(!(mCursor.isAfterLast())){
			Player newPlayer = new Player(mCursor.getString(0), null);
			playersNotPresentList.add(newPlayer);
			mCursor.moveToNext();
		}
		mCursor.close();
		return playersNotPresentList;
	}

}