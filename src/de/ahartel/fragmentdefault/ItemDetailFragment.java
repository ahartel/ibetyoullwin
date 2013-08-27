package de.ahartel.fragmentdefault;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.ahartel.SoccerLite.Season;
import de.ahartel.SoccerLite.SeasonDataSource;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Season mSeason;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        return rootView;
    }

    @Override
    public void onStart()
    {
    	super.onStart();
    	// Show the dummy content as text in a TextView.
    	if (mSeason != null) {
    		((TextView) getActivity().findViewById(R.id.item_detail)).setText(mSeason.getName());
    	}
    	else {
    		((TextView) getActivity().findViewById(R.id.item_detail)).setText("define me!");
    	}
    }
    
    public void setSeason(Season s) {
    	Log.i("ItemDetailFragment","setSeason called.");
    	mSeason = s;
    }
}
