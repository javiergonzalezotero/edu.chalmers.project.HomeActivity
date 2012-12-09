package edu.chalmers.project.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoalDBAdapter {
	public static final String ROW_ID = "_id";
	public static final String PLAYERUSERNAME = "playerUsername";
	public static final String ID_MATCH = "idMatch";
	public static final String MINUTE = "minute";


	private static final String DATABASE_TABLE = "goal";

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
	public GoalDBAdapter(Context ctx) {
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
	public GoalDBAdapter open() throws SQLException {
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
	public long insertGoal(String playerUsername, long idMatch, int minute){
		ContentValues initialValues = new ContentValues();
		initialValues.put(PLAYERUSERNAME, playerUsername);
		initialValues.put(ID_MATCH, idMatch);
		initialValues.put(MINUTE, minute);
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
	}



	public ArrayList<Goal> getMatchListGoal(long idMatch) throws SQLException {

		ArrayList<Goal> goalList = new ArrayList<Goal>();
		Cursor mCursor =

				this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, PLAYERUSERNAME, ID_MATCH,
						MINUTE}, ID_MATCH + "=" + idMatch, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		while(!(mCursor.isAfterLast())){
			Goal newGoal = new Goal(Integer.parseInt(mCursor.getString(0)), mCursor.getString(1), Integer.parseInt(mCursor.getString(2)), 
					Integer.parseInt(mCursor.getString(3)));
			goalList.add(newGoal);
			mCursor.moveToNext();
		}
		mCursor.close();
		return goalList;
	}

	public ArrayList<Goal> getGoalTeam(long idMatch, int numberTeam) throws SQLException {

		ArrayList<Goal> goalList = new ArrayList<Goal>();

		String DATABASE_TABLE_JOIN = "match_played";
		String queryGoal = "SELECT " + DATABASE_TABLE + "." + ROW_ID + ", " + DATABASE_TABLE +"." + PLAYERUSERNAME + ", " + 
		DATABASE_TABLE + "." + ID_MATCH + ", " + DATABASE_TABLE + "." + MINUTE + ", " + DATABASE_TABLE_JOIN + "." + 
				PLAYERUSERNAME + ", " +	DATABASE_TABLE_JOIN + "." + MatchPlayedDBAdapter.TEAM + " FROM goal join match_played on " + 
		DATABASE_TABLE + "." + ID_MATCH + "=" + DATABASE_TABLE_JOIN + "." + MatchPlayedDBAdapter.ID_MATCH +
				" where " + DATABASE_TABLE + "." + ID_MATCH + "=" + idMatch + " AND " + DATABASE_TABLE_JOIN + "." + MatchPlayedDBAdapter.TEAM + "=" + numberTeam;

		Cursor mCursor = this.mDb.rawQuery(queryGoal, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		while(!(mCursor.isAfterLast())){
			Goal newGoal = new Goal(Integer.parseInt(mCursor.getString(0)), mCursor.getString(1), Integer.parseInt(mCursor.getString(2)), 
					Integer.parseInt(mCursor.getString(3)));
			goalList.add(newGoal);
			mCursor.moveToNext();
		}
		mCursor.close();
		return goalList;
	}

	public String getGoalsScored(String username) throws SQLException {
		String query = "SELECT COUNT(*) FROM "+ DATABASE_TABLE + 
				" WHERE "+ PLAYERUSERNAME+ " = '"+ username + "'";
		Cursor mCursor = this.mDb.rawQuery(query, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		Integer res = mCursor.getInt(0);
		mCursor.close();
		return res.toString();
	}

	public Cursor getGoalMinuteUsername(String username, int idMatch, int minute){
		Cursor mCursor =

				this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, PLAYERUSERNAME,
						ID_MATCH, MINUTE}, PLAYERUSERNAME + "=" + "'" + username + "'" + " AND " + ID_MATCH + "=" + idMatch + 
						" AND " + MINUTE + "=" + minute, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}


}