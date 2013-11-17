package de.ahartel.SoccerLite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.ahartel.fragmentdefault.MatchListActivity;
import de.ahartel.fragmentdefault.R;

import com.google.gson.Gson;

public class ReceiveOpenLiga extends AsyncTask<Object, Integer, List<StreamSeasonContainer> > {

    private Exception exception;
    private MatchDataSource datasource;
    private String base_url;
    private MatchListActivity mAct;
    private Context mContext;
    private LinearLayout mProgress;
    private TextView mProgressText;
    
    public ReceiveOpenLiga(Context context)
    {
    	mContext = context;
    	mAct = (MatchListActivity) mContext;
    	mProgress = (LinearLayout) mAct.findViewById(R.id.ProgressBarWrap);
    	mProgressText = (TextView) mAct.findViewById(R.id.ProgressText);
    }

    protected List<StreamSeasonContainer> doInBackground(Object... input) {
    	List<StreamSeasonContainer> seasons = new ArrayList<StreamSeasonContainer>();
    	this.datasource = (MatchDataSource)input[0];
    	this.base_url = (String)input[1]; 
    	
    	List<String> season_years = new ArrayList<String>();
    	season_years.add("2013");
    	season_years.add("2012");
    	
    	// initialize counter for total number of groups to fetch
    	int groups_size = 0;
    	
    	for (String season_year : season_years)
    	{
    		Log.i("ReceiveOpenLiga","Loading teams and groups for year "+season_year);
    		StreamSeasonContainer season = new StreamSeasonContainer();
    		season.year = season_year;
    		season.teams = getTeams(base_url+"/teams_by_league_saison?league_saison="+season_year+"&league_shortcut=bl1");
 
    		season.groups = getGroups(base_url+"/avail_groups?league_saison="+season_year+"&league_shortcut=bl1");
    	
	    	groups_size += season.groups.group.size();
	    	seasons.add(season);
    	}

    	// publish progress zero
    	int cnt = 1;
    	publishProgress(groups_size,cnt);
    	
    	// load all groups for all seasons
    	for (StreamSeasonContainer season : seasons)
    	{
    		Log.i("ReceiveOpenLiga","Loading group details for year "+season.year);
	    	for (StreamGroup g : season.groups.group)
	    	{
	    		season.matches.addAll(getMatches(base_url+"/matchdata_by_group_league_saison?group_order_id="+g.group_order_id+"&league_saison="+season.year+"&league_shortcut=bl1").matchdata);
	    		cnt++;
	    		publishProgress(groups_size,cnt);
	    	}
    	}

    	return seasons;
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
    
    @Override
    protected void onPreExecute()
    {
    	mProgress.setVisibility(View.VISIBLE);
    }

    protected void onProgressUpdate(Integer... progress) {
    	//mProgress.setMax(progress[0]);
        //mProgress.setProgress(progress[1]);
    	mProgressText.setText("Lade Spieltag "+progress[1]+" / "+progress[0]);
    }

    protected void onPostExecute(List<StreamSeasonContainer> seasons) {
        // TODO: check this.exception 
        // TODO: do something with the feed
    	Season insert_season;
    	//datasource.open();
    	
    	for (StreamSeasonContainer season : seasons)
    	{
	    	if (!datasource.season_source.existsSeason(season.year))
	    		insert_season = datasource.season_source.createSeason(season.year, null);
	    	else
	    		insert_season = datasource.season_source.getSeasonByName(season.year);
	    	
	    	Log.i("Insert season",season.year+","+insert_season.getId());
	    	
	    	for (StreamTeam t : season.teams.team)
	    	{
	    		Log.i("Creating team",t.team_id+" "+t.team_name);
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

    	mProgress.setVisibility(View.GONE);
    	mAct.load_next_matches();
    }
}