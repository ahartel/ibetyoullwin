package de.ahartel.SoccerLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TeamDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.TM_COLUMN_ID,
      MySQLiteHelper.TM_COLUMN_NAME };

  public TeamDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }
  
  public TeamDataSource(MySQLiteHelper helper, SQLiteDatabase db) {
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

  public Team createTeam(String name, long season_id) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.TM_COLUMN_NAME, name);
    values.put(MySQLiteHelper.TM_COLUMN_SEASON, season_id);
    
    long insertId = database.insert(MySQLiteHelper.TABLE_TEAM, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_TEAM,
        allColumns, MySQLiteHelper.TM_COLUMN_ID + " = " + insertId, null,
        null, null, null);
    
    cursor.moveToFirst();
    Team newTeam = cursorToTeam(cursor);
    cursor.close();
    return newTeam;
  }
  
  public Team getTeam(long id) {

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TEAM,
	        allColumns, MySQLiteHelper.TM_COLUMN_ID + " = " + id, null,
	        null, null, null);
	    
	    cursor.moveToFirst();
	    Team newComment = cursorToTeam(cursor);
	    cursor.close();
	    return newComment;
	  }

  public void deleteSeason(Season s) {
    long id = s.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_TEAM, MySQLiteHelper.TM_COLUMN_ID
        + " = " + id, null);
  }

  public List<Team> getAllTeams(long season) {
    List<Team> teams = new ArrayList<Team>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_TEAM,
        allColumns, MySQLiteHelper.TM_COLUMN_SEASON + " = " + season, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Team s = cursorToTeam(cursor);
      teams.add(s);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return teams;
  }
  
  public List<Team> getAllTeamsNotThis(long season, long team) {
	    List<Team> teams = new ArrayList<Team>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TEAM,
	        allColumns, MySQLiteHelper.TM_COLUMN_SEASON + " = " + season + " AND "+ MySQLiteHelper.TM_COLUMN_ID + " != " + team, null , null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Team s = cursorToTeam(cursor);
	      teams.add(s);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return teams;
	  }


  private Team cursorToTeam(Cursor cursor) {
    Team s = new Team();
    s.setId(cursor.getLong(0));
    s.setName(cursor.getString(1));
    return s;
  }
} 