package de.ahartel.SoccerLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class MatchDataSource {
	private SeasonDataSource season_source;
	private TeamDataSource team_source;

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.MA_COLUMN_ID,
	  MySQLiteHelper.MA_COLUMN_SEASON,
      MySQLiteHelper.MA_COLUMN_HOMETEAM,
      MySQLiteHelper.MA_COLUMN_AWAYTEAM,
      MySQLiteHelper.MA_COLUMN_HOMEGOALS,
      MySQLiteHelper.MA_COLUMN_AWAYGOALS,
      MySQLiteHelper.MA_COLUMN_YEAR,
      MySQLiteHelper.MA_COLUMN_MONTH,
      MySQLiteHelper.MA_COLUMN_DAY
      };

  public MatchDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
    season_source = new SeasonDataSource(dbHelper,database);
    team_source = new TeamDataSource(dbHelper,database);
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
  
  public List<Team> getAllTeams(long season_id)
  {
	  return team_source.getAllTeams(season_id);
  }
  
  public List<Team> getAllTeamsNotThis(long season_id, long team_id) {
	  return team_source.getAllTeamsNotThis(season_id,team_id);
  }

  public Match createMatch(long season_id, long home_team, long away_team, int year, int month, int day) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.MA_COLUMN_HOMETEAM, home_team);
    values.put(MySQLiteHelper.MA_COLUMN_AWAYTEAM, away_team);
    values.put(MySQLiteHelper.MA_COLUMN_SEASON, season_id);
    values.put(MySQLiteHelper.MA_COLUMN_YEAR, year);
    values.put(MySQLiteHelper.MA_COLUMN_MONTH, month);
    values.put(MySQLiteHelper.MA_COLUMN_DAY, day);
    
    long insertId = database.insert(MySQLiteHelper.TABLE_MATCH, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_MATCH,
        allColumns, MySQLiteHelper.MA_COLUMN_ID + " = " + insertId, null,
        null, null, null);
    
    Log.i("MatchDataSource",TextUtils.join(",",cursor.getColumnNames()));
    cursor.moveToFirst();
    Match newMatch = cursorToMatch(cursor);
    cursor.close();
    return newMatch;
  }
  
//  public List<Match> getMatches(long season_id, long team_id) {
//
//	    Cursor cursor = database.query(MySQLiteHelper.TABLE_MATCHES,
//	        allColumns, MySQLiteHelper.MA_COLUMN_SEASON + " = " + id, null,
//	        null, null, null);
//	    
//	    cursor.moveToFirst();
//	    Team newComment = cursorToTeam(cursor);
//	    cursor.close();
//	    return newComment;
//	  }

  public void deleteSeason(Season s) {
    long id = s.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_TEAM, MySQLiteHelper.TM_COLUMN_ID
        + " = " + id, null);
  }

  public List<Match> getAllMatches(long season, long team) {
    List<Match> matches = new ArrayList<Match>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_MATCH, allColumns,
    		MySQLiteHelper.MA_COLUMN_SEASON + " = " + season
    		+ " AND (" + MySQLiteHelper.MA_COLUMN_HOMETEAM + " = " + team
    		+ " OR "+MySQLiteHelper.MA_COLUMN_AWAYTEAM + " = " + team + ")",
    		null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Match m = cursorToMatch(cursor);
      matches.add(m);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return matches;
  }

  private Match cursorToMatch(Cursor cursor) {
    Match m = new Match();
    m.setId(cursor.getLong(0));
    m.setSeason(season_source.getSeason(cursor.getLong(1)));
    m.setHomeTeam(team_source.getTeam(cursor.getLong(2)));
    m.setAwayTeam(team_source.getTeam(cursor.getLong(3)));
    m.setDate((int)cursor.getLong(4),(int)cursor.getLong(5),(int)cursor.getLong(6));
    return m;
  }
} 