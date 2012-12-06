package edu.chalmers.project.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
     * Open the cars database. If it cannot be opened, try to create a new
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
     * Return a Cursor positioned at the match that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching car, if found
     * @throws SQLException if player could not be found/retrieved
     */
  /*  public Cursor getMatch(long rowId) throws SQLException {

        Cursor mCursor =

        this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, DATE,
        		NAME, FIELD, LOCATION, COST, NUMBER_PLAYERS, ID_ORGANIZER}, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    */
    
    public ArrayList<Player> getFriendList(String username){
    	ArrayList<Player> friendList = new ArrayList<Player>();
    	Cursor mCursor = this.mDb.query(true, DATABASE_TABLE, new String[] {FRIENDUSERNAME},
    			PLAYERUSERNAME + "=" + "'" + username + "'", null,null,null,null,null);
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
    	while(!(mCursor.isAfterLast())){
    		Player friendPlayer = new Player(mCursor.getString(0), null);
    		friendList.add(friendPlayer);
    		mCursor.moveToNext();
    	}
    	mCursor.close();
    	return friendList;
    }
    
    /**
     * @return Return the list of all the matches
     */
 /*   public ArrayList<Match> getMatchList() {
    	
    	int rowId = 1;
    	Cursor cursor;
    	cursor = this.getMatch(rowId);
    	
    	
    	while(cursor.getCount() != 0){
    		Match m = new Match(cursor.getString(2), "ok","ok","ok","ok",1,1);
	    	this.matchList.add(m);
	    	rowId = rowId + 1;
	    	cursor = this.getMatch(rowId);    	
    	}
    	
    	
    	return this.matchList;
    }
*/
}
