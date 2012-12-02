package edu.chalmers.project.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	public static final String DATABASE_NAME = "SocialFootball"; 
	
	public static final int DATABASE_VERSION = 1;
	
	private static final String CREATE_TABLE_PLAYERS =
	   "create table  player(_id integer primary key autoincrement, " 
	+ PlayerDBAdapter.USERNAME+ " TEXT not null," 
	+ PlayerDBAdapter.PASSWORD+ " TEXT not null,"
	+ PlayerDBAdapter.FIRSTNAME+ " TEXT,"
	+ PlayerDBAdapter.FAMILYNAME+ " TEXT,"
	+ PlayerDBAdapter.MAIL+ " TEXT,"
	+ PlayerDBAdapter.RELIABILITY+ " INTEGER,"
	+ PlayerDBAdapter.POSITION+ " TEXT,"
	+ PlayerDBAdapter.CITY+ " TEXT,"
	
	+ PlayerDBAdapter.BIRTHDATE+ " TEXT" + ");"; 
	
	
	private final Context context; 
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	/**
	 * Constructor
	 * @param ctx
	 */
	public DBAdapter(Context ctx){
	    this.context = ctx;
	    this.DBHelper = new DatabaseHelper(this.context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
	    DatabaseHelper(Context context){
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(CREATE_TABLE_PLAYERS);
	    }
	
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {               
	        // Adding any table mods to this guy here
	    }
	} 
	
	/**
	 * open the db
	 * @return this
	 * @throws SQLException
	 * return type: DBAdapter
	 */
	public DBAdapter open() throws SQLException{
	    this.db = this.DBHelper.getWritableDatabase();
	    return this;
	}
	
	/**
	 * close the db 
	 * return type: void
	 */
	public void close() {
	    this.DBHelper.close();
	}
}