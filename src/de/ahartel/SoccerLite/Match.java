
package de.ahartel.SoccerLite;

import java.util.Calendar;
import java.util.Locale;

import android.util.Log;

public class Match {
  private long id;
  private Team home_team;
  private Team away_team;
  private Season season;
  private Calendar date;
  private long home_goals;
  private long away_goals;
  
  public Match()
  {
	  this.date = Calendar.getInstance();
  }
  
  public Match(Team ht, Team at, Season s)
  {
	  this.home_team = ht;
	  this.away_team = at;
	  this.season = s;
	  this.date = Calendar.getInstance();
  }
  
  public String getDate()
  {
	  return date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH)+1) + "." + date.get(Calendar.YEAR);
  }
  
  public String getDateString()
  {
	  return date.get(Calendar.YEAR) + "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Team getHomeTeam() {
    return home_team;
  }
  
  public Team getAwayTeam() {
	    return away_team;
	  }
  
  public void setSeason(Season s) {
	this.season = s;
  }
  
  public void setHomeTeam(Team t) {
		this.home_team = t;
	  }
  
  public void setAwayTeam(Team t) {
		this.away_team = t;
	  }
  
  public long getHomeGoals()
  {
	  return home_goals;
  }
	  
  public long getAwayGoals() {
	  return away_goals;
  }

  public void setHomeGoals(long goals) {
    this.home_goals = goals;
  }
  
  public void setAwayGoals(long goals) {
	  this.away_goals = goals;
  }
  
  public void setDate(int year, int month, int day) {
	  this.date	.set(year,month,day);
  }

  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
    return home_team.toString() + " vs. " + away_team.toString() + ", " + getDate();
  }
} 