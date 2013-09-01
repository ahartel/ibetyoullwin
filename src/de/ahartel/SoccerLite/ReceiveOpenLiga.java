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

class ReceiveOpenLiga extends AsyncTask<String, Void, String[]> {

    private Exception exception;

    protected String[] doInBackground(String... urls) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://openligadb-json.heroku.com/api/teams_by_league_saison?league_saison=2013&league_shortcut=bl1");
        HttpResponse response;
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


	        	Log.i("ReceiveOpenLiga",result);


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
    	String[] result = new String[1];
    	return result;
    }

    protected void onPostExecute(String[] feed) {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
}