package edu.chalmers.project.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Adapter for accessing the database, concretely the Availability table.
 * This table will have four rows: ROW_ID, USERNAME, DAY and INTERVAL
 *
 */
public class AvailabilityDBAdapter {

	public static final String ROW_ID = "_id";
	public static final String USERNAME = "username";
	public static final String DAY = "day";
	public static final String INTERVAL = "interval";

	private static final String DATABASE_TABLE = "availability";

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
	public AvailabilityDBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the availability database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public AvailabilityDBAdapter open() throws SQLException {
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
	 * Create a new row in the table. If it is successfully created return the new
	 * rowId , otherwise return a -1 to indicate failure.
	 * 
	 * @param username
	 * @param day
	 * @param interval
	 */
	public long createAvailability(String username, String day, String interval){
		ContentValues initialValues = new ContentValues();
		initialValues.put(USERNAME, username);
		initialValues.put(DAY, day);
		initialValues.put(INTERVAL, interval);
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Updates one interval for a row given by username and day.
	 * @param username
	 * @param day
	 * @param interval
	 * @return
	 */
	public boolean updateAvailability(String username, String day, String interval) 
	{
		ContentValues args = new ContentValues();
		args.put(INTERVAL, interval);
		return this.mDb.update(DATABASE_TABLE, args, USERNAME + "= '" + username + "' AND " + DAY + 
				"= '" + day + "'", null) > 0;
	}

	/**
	 * Delete one row in the table according to a given username and day
	 * @param username
	 * @param day
	 * @return
	 */
	public boolean deleteAvailability(String username, String day) {

		return this.mDb.delete(DATABASE_TABLE, USERNAME + "= '" + username + "' AND " + DAY + "= '" + day + "'", null) > 0; 
	}


	/**
	 * Returns a cursor pointing to all the entries in the table that matches a given username
	 * and day of the week.
	 * @param username
	 * @param day
	 * @return
	 * @throws SQLException
	 */
	public Cursor getAvailability(String username, String day)throws SQLException{
		Cursor mCursor =
				this.mDb.query(true, DATABASE_TABLE, new String[] {USERNAME,DAY,INTERVAL}, 
						USERNAME + "= '" + username+ "' AND " + DAY + "= '" + day+ "'", null, null, null, null, null);
		if (mCursor != null ) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}


	/**
	 * Obtains de list of intervals of time for which one user in one day of the week is
	 * available
	 * @param username
	 * @param day
	 * @return
	 */
	public ArrayList<Availability> getAvailabilityBis(String username, String day) {

		ArrayList<Availability> timeAvailability = new ArrayList<Availability>();

		Cursor mCursor = this.mDb.query(true, DATABASE_TABLE, new String[] {ROW_ID,USERNAME,DAY,INTERVAL}, 
				USERNAME + "= '" + username+ "' AND " + DAY + "= '" + day+ "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		while(!(mCursor.isAfterLast())){
			Availability newAvailability = new Availability(Integer.parseInt(mCursor.getString(0)),mCursor.getString(1),mCursor.getString(2),mCursor.getString(3));
			timeAvailability.add(newAvailability);
			mCursor.moveToNext();
		}
		mCursor.close();
		return timeAvailability;
	}



}








