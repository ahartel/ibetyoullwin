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

class ReceiveOpenLiga extends AsyncTask<String, Void, String[]> {

    private Exception exception;

    protected String[] doInBackground(String... urls) {
    	StreamTeamContainer teams2012 = getTeams(urls[0]+"?league_saison=2012&league_shortcut=bl1");
    	StreamTeamContainer teams2013 = getTeams(urls[0]+"?league_saison=2013&league_shortcut=bl1");
    	
    	
    	return new String[1];
    }
    
    private StreamTeamContainer getTeams (String url)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response;
        StreamTeamContainer stc = new StreamTeamContainer();
        
        try {
        	response = httpClient.execute(httpGet);
        	// TODO: HTTP-Status (z.B. 404) in eigener Anwendung verarbeiten.
        	Log.i("MySQLiteHelper",response.getStatusLine().toString());
        	
        	HttpEntity entity = response.getEntity();

        	if (entity != null)
        	{
	        	InputStream instream = entity.getContent();
	
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
	
	        	StringBuilder sb = new StringBuilder();
	
	        	String line = null;
	
	        	while ((line = reader.readLine()) != null)
	        		sb.append(line + "n");

	        	String result=sb.toString();
	        	result = result.substring(0, result.length() - 1);
	        	instream.close();
	        	Gson gson = new Gson();
	        	stc = gson.fromJson(result, StreamTeamContainer.class);
	        	Log.i("ReceiveOpenLiga",stc.team.get(0).team_name);
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
    	/*
        try {
            URL url= new URL(urls[0]);
            SAXParserFactory factory =SAXParserFactory.newInstance();
            SAXParser parser=factory.newSAXParser();
            XMLReader xmlreader=parser.getXMLReader();
            RssHandler theRSSHandler=new RssHandler();
            xmlreader.setContentHandler(theRSSHandler);
            InputSource is=new InputSource(url.openStream());
            xmlreader.parse(is);
            return theRSSHandler.getFeed();
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
        */

    	return stc;
    }

    protected void onPostExecute(String[] feed) {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
}