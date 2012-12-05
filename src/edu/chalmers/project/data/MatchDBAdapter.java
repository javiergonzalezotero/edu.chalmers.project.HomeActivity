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
    	
    	return this.matchList;
    }

    
}