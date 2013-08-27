package de.ahartel.fragmentdefault;

import java.util.Calendar;

import de.ahartel.SoccerLite.Match;
import de.ahartel.SoccerLite.MatchDataSource;
import de.ahartel.SoccerLite.Team;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CompoundButton;
import android.widget.Toast;


/**
 * An activity representing a single Item detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ItemDetailFragment}.
 */
public class MatchListActivity extends FragmentActivity
			implements MatchListFragment.Callbacks, OnItemSelectedListener, OnCheckedChangeListener {

	private MatchDataSource datasource;
	private long mSeasonId;
	private long mTeamId;
	private Team selectedTeam;
	private int mSetYear;
	private int mSetMonth;
	private int mSetDay;
	private boolean team_is_home;
	
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);
        
        mSeasonId = -1;
        mTeamId = -1;
        mSetYear = -1;
        mSetMonth = -1;
        mSetDay = -1;
        
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
            mSeasonId = getIntent().getLongExtra(MatchListFragment.ARG_SEASON_ID,0);
            mTeamId = getIntent().getLongExtra(MatchListFragment.ARG_TEAM_ID,0);
        }

        if (mSeasonId >= 0 && mTeamId >= 0)
        {
        	MatchListFragment listfrag = ((MatchListFragment) getSupportFragmentManager()
        			.findFragmentById(R.id.match_list_container));
        	
        	listfrag.setValues(datasource.getAllMatches(mSeasonId,mTeamId));
        	
        	listfrag.setTeams(datasource.getAllTeamsNotThis(mSeasonId,mTeamId));
        }
        
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpTo(this, new Intent(this, SeasonActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
    
    /**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(long id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(ItemDetailFragment.ARG_ITEM_ID, id);
            TeamListFragment fragment = new TeamListFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            Log.i("ItemListActivity", "ItemListActivity.onItemSelected() — id " + id);
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MatchActivity.class);
            detailIntent.putExtra(MatchListFragment.ARG_SEASON_ID, mSeasonId);
            detailIntent.putExtra(MatchListFragment.ARG_TEAM_ID, id);
            startActivity(detailIntent);
        }
    }
    
    // Callback from switch
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
       this.team_is_home = isChecked;
    }
    
    /*
     * Callback from DatePicker
     */
    public void setDate(int year, int month, int day) {
    	mSetYear = year;
    	mSetMonth = month;
    	mSetDay = day;
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        selectedTeam = (Team)parent.getItemAtPosition(pos);
        Log.i("MatchListActivity","selected Team " + selectedTeam.getName());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }
    
    public void showDatePickerDialog(View v) {
    	DialogFragment newFragment = new DatePickerFragment();
    	newFragment.show((FragmentManager)getSupportFragmentManager(), "datePicker");
    }
    
    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
      @SuppressWarnings("unchecked")

      MatchListFragment listfrag = ((MatchListFragment) getSupportFragmentManager()
              .findFragmentById(R.id.match_list_container));

      Match t = null;
      switch (view.getId()) {
      case R.id.add:
    	long otherTeam = -1;
    	try {
    		otherTeam = selectedTeam.getId();
    	}
    	catch (NullPointerException e) {
    		break;
    	}
    	if (otherTeam >= 0)
    	{
    		// Save the new match to the database
    		if (team_is_home) {
    			t = datasource.createMatch(mSeasonId,mTeamId,otherTeam,mSetYear,mSetMonth,mSetDay);
    		}
    		else {
    			t = datasource.createMatch(mSeasonId,otherTeam,mTeamId,mSetYear,mSetMonth,mSetDay);
    		}
    		
    		listfrag.add(t);
    	}
        break;
        /*
      case R.id.delete:
        if (listfrag.getCount() > 0) {
          s = (Season) listfrag.front();
          datasource.deleteSeason(s);
          listfrag.remove(s);
        }
        break;
        */
      }
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
