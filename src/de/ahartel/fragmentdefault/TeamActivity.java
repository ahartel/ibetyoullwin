package de.ahartel.fragmentdefault;

import java.util.Date;
import java.util.Random;

import de.ahartel.SoccerLite.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details
 * (if present) is a {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class TeamActivity extends FragmentActivity
        implements TeamListFragment.Callbacks {
	
	private TeamDataSource datasource;
	private long mSeasonId;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        
        datasource = new TeamDataSource(this);
        datasource.open();

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((SeasonListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }
        
        if (savedInstanceState == null) {
        	mSeasonId = getIntent().getLongExtra(ItemDetailFragment.ARG_ITEM_ID,0);
        }
        
        TeamListFragment listfrag = ((TeamListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.team_list));
        listfrag.setValues(datasource.getAllTeams(mSeasonId));

        // TODO: If exposing deep links into your app, handle intents here.
    }

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
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MatchListActivity.class);
            detailIntent.putExtra(MatchListFragment.ARG_SEASON_ID, mSeasonId);
            detailIntent.putExtra(MatchListFragment.ARG_TEAM_ID, id);
            startActivity(detailIntent);
        }
    }
    
    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
      @SuppressWarnings("unchecked")
      
      TeamListFragment listfrag = ((TeamListFragment) getSupportFragmentManager()
              .findFragmentById(R.id.team_list));

      Team t = null;
      switch (view.getId()) {
      case R.id.add:
    	EditText input = (EditText) this.findViewById(R.id.name);
    	String name = input.getText().toString();
    	if (name.length() > 0)
    	{
    		// Save the new comment to the database
    		t = datasource.createTeam(name,mSeasonId);
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
