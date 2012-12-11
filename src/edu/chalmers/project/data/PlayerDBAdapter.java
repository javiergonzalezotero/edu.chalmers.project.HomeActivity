package edu.chalmers.project.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayerDBAdapter {
	public static final String ROW_ID = "_id";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FIRSTNAME = "firstname";
    public static final String FAMILYNAME = "familyname";
    public static final String MAIL = "mail";
    public static final String RELIABILITY = "reliability";
    public static final String POSITION = "position";
    public static final String CITY = "city";
    public static final String BIRTHDATE = "birthdate";
    public static final String IMGPATH = "imgpath";

	private static final String DATABASE_TABLE = "player";

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
	public PlayerDBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the player database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public PlayerDBAdapter open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}


    /**
     * Create a new player. If the player is successfully created return the new
     * rowId for that player, otherwise return a -1 to indicate failure.
     * 
     * @param username
     * @param password
     * @param firstname
     * @param familyname
     * @param mail
     * @param reliability
     * @param position
     * @param city
     * @param birthdate
     * @return rowId or -1 if failed
     */
    public long createPlayer(String username, String password, String familyName, String firstName, 
    		String mail,int reliability, String position, String city, String birthdate, String imgPath){
        ContentValues initialValues = new ContentValues();
        initialValues.put(USERNAME, username);
        initialValues.put(PASSWORD, password);
        initialValues.put(FIRSTNAME, firstName);
        initialValues.put(FAMILYNAME, familyName);
        initialValues.put(MAIL, mail);
        initialValues.put(RELIABILITY, reliability);
        initialValues.put(POSITION, position);
        initialValues.put(CITY, city);
        initialValues.put(BIRTHDATE, birthdate);
        initialValues.put(IMGPATH, imgPath);
        return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    }

	/**
	 * close return type: void
	 */
	public void close() {
		this.mDbHelper.close();
	}

	/**
	 * Delete the player with the given rowId
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePlayer(long rowId) {

		return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; 
	}


	/**
	 * Return a Cursor positioned at the player that matches the given rowId
	 * @param rowId
	 * @return Cursor positioned to matching car, if found
	 * @throws SQLException if player could not be found/retrieved
	 */
	public Cursor getPlayer(long rowId) throws SQLException {

		Cursor mCursor =

        this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, USERNAME,PASSWORD,FIRSTNAME,
        		FAMILYNAME,MAIL,RELIABILITY,POSITION,CITY,BIRTHDATE, IMGPATH}, 
        		ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getPlayer(String username)throws SQLException{
    	Cursor mCursor =
    	        this.mDb.query(true, DATABASE_TABLE, new String[] {USERNAME,PASSWORD,FIRSTNAME,FAMILYNAME,
    	        		MAIL,RELIABILITY,POSITION,CITY,BIRTHDATE, ROW_ID, IMGPATH}, 
    	        		USERNAME + "= '" + username+ "'", null, null, null, null, null);
    	        if (mCursor != null ) {
    	            mCursor.moveToFirst();
    	        }
    	        return mCursor;
    }
				
    /**
     * Update the player.
     * 
     * @param username
     * @param password
     * @param firstname
     * @param familyname
     * @param mail
     * @param position
     * @param city
     * @param birthdate
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updatePlayer( String username, String password, String firstname, String familyname,
            String mail, String position, String city, String birthdate, String imgPath){
        ContentValues args = new ContentValues();
        args.put(USERNAME, username);
        args.put(PASSWORD, password);
        args.put(FIRSTNAME, firstname);
        args.put(FAMILYNAME, familyname);
        args.put(MAIL, mail);
        args.put(POSITION, position);
        args.put(CITY, city);
        args.put(BIRTHDATE, birthdate);
        args.put(IMGPATH, imgPath);
        
        return this.mDb.update(DATABASE_TABLE, args, USERNAME + "=" + "'" + username + "'", null) >0; 
    }


	public boolean updateReliability(String username, int present){
		Cursor cursorPlayer;
		cursorPlayer = this.getPlayer(username);
		int currentReliability = Integer.parseInt(cursorPlayer.getString(5));
		ContentValues args = new ContentValues();
		if(present == 0){
			int newReliability = currentReliability - 2;
			if(newReliability < 0)
				newReliability = 0;
			args.put(RELIABILITY, newReliability);
		}
		if(present == 1){
			int newReliability = currentReliability + 5;
			if(newReliability > 100)
				newReliability = 100;
			args.put(RELIABILITY, newReliability);

		}

		return this.mDb.update(DATABASE_TABLE, args, USERNAME + "=" + "'" + username + "'", null) >0; 
	}

	/**
	 * @return Return the list of all the players
	 */
	public ArrayList<Player> getPlayerList() {

		int rowId = 1;
		Cursor cursor;
		cursor = this.getPlayer(rowId);
		ArrayList<Player> playerList = new ArrayList<Player>();

		while(cursor.getCount() != 0){
			Player p = new Player(cursor.getString(1),null);
			playerList.add(p);
			rowId = rowId + 1;
			cursor = this.getPlayer(rowId);    	
		}
		cursor.close();

		return playerList;
	}

	public ArrayList<Player> searchPlayer(String search){
		int rowId = 1;
		Cursor cursor;
		cursor = this.getPlayer(rowId);
		ArrayList<Player> playerList = new ArrayList<Player>();
		while(cursor.getCount() != 0){
			if (cursor.getString(1).startsWith(search)) {
				Player p = new Player(cursor.getString(1),cursor.getString(2),cursor.getString(3),
						cursor.getString(4),cursor.getString(5),cursor.getInt(6),
						cursor.getString(7),cursor.getString(8),cursor.getString(9),
						cursor.getString(10));
				playerList.add(p);
			}
			rowId = rowId + 1;
			cursor = this.getPlayer(rowId);    	
		}
		cursor.close();

		return playerList;
	}

}








