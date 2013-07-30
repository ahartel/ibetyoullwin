package de.ahartel.SoccerLite;

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
  public static final String TM_COLUMN_SEASON = "season_id";

  private static final String DATABASE_NAME = "SoccerLite.db";
  private static final int DATABASE_VERSION = 3;

  // Database creation sql statement
  private static final String DATABASE_CREATE_SEASON = "create table "
      + TABLE_SEASON + "(" + SN_COLUMN_ID
      + " integer primary key autoincrement, " + SN_COLUMN_NAME
      + " text not null, " + SN_COLUMN_STARTED + " text not null);";
  
  private static final String DATABASE_CREATE_TEAM = "create table "
	      + TABLE_TEAM + "(" + TM_COLUMN_ID
	      + " integer primary key autoincrement, " + TM_COLUMN_NAME
	      + " text not null, " + TM_COLUMN_SEASON + " integer not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE_SEASON);
    database.execSQL(DATABASE_CREATE_TEAM);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEASON);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
    onCreate(db);
  }

} 