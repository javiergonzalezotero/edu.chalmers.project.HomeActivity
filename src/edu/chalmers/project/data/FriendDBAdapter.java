package edu.chalmers.project.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Adapter of the database for the table of friendships.
 * This table will have 3 columns: ROW_ID, PLAYERUSERNAME, and FRIENDUSERNAME
 *
 */
public class FriendDBAdapter {
	public static final String ROW_ID = "_id";
	public static final String PLAYERUSERNAME = "playerUsername";
	public static final String FRIENDUSERNAME = "friendUsername";

	private static final String DATABASE_TABLE = "friend";

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
	public FriendDBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the friends database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public FriendDBAdapter open() throws SQLException {
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
	 * Create a new friendship entry. If the friendship is successfully created return the new
	 * rowId for that car, otherwise return a -1 to indicate failure.
	 * 
	 * @param idPlayer
	 * @param idFriend
	 */
	public long createFriendship(String playerUsername, String friendUsername){
		ContentValues initialValues = new ContentValues();
		initialValues.put(PLAYERUSERNAME, playerUsername);
		initialValues.put(FRIENDUSERNAME, friendUsername);
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete a friendship between one player and his friend
	 * @param playerUsername
	 * @param friendUsername
	 * @return
	 */
	public boolean deleteFriendship(String playerUsername, String friendUsername){
		return this.mDb.delete(DATABASE_TABLE, 
				PLAYERUSERNAME + "= '" + playerUsername + "' AND " + 
						FRIENDUSERNAME +" = '" + friendUsername +"'", null) > 0;
	}

	/**
	 * Gets the list of friends of a given user
	 * @param username
	 * @return The arrayList of players who are friends of the user.
	 */
	public ArrayList<Player> getFriendList(String username){
		ArrayList<Player> friendList = new ArrayList<Player>();
		String query = "Select DISTINCT "+ DATABASE_TABLE +"."+FRIENDUSERNAME + ", player." + 
				PlayerDBAdapter.PASSWORD + ", player." + PlayerDBAdapter.FIRSTNAME + 
				", player." + PlayerDBAdapter.FAMILYNAME + ", player." + 
				PlayerDBAdapter.MAIL + ", player." + PlayerDBAdapter.RELIABILITY + ", player." + 
				PlayerDBAdapter.POSITION +", player." + PlayerDBAdapter.CITY +", player." +
				PlayerDBAdapter.BIRTHDATE+", player." + 
				PlayerDBAdapter.IMGPATH +" from "+ DATABASE_TABLE +" join player " +
				" on player."+ PlayerDBAdapter.USERNAME +" = "+DATABASE_TABLE + "."+ FRIENDUSERNAME +
				" where "+DATABASE_TABLE + "." + PLAYERUSERNAME +" = '" + username + "'";
		Cursor mCursor = this.mDb.rawQuery(query, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		while(!mCursor.isAfterLast()){
			Player friendPlayer = new Player(mCursor.getString(0), mCursor.getString(1), 
					mCursor.getString(2), mCursor.getString(3), mCursor.getString(4), mCursor.getInt(5),
					mCursor.getString(6), mCursor.getString(7), mCursor.getString(8),
					mCursor.getString(9));
			friendList.add(friendPlayer);
			mCursor.moveToNext();
		}
		mCursor.close();
		return friendList;
	}

	/**
	 * Checks if one user is friend of another.
	 * @param username The user we want to check his friendlist
	 * @param otherUsername The user we want to see if is the friendlist of "username"
	 * @return True if otherUsername is friend of username, false otherwise.
	 */
	public boolean isFriend(String username, String otherUsername){
		Cursor mCursor = this.mDb.query(true, DATABASE_TABLE, new String[] {FRIENDUSERNAME},
				PLAYERUSERNAME + "=" + "'" + username + "'" + " AND " +
						FRIENDUSERNAME + "=" + "'" + otherUsername + "'", null,null,null,null,null);
		boolean b = false;
		if (mCursor != null) {
			b= mCursor.moveToFirst();
		}
		return b; 
	}
}
