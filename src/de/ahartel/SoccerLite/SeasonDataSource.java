package de.ahartel.SoccerLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SeasonDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.SN_COLUMN_ID,
      MySQLiteHelper.SN_COLUMN_NAME, MySQLiteHelper.SN_COLUMN_STARTED };

  public SeasonDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }
  
  public SeasonDataSource(MySQLiteHelper helper, SQLiteDatabase db) {
	    dbHelper = helper;
	    database = db;
	  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }
 /*
  private String dateToString(Date d)
  {
	  String finalDateTime = "";  
	  int flags = 0;
	  flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
	  flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
	  flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
	  flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

	  finalDateTime = android.text.format.DateUtils.formatDateTime(context,
			  when + TimeZone.getDefault().getOffset(when), flags);
}
  */

  public Season createSeason(String name, Date started) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.SN_COLUMN_NAME, name);
    values.put(MySQLiteHelper.SN_COLUMN_STARTED, "2013-07-21 10:40:50");
    
    long insertId = database.insert(MySQLiteHelper.TABLE_SEASON, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_SEASON,
        allColumns, MySQLiteHelper.SN_COLUMN_ID + " = " + insertId, null,
        null, null, null);
    
    cursor.moveToFirst();
    Season newComment = cursorToSeason(cursor);
    cursor.close();
    return newComment;
  }
  
  public Season getSeason(long id) {

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_SEASON,
	        allColumns, MySQLiteHelper.SN_COLUMN_ID + " = " + id, null,
	        null, null, null);
	    
	    cursor.moveToFirst();
	    Season newComment = cursorToSeason(cursor);
	    cursor.close();
	    return newComment;
	  }
  
  public boolean existsSeason(String name)
  {
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_SEASON,
	        allColumns, MySQLiteHelper.SN_COLUMN_NAME + " = " + name, null,
	        null, null, null);
	    
	    if (cursor.getCount() > 0)
	    	return true;
	    else
	    	return false;
	  }
  
  public Season getSeasonByName(String name) {

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_SEASON,
	        allColumns, MySQLiteHelper.SN_COLUMN_NAME + " = " + name, null,
	        null, null, null);
	    
	    cursor.moveToFirst();
	    Season newComment = cursorToSeason(cursor);
	    cursor.close();
	    return newComment;
	  }

  public void deleteSeason(Season s) {
    long id = s.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_SEASON, MySQLiteHelper.SN_COLUMN_ID
        + " = " + id, null);
  }

  public List<Season> getAllSeasons() {
    List<Season> seasons = new ArrayList<Season>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_SEASON,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Season s = cursorToSeason(cursor);
      seasons.add(s);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return seasons;
  }

  private Season cursorToSeason(Cursor cursor) {
    Season s = new Season();
    s.setId(cursor.getLong(0));
    s.setName(cursor.getString(1));
    return s;
  }
  
  public void drop_recreate_db() {
	  dbHelper.drop_recreate_all(database);
  }
} 