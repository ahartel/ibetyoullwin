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
  public static final String MA_COLUMN_FINISHED = "is_finished";
  
  public static final String TABLE_TEAM_TO_SEASON = "team_to_season";
  public static final String TS_COLUMN_ID = "_id";
  public static final String TS_COLUMN_TEAM = "team_id";
  public static final String TS_COLUMN_SEASON = "season_id";
  
  public static final String TABLE_MATCH_ODDS = "match_odds";
  public static final String MO_COLUMN_ID = "_id";
  public static final String MO_COLUMN_HOME = "home_odds";
  public static final String MO_COLUMN_DRAW = "draw_odds";
  public static final String MO_COLUMN_AWAY = "away_odds";

  private static final String DATABASE_NAME = "SoccerLite.db";
  private static final int DATABASE_VERSION = 21;

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
	      + MA_COLUMN_AWAYGOALS + " integer, " + MA_COLUMN_DATE + " text not null,"
	      + MA_COLUMN_FINISHED + " bool not null default false);";
  
  private static final String DATABASE_CREATE_TEAM_TO_SEASON = "create table "
	      + TABLE_TEAM_TO_SEASON + "(" + TS_COLUMN_ID
	      + " integer primary key autoincrement, " + TS_COLUMN_TEAM
	      + " integer not null, " + TS_COLUMN_SEASON + " integer not null);";
  
  private static final String DATABASE_CREATE_MATCH_ODDS = "create table if not exists "
	      + TABLE_MATCH_ODDS + "(" + MO_COLUMN_ID
	      + " integer primary key autoincrement, " + MO_COLUMN_HOME + " float not null, "
	      + MO_COLUMN_DRAW + " float not null, " + MO_COLUMN_AWAY + " float not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE_SEASON);
    database.execSQL(DATABASE_CREATE_TEAM);
    database.execSQL(DATABASE_CREATE_MATCH);
    database.execSQL(DATABASE_CREATE_TEAM_TO_SEASON);
    database.execSQL(DATABASE_CREATE_MATCH_ODDS);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    drop_recreate_all(db);
  }
  
  public void drop_recreate_all(SQLiteDatabase db) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEASON);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM_TO_SEASON);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH_ODDS);
    onCreate(db);
  }
  
  public void drop_recreate_except_odds(SQLiteDatabase db)
  {
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEASON);
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM_TO_SEASON);
	  onCreate(db);
  }

} 
