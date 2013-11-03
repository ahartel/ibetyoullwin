package de.ahartel.SoccerLite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
//import com.google.gson.annotations.SerializedName;

public class ReceiveOpenLiga extends AsyncTask<Object, Void, StreamSeasonContainer> {

    private Exception exception;
    private MatchDataSource datasource;
    private String base_url;
    private String season_year;

    protected StreamSeasonContainer doInBackground(Object... input) {
    	StreamSeasonContainer season = new StreamSeasonContainer();
    	this.datasource = (MatchDataSource)input[0];
    	this.base_url = (String)input[1];
    	this.season_year = (String)input[2]; 
    	
    	season.teams = getTeams(base_url+"/teams_by_league_saison?league_saison="+season_year+"&league_shortcut=bl1");
 
    	season.groups = getGroups(base_url+"/avail_groups?league_saison="+season_year+"&league_shortcut=bl1");
    	
    	for (StreamGroup g : season.groups.group)
    	{
    		season.matches.addAll(getMatches(base_url+"/matchdata_by_group_league_saison?group_order_id="+g.group_order_id+"&league_saison="+season_year+"&league_shortcut=bl1").matchdata);
    	}
    	
    	return season;
    }
    
    private String getHttp (String url)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response;
        String result = new String();
        
        try {
        	response = httpClient.execute(httpGet);
        	// TODO: HTTP-Status (z.B. 404) in eigener Anwendung verarbeiten.
        	//Log.i("MySQLiteHelper",response.getStatusLine().toString());
        	
        	HttpEntity entity = response.getEntity();

        	if (entity != null)
        	{
	        	InputStream instream = entity.getContent();
	
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
	
	        	StringBuilder sb = new StringBuilder();
	
	        	String line = null;
	
	        	while ((line = reader.readLine()) != null)
	        		sb.append(line + "n");

	        	result=sb.toString();
	        	result = result.substring(0, result.length() - 1);
	        	
	        	//Log.i("ReceiveOpenLiga",result);
	        	//Log.i("ReceiveOpenLiga",url);
	        	
	        	instream.close();
        	}
        }
    	catch (ClientProtocolException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        finally{

        	httpGet.abort();

        }

    	return result;
    }
    
    private StreamTeamContainer getTeams (String url)
    {
        StreamTeamContainer stc = new StreamTeamContainer();
        
        Gson gson = new Gson();
	    stc = gson.fromJson(getHttp(url), StreamTeamContainer.class);

    	return stc;
    }
    
    private StreamGroupContainer getGroups (String url)
    {
        StreamGroupContainer stc = new StreamGroupContainer();
        
        Gson gson = new Gson();
	    stc = gson.fromJson(getHttp(url), StreamGroupContainer.class);

    	return stc;
    }
    
    private StreamMatchContainer getMatches (String url)
    {
        StreamMatchContainer stc = new StreamMatchContainer();
        
        Gson gson = new Gson();
        String result = getHttp(url);
        //Log.i("bla",result);
	    stc = gson.fromJson(result, StreamMatchContainer.class);
//	    Log.i("ReceiveOpenLiga",stc.matchdata.get(0).id_team1+" "+stc.matchdata.get(0).id_team2);

    	return stc;
    }

    protected void onPostExecute(StreamSeasonContainer season) {
        // TODO: check this.exception 
        // TODO: do something with the feed
    	Season insert_season;
    	datasource.open();
    	if (!datasource.season_source.existsSeason(season_year))
    		insert_season = datasource.season_source.createSeason(season_year, null);
    	else
    		insert_season = datasource.season_source.getSeasonByName(season_year);
    	
    	for (StreamTeam t : season.teams.team)
    	{
    		//Log.i("Creating team",t.team_id+" "+t.team_name);
    		if (!datasource.team_source.existsTeam(t.team_id))
    			datasource.team_source.createTeam(t.team_id, t.team_name, insert_season.getId() );
    		else
    			datasource.team_source.attachTeam(t.team_id, insert_season.getId());
    	}
    	
    	for (StreamMatch m : season.matches)
    	{
    		if (!datasource.existsMatch(m.match_id))
    			datasource.createMatch(insert_season.getId(),m.match_id,m.id_team1,m.id_team2,m.points_team1,m.points_team2,m.match_date_time_utc,m.match_is_finished);
    	}
    }
}