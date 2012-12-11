  package edu.chalmers.project.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	 * Open the cars database. If it cannot be opened, try to create a new
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
	 * @param idPlayer
	 * @param idFriend
	 */
	public long joinMatch(String playerUsername, long idMatch, int team){
		ContentValues initialValues = new ContentValues();
		initialValues.put(PLAYERUSERNAME, playerUsername);
		initialValues.put(ID_MATCH, idMatch);
		initialValues.put(TEAM, team);
		initialValues.put(PRESENT, 2);
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean updatePresent(String playerUsername, long idMatch, int present) 
	{
		ContentValues args = new ContentValues();
		args.put(PRESENT, present);
		return this.mDb.update(DATABASE_TABLE, args, PLAYERUSERNAME + "=" + "'" + playerUsername + "'" + 
		" AND " + ID_MATCH + "=" + idMatch, null) > 0;
	}
	
	public Cursor getPresent(String playerUsername, int idMatch){
		Cursor mCursor =

				this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, PRESENT}, PLAYERUSERNAME + "=" + "'" + 
				playerUsername + "'" + " AND " + ID_MATCH + "=" + idMatch, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}


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

	public boolean quitMatch(String playerUsername, long idMatch){
		return this.mDb.delete(DATABASE_TABLE, 
				PLAYERUSERNAME + "= '" + playerUsername + "' AND " + ID_MATCH +" = "+idMatch, null) > 0; 
	}

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