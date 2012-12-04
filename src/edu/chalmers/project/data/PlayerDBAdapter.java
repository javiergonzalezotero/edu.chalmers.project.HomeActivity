package edu.chalmers.project.data;

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
     * close return type: void
     */
    public void close() {
        this.mDbHelper.close();
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
    		String mail,int reliability, String position, String city, String birthdate){
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
        return this.mDb.insert(DATABASE_TABLE, null, initialValues);
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

        this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, FAMILYNAME,
                USERNAME, PASSWORD}, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getPassword(String username)throws SQLException{
    	Cursor mCursor =

    	        this.mDb.query(true, DATABASE_TABLE, new String[] {PASSWORD}, 
    	        		USERNAME + "= '" + username+ "'", null, null, null, null, null);
    	        if (mCursor != null) {
    	            mCursor.moveToFirst();
    	        }
    	        return mCursor;
    }

    /**
     * Update the car.
     * 
     * @param rowId
     * @param name
     * @param model
     * @param year
     * @return true if the note was successfully updated, false otherwise
     */
   /* public boolean updateCar(long rowId, String name, String model,
            String year){
        ContentValues args = new ContentValues();
        args.put(NAME, name);
        args.put(MODEL, model);
        args.put(YEAR, year);

        return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
    }
    */
}
