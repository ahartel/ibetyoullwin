package de.ahartel.SoccerLite;

import java.util.ArrayList;
import java.util.List;

public class StreamSeasonContainer {
	public StreamTeamContainer teams;
	public StreamGroupContainer groups;
	public List<StreamMatch> matches;
	public String year;
	
	StreamSeasonContainer()
	{
		matches = new ArrayList<StreamMatch>();
	}
}
