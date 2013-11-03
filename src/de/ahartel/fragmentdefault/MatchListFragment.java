package de.ahartel.fragmentdefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import de.ahartel.SoccerLite.*;
//import de.ahartel.fragmentdefault.dummy.DummyContent;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MatchListFragment extends ListFragment {
	
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_SEASON_ID = "season_id";
    public static final String ARG_TEAM_ID = "team_id";
	
    // Team list for spinner
    List<Team> teams;
    ArrayAdapter<Team> spinner_adapter;
    
	List<Match> values;
	ArrayAdapter<Match> adapter;
	
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(long id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        
        values = new ArrayList<Match>();

        // TODO: replace with a real list adapter.
        adapter = new ArrayAdapter<Match>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                values);
        setListAdapter(adapter);
    }
    
    public void setValues(List<Match> matches)
    {
    	adapter.addAll(matches);
    }
    
    public void setTeams(List<Team> teams)
    {
    	spinner_adapter.addAll(teams);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.match_list,
          container, false);
      
      // opponent spinner
      teams = new ArrayList<Team>();
      /*
      // Create an ArrayAdapter using the string array and a default spinner layout
      spinner_adapter = new ArrayAdapter<Team>(getActivity(),
  	        android.R.layout.simple_spinner_item,teams);
      // Spinner adapter
      Spinner spinner = (Spinner) view.findViewById(R.id.opponent);
      // Specify the layout to use when the list of choices appears
      spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      // Apply the adapter to the spinner
      spinner.setAdapter(spinner_adapter);
      spinner.setOnItemSelectedListener((MatchListActivity)getActivity());
      
      // Home-Away Switch
      Switch home_switch = (Switch) view.findViewById(R.id.home_away);
      home_switch.setTextOff("Home");
      home_switch.setTextOn("Away");
      home_switch.setOnCheckedChangeListener((MatchListActivity)getActivity());
      */
      return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(adapter.getItem(position).getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu,inflater);
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.main_activity_actions, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
    	MatchListActivity act = (MatchListActivity)getActivity();
        switch (item.getItemId()) {
            case R.id.action_wipe:
                act.drop_recreate_db();
                return true;
            case R.id.action_load:
                act.update_database();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
 
    public void add(Match m)
    {
    	adapter.add(m);
    }
    
    public Match front()
    {
    	return adapter.getItem(0);
    }
    
    public void remove(Match s)
    {
    	adapter.remove(s);
    }
    
    public int getCount()
    {
    	return adapter.getCount();
    }
}
