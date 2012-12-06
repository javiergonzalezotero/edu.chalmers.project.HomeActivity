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
        initialValues.put(PRESENT, 1);
        return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    
    public boolean quitMatch(String playerUsername, long idMatch){
    	return this.mDb.delete(DATABASE_TABLE, 
    			PLAYERUSERNAME + "= '" + playerUsername + "' AND " + ID_MATCH +" = "+idMatch, null) > 0; 
    }
    
    public ArrayList<Player> getTeam(int team, long idMatch){
    	ArrayList<Player> teamList = new ArrayList<Player>();
    	Cursor mCursor = this.mDb.query(true, DATABASE_TABLE, new String[] {PLAYERUSERNAME},
    			ID_MATCH + " = "  + idMatch + " AND "+ TEAM +" = " + team, null,null,null,null,null);
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
    	while(!(mCursor.isAfterLast())){
    		Player newPlayer = new Player(mCursor.getString(0), null);
    		teamList.add(newPlayer);
    		mCursor.moveToNext();
    	}
    	mCursor.close();
    	return teamList;
    }
    
    /**
     * Return a Cursor positioned at the match that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching car, if found
     * @throws SQLException if player could not be found/retrieved
     */
}