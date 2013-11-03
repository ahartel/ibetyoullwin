package de.ahartel.SoccerLite;

import java.util.List;

final class MatchResult {
	public String result_name;
	public long result_order_id;
	public long points_team1;
	public String result_type_name;
	public long points_team2;
	public long result_type_id;
}

final class MatchResults {
	List<MatchResult> match_result;
}

final class Location {
	public long location_id;
	public String location_city;
	public String location_stadium;
}

final class Goal {
	public long goal_match_minute;
	public long goal_getter_id;
	public long goal_id;
	public String goal_getter_name;
	public long goal_mach_id;
	public boolean goal_penalty;
	public long goal_score_team1;
	public String goal_comment;
	public boolean goal_own_goal;
	public long goal_score_team2;
	public boolean goal_overtime;
}

final class Goals {
	List<Goal> goal;
}

public class StreamMatch {
	public long match_id;
	public long id_team1;
	public long id_team2;
	public int points_team1;
	public int points_team2;
	public String match_date_time_utc;
	public boolean match_is_finished;
	/*
	Goals goals;
	public long group_id;
	public String group_name;
	public long group_order_id;
	public String icon_url_team1;
	public String icon_url_team2;

	public String last_update;
	public long league_id;
	public String league_name;
	public long league_saison;	
	public String league_shortcut;
	Location location;
	public String match_date_time;
	public long match_id;
	MatchResults match_results;
	public String name_team1;
	public String name_team2;
	public long number_of_viewers;

	public String time_zone_id;
	*/
}
