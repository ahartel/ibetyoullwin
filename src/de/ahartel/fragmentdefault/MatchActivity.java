package de.ahartel.fragmentdefault;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.lang.Math;

import de.ahartel.SoccerLite.Match;
import de.ahartel.SoccerLite.MatchDataSource;
import de.ahartel.SoccerLite.Team;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ItemDetailFragment}.
 */
public class MatchActivity extends FragmentActivity {

	private MatchDataSource datasource;
	private long mSeasonId;
	private long mTeamId;
	private Match mMatch;
	
	double MAX_BET = 10.0;
	
	private EditText editTextHome;
	private EditText editTextDraw;
	private EditText editTextAway;
	
	double win_home;
	double win_none;
	double win_away;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_facts);
        
        mSeasonId = -1;
        mTeamId = -1;
        
        datasource = new MatchDataSource(this);
        datasource.open();

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
        	/*
            Bundle arguments = new Bundle();
            arguments.putLong(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(ItemDetailFragment.ARG_ITEM_ID,0));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
            */
        	/*
        	 * Get data from calling activity
        	 */
            mSeasonId = getIntent().getLongExtra(MatchListFragment.ARG_SEASON_ID,0);
            mTeamId = getIntent().getLongExtra(MatchListFragment.ARG_TEAM_ID,0);
            long matchId = getIntent().getLongExtra(MatchFragment.ARG_MATCH_ID,0);
            
            win_home = 0.0;
            win_none = 0.0;
            win_away = 0.0;
            
            calculate_odds(matchId);
            
            double[] book_odds = get_book_odds();

            editTextHome = (EditText) findViewById(R.id.enterQuoteHome);
            editTextHome.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (book_odds[0] > 0.0) editTextHome.setText(book_odds[0]+"");
            editTextDraw = (EditText) findViewById(R.id.enterQuoteDraw);
            editTextDraw.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (book_odds[1] > 0.0) editTextDraw.setText(book_odds[1]+"");
            editTextAway = (EditText) findViewById(R.id.enterQuoteAway);
            editTextAway.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (book_odds[2] > 0.0) editTextAway.setText(book_odds[2]+"");
            
            calculate_stake();

            TextWatcher tw = new TextWatcher() {

              public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                calculate_stake(); 

              }

              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

              public void onTextChanged(CharSequence s, int start, int before, int count) {}
           };
            
           editTextHome.addTextChangedListener(tw);
           editTextDraw.addTextChangedListener(tw);
           editTextAway.addTextChangedListener(tw);
	        
        }
    }
	
	double[] get_book_odds()
	{
		return datasource.get_book_odds(mMatch.getId());
	}
	
	void calculate_stake()
	{
		float home = (float)0.0;
		float draw = (float) 0.0;
		float away = (float)0.0;
		
		try {
			home = Float.valueOf(editTextHome.getText().toString());
		}
		catch (java.lang.NumberFormatException e) {}
		
		try
		{
			draw = Float.valueOf(editTextDraw.getText().toString());
		}
		catch (java.lang.NumberFormatException e) {}

		try
		{
			away = Float.valueOf(editTextAway.getText().toString());
		}
		catch (java.lang.NumberFormatException e) {}
		
		TextView stake = (TextView)  findViewById(R.id.stake);
		int stake_candidate = -1;
		double difference = 0.0;
		
		if (home > 0.0 && draw > 0.0 && away > 0.0)
		{
			if (win_home > 0.0 && win_none > 0.0 && win_away > 0.0)
			{
				if (home > 1/win_home)
				{
					difference = home-1/win_home;
					stake_candidate = 0;
				}
				if (draw > 1/win_none && draw-1/win_none > difference)
				{
					difference = draw-1/win_none;
					stake_candidate = 1;
				}
				if (away > 1/win_away && away-1/win_away > difference)
				{
					difference = away-1/win_away;
					stake_candidate = 2;
				}
			}
		}
				
		if (stake_candidate == 0)
		{
			stake.setText(mMatch.getHomeTeam().getName()+": "+String.format("%.02f",kelly(win_home,1./home))+" €");
		}
		else if (stake_candidate == 1)
		{
			stake.setText("Draw: "+String.format("%.02f",kelly(win_none,1./draw))+" €");
		}
		else if (stake_candidate == 2)
		{
			stake.setText(mMatch.getAwayTeam().getName()+": "+String.format("%.02f",kelly(win_away,1./away))+" €");
		}
		else
			stake.setText("Don't!");

		datasource.update_book_odds(mMatch.getId(),home,draw,away);
	}
	
	double kelly(double prob_me, double prob_them)
	{
		return MAX_BET*(prob_me/prob_them-1)/(1/prob_them-1);
	}
	
	void calculate_odds(long matchId)
	{
		if (matchId >= 0)
	    {
	    	mMatch = datasource.getMatch(matchId);
	    	TextView matchName = (TextView) findViewById(R.id.MatchName);
	    	matchName.setText(mMatch.toString());
	    	/*
	    	 * print score to textview
	    	 */
			TextView score = (TextView) findViewById(R.id.Score);
	    	if (mMatch.getHomeGoals() >= 0 && mMatch.getAwayGoals() >= 0)
	    		score.setText("Score: "+mMatch.getHomeGoals() + " : " + mMatch.getAwayGoals());
	    	else
	    		score.setText("Score: unset");
	    	/*
	    	 * get past matches
	    	 */
	    	List<Match> matches_home = datasource.getPastMatches(mMatch.getHomeTeam().getId(),true,mMatch.getDateString());
	    	List<Match> matches_away = datasource.getPastMatches(mMatch.getAwayTeam().getId(),false,mMatch.getDateString());
	    	/*
	    	 * get widget pointers
	    	 */
	    	TextView home_avg = (TextView) findViewById(R.id.HomeAverage);
	    	TextView away_avg = (TextView) findViewById(R.id.AwayAverage);
	    	/*
	    	 * Calculate average goals
	    	 */
	    	float home_avg_goals = 0;
	    	float away_avg_goals = 0;
	    	
	    	int home_divisor = 0;
	    	int away_divisor = 0;
	    	
	    	Iterator<Match> iterator = matches_home.iterator();
	    	while (iterator.hasNext()) {
	    		Match current = iterator.next();
	    		home_divisor += 1;
	    		home_avg_goals += current.getHomeGoals();
	    		if (current.getSeason().getId() ==  mSeasonId)
	    		{
	    			home_divisor += 1;
	    			home_avg_goals += current.getHomeGoals();
	    		}
	    	}
	    	home_avg_goals /= home_divisor;
	    	
	    	iterator = matches_away.iterator();
	    	while (iterator.hasNext()) {
	    		Match current = iterator.next();
	    		away_divisor += 1;
	    		away_avg_goals += current.getAwayGoals();
	    		if (current.getSeason().getId() == mSeasonId)
	    		{
	    			away_divisor += 1;
		    		away_avg_goals += current.getAwayGoals();
	    		}
	    	}
	    	away_avg_goals /= away_divisor;
	    	
	    	/*
	    	 * Write average goals to textview
	    	 */
	    	if (matches_home.size() > 0 )
	    		//home_avg.setText("Home goals avg." + home_avg_goals + "/" + matches_home.size() + " = " + home_avg_goals/matches_home.size());
	    		home_avg.setText("Home goals avg." + home_avg_goals);
	    	else
	    		home_avg.setText("No matches found");
	    	
	    	
	    	if (matches_away.size() > 0 )
	    		//away_avg.setText("Away goals avg." + away_avg_goals + "/" + matches_away.size() + " = " + away_avg_goals/matches_away.size());
	    		away_avg.setText("Away goals avg." + away_avg_goals);
	    	else
	    		away_avg.setText("No matches found");
	
	    	if (matches_home.size() > 0  && matches_away.size() > 0)
	    	{
	        	double home_prob[] = new double[6];
	        	double away_prob[] = new double[6];
	        	// Poisson distribution
	        	for (int i=0; i<6; i++)
	        	{
	        		home_prob[i] = Math.exp(-home_avg_goals)*Math.pow(home_avg_goals,i)/(double)factorial(i);
	        		away_prob[i] = Math.exp(-away_avg_goals)*Math.pow(away_avg_goals,i)/(double)factorial(i);
	        	}
	        	//TextView target = (TextView) findViewById(R.id.HomePoisson);
	        	//target.setText(String.format("%.02f, %.02f, %.02f, %.02f, %.02f, %.02f",home_prob[0],home_prob[1],home_prob[2],home_prob[3],home_prob[4],home_prob[5]));
	        	//target = (TextView) findViewById(R.id.AwayPoisson);
	        	//target.setText(String.format("%.02f, %.02f, %.02f, %.02f, %.02f, %.02f",away_prob[0],away_prob[1],away_prob[2],away_prob[3],away_prob[4],away_prob[5]));
	        	
	        	win_home = 0;
	        	win_away = 0;
	        	win_none = 0;
	        	for (int i=0; i<6; i++)
	        	{
	        		for (int j=0; j<6; j++)
	        		{
	        			double product = home_prob[i]*away_prob[j];
	        			if (i==j)
	        				win_none += product;
	        			else if (i < j)
	        				win_away += product;
	        			else if (j < i)
	        				win_home += product;
	        			
	        			//target = (TextView) findViewById(getResources().getIdentifier("text"+i+j,"id",getPackageName()));
	        			//target.setText(String.format("%.02f", product*100.0)+"%");
	        		}
	        	}
	        	win_none /= (win_home+win_away+win_none);
	        	win_home /= (win_home+win_away+win_none);
	        	win_away /= (win_home+win_away+win_none);
	        	
	        	//TextView prob = (TextView) findViewById(R.id.ResultProb);
	        	//prob.setText(win_home+" + "+win_none+" + "+win_away+" = "+(win_away+win_home+win_none));
	        	TextView quote = (TextView) findViewById(R.id.resultQuoteHome);
	        	quote.setText(String.format("%.02f",1./win_home));
	        	quote = (TextView) findViewById(R.id.resultQuoteDraw);
	        	quote.setText(String.format("%.02f",1./win_none));
	        	quote = (TextView) findViewById(R.id.resultQuoteAway);
	        	quote.setText(String.format("%.02f",1./win_away));
	        }
	    }
	}
	
	public int factorial(int i)
	{
		if (i==1 || i==0)
			return 1;
		else return factorial(i-1)*i;
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback

	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show((FragmentManager) getSupportFragmentManager(),
				"datePicker");
	}

	@Override
	public void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	public void onPause() {
		datasource.close();
		super.onPause();
	}
}
