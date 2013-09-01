package de.ahartel.SoccerLite;

import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_SEASON = "season";
  public static final String SN_COLUMN_ID = "_id";
  public static final String SN_COLUMN_NAME = "name";
  public static final String SN_COLUMN_STARTED = "started";
  
  public static final String TABLE_TEAM = "team";
  public static final String TM_COLUMN_ID = "_id";
  public static final String TM_COLUMN_NAME = "name";
  
  public static final String TABLE_MATCH = "match";
  public static final String MA_COLUMN_ID = "_id";
  public static final String MA_COLUMN_HOMETEAM = "home_team_id";
  public static final String MA_COLUMN_AWAYTEAM = "away_team_id";
  public static final String MA_COLUMN_SEASON = "season_id";
  public static final String MA_COLUMN_HOMEGOALS = "home_goals";
  public static final String MA_COLUMN_AWAYGOALS = "away_goals";
  public static final String MA_COLUMN_DATE = "date";
  
  public static final String TABLE_TEAM_TO_SEASON = "team_to_season";
  public static final String TS_COLUMN_ID = "_id";
  public static final String TS_COLUMN_TEAM = "team_id";
  public static final String TS_COLUMN_SEASON = "season_id";

  private static final String DATABASE_NAME = "SoccerLite.db";
  private static final int DATABASE_VERSION = 18;

  // Database creation sql statement
  private static final String DATABASE_CREATE_SEASON = "create table "
      + TABLE_SEASON + "(" + SN_COLUMN_ID
      + " integer primary key autoincrement, " + SN_COLUMN_NAME
      + " text not null, " + SN_COLUMN_STARTED + " text not null);";
  
  private static final String DATABASE_CREATE_TEAM = "create table "
	      + TABLE_TEAM + "(" + TM_COLUMN_ID
	      + " integer primary key autoincrement, " + TM_COLUMN_NAME
	      + " text not null);";
  
  private static final String DATABASE_CREATE_MATCH = "create table "
	      + TABLE_MATCH + "(" + MA_COLUMN_ID
	      + " integer primary key autoincrement, " + MA_COLUMN_HOMETEAM
	      + " integer not null, " + MA_COLUMN_AWAYTEAM + " integer not null,"
	      + MA_COLUMN_SEASON + " integer not null, "+MA_COLUMN_HOMEGOALS+" integer,"
	      + MA_COLUMN_AWAYGOALS + " integer, " + MA_COLUMN_DATE + " text not null);";
  
  private static final String DATABASE_CREATE_TEAM_TO_SEASON = "create table "
	      + TABLE_TEAM_TO_SEASON + "(" + TS_COLUMN_ID
	      + " integer primary key autoincrement, " + TS_COLUMN_TEAM
	      + " integer not null, " + TS_COLUMN_SEASON + " integer not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE_SEASON);
    database.execSQL(DATABASE_CREATE_TEAM);
    database.execSQL(DATABASE_CREATE_MATCH);
    database.execSQL(DATABASE_CREATE_TEAM_TO_SEASON);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    drop_recreate(db);
  }
  
  public void drop_recreate(SQLiteDatabase db) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEASON);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM_TO_SEASON);
    onCreate(db);
    
    db.execSQL("INSERT INTO " + TABLE_SEASON + " (" + SN_COLUMN_NAME + "," + SN_COLUMN_STARTED + ") VALUES ('2012/13','2012-08-24')");
    db.execSQL("INSERT INTO " + TABLE_SEASON + " (" + SN_COLUMN_NAME + "," + SN_COLUMN_STARTED + ") VALUES ('2013/14','2012-08-09')");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Bayern München')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (1,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (1,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Borussia Dortmund')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (2,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (2,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Bayer 04 Leverkusen')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (3,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (3,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'FC Schalke 04')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (4,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (4,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'SC Freiburg')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (5,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (5,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Eintracht Frankfurt')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (6,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (6,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Hamburger SV')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (7,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (7,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Borussia Mönchengladbach')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (8,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (8,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Hannover 96')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (9,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (9,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'FC Nürnberg')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (10,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (10,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'VfL Wolfsburg')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (11,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (11,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'VfB Stuttgart')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (12,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (12,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'FSV Mainz 05')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (13,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (13,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Werder Bremen')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (14,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (14,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'FC Augsburg')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (15,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (15,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'TSG 1899 Hoffenheim')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (16,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (16,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Fortuna Düsseldorf')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (17,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'SpVgg Greuther Fürth')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (18,1)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Hertha BSC')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (19,2)");
    db.execSQL("INSERT INTO " + TABLE_TEAM + " (" + TM_COLUMN_NAME + ") VALUES ( 'Eintracht Braunschweig')");
    db.execSQL("INSERT INTO " + TABLE_TEAM_TO_SEASON + " ("+ TS_COLUMN_TEAM + "," + TS_COLUMN_SEASON + ") VALUES (20,2)");
    
    new ReceiveOpenLiga().execute("https://openligadb-json.heroku.com/api/teams_by_league_saison?league_saison=2013&league_shortcut=bl1");
  }

} 