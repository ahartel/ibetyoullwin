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
	public SeasonDataSource season_source;
	public TeamDataSource team_source;

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.MA_COLUMN_ID,
	  MySQLiteHelper.MA_COLUMN_SEASON,
      MySQLiteHelper.MA_COLUMN_HOMETEAM,
      MySQLiteHelper.MA_COLUMN_AWAYTEAM,
      MySQLiteHelper.MA_COLUMN_HOMEGOALS,
      MySQLiteHelper.MA_COLUMN_AWAYGOALS,
      MySQLiteHelper.MA_COLUMN_DATE,
      MySQLiteHelper.MA_COLUMN_FINISHED
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
  
  public List<Team> getAllTeams(long season_id)
  {
	  return team_source.getAllTeams(season_id);
  }
  
  public List<Team> getAllTeamsNotThis(long season_id, long team_id) {
	  return team_source.getAllTeamsNotThis(season_id,team_id);
  }
  
  public Match createMatch(long season_id, long match_id, long home_team, long away_team, int home_goals, int away_goals, String date, boolean finished) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.MA_COLUMN_ID, match_id);
	    values.put(MySQLiteHelper.MA_COLUMN_HOMETEAM, home_team);
	    values.put(MySQLiteHelper.MA_COLUMN_AWAYTEAM, away_team);
	    values.put(MySQLiteHelper.MA_COLUMN_SEASON, season_id);
	    values.put(MySQLiteHelper.MA_COLUMN_HOMEGOALS, home_goals);
	    values.put(MySQLiteHelper.MA_COLUMN_AWAYGOALS, away_goals);
	    values.put(MySQLiteHelper.MA_COLUMN_DATE, date);
	    values.put(MySQLiteHelper.MA_COLUMN_FINISHED, finished);
	    
	    long insertId = database.insert(MySQLiteHelper.TABLE_MATCH, null,
	        values);
	    return getMatch(insertId);
	  }

  public Match createMatch(long season_id, long home_team, long away_team, int year, int month, int day) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.MA_COLUMN_HOMETEAM, home_team);
    values.put(MySQLiteHelper.MA_COLUMN_AWAYTEAM, away_team);
    values.put(MySQLiteHelper.MA_COLUMN_SEASON, season_id);
    values.putNull(MySQLiteHelper.MA_COLUMN_HOMEGOALS);
    values.putNull(MySQLiteHelper.MA_COLUMN_AWAYGOALS);
    values.put(MySQLiteHelper.MA_COLUMN_DATE, year+"-"+month+"-"+day);
    
    long insertId = database.insert(MySQLiteHelper.TABLE_MATCH, null,
        values);
    return getMatch(insertId);
  }
  
  public Match createMatch(long season_id, long home_team, long away_team, long home_goals, long away_goals, int year, int month, int day) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.MA_COLUMN_HOMETEAM, home_team);
	    values.put(MySQLiteHelper.MA_COLUMN_AWAYTEAM, away_team);
	    values.put(MySQLiteHelper.MA_COLUMN_SEASON, season_id);
	    values.put(MySQLiteHelper.MA_COLUMN_HOMEGOALS, home_goals);
	    values.put(MySQLiteHelper.MA_COLUMN_AWAYGOALS, away_goals);
	    values.put(MySQLiteHelper.MA_COLUMN_DATE, year+"-"+month+"-"+day);
	    
	    long insertId = database.insert(MySQLiteHelper.TABLE_MATCH, null,
	        values);
	    return getMatch(insertId);
	  }
  
  public boolean existsMatch(long id)
  {
    Cursor cursor = database.query(MySQLiteHelper.TABLE_MATCH,
        allColumns, MySQLiteHelper.MA_COLUMN_ID + " = " + id, null,
        null, null, null);

    if (cursor.getCount() > 0)
    	return true;
    else
    	return false;
  }
  
  public Match getMatch(long id)
  {
    Cursor cursor = database.query(MySQLiteHelper.TABLE_MATCH,
        allColumns, MySQLiteHelper.MA_COLUMN_ID + " = " + id, null,
        null, null, null);

    cursor.moveToFirst();
    Match newMatch = cursorToMatch(cursor);
    cursor.close();
    return newMatch;
  }
  
	public List<Match> getPastMatches(long team_id, boolean home_away, String date_string)
	{
		//String query_string = new String("strftime('%s',"+MySQLiteHelper.MA_COLUMN_DATE + ") < strftime('%s','" + date_string + "') AND ");
		String query_string = new String(MySQLiteHelper.MA_COLUMN_FINISHED+" = 1 AND "+MySQLiteHelper.MA_COLUMN_DATE + " < '" + date_string + "' AND ");
	    List<Match> matches = new ArrayList<Match>();
	    
		if (home_away == true)
		{
			query_string += MySQLiteHelper.MA_COLUMN_HOMETEAM + " = " + team_id;
		}
		else
		{
			query_string += MySQLiteHelper.MA_COLUMN_AWAYTEAM + " = " + team_id;
		}

		Cursor cursor = database.query(MySQLiteHelper.TABLE_MATCH,
				allColumns, query_string, null,
				null, null, null);
		
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Match s = cursorToMatch(cursor);
	      matches.add(s);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();

	    return matches;
	}

  public void deleteSeason(Season s) {
    long id = s.getId();
    //System.out.println("Comment deleted with id: " + id);
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
  
  public List<Match> getNextMatches(long season) {
	    List<Match> matches = new ArrayList<Match>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_MATCH, allColumns,
	    		MySQLiteHelper.MA_COLUMN_SEASON + " = " + season
	    		+ " AND " + MySQLiteHelper.MA_COLUMN_FINISHED + " = 0",
	    		null, null, null, MySQLiteHelper.MA_COLUMN_DATE+" ASC ", "20");

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
    if (cursor.isNull(4))
    	m.setHomeGoals(-1);
    else
    	m.setHomeGoals(cursor.getLong(4));
    if (cursor.isNull(5))
    	m.setAwayGoals(-1);
    else
    	m.setAwayGoals(cursor.getLong(5));
    String date = new String(cursor.getString(6));
    String date_time[] = date.split("T");
    String dates[] = date_time[0].split("-");
    int year = Integer.parseInt(dates[0]);
    int month = Integer.parseInt(dates[1]);
    int day = Integer.parseInt(dates[2]);
    m.setDate(year,month-1,day);
    return m;
  }
  
  public void drop_recreate_db() {
	  dbHelper.drop_recreate_except_odds(database);
  }
} 