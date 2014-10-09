package com.commandapps.helloworldmap.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.commandapps.helloworldmap.OfficeLocationAdapter;
import com.commandapps.helloworldmap.OfficeLocationsLoader;
import com.commandapps.helloworldmap.model.DummyContent;
import com.commandapps.helloworldmap.model.OfficeLocation;

import java.util.ArrayList;
import java.util.List;


public class OfficeLocationFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<OfficeLocation>> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OfficeLocationAdapter adapter;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static OfficeLocationFragment newInstance(String param1, String param2) {
        OfficeLocationFragment fragment = new OfficeLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this).forceLoad();

    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OfficeLocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        adapter  = new OfficeLocationAdapter(getActivity(), 0, new ArrayList<OfficeLocation>());
        setListAdapter(adapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    @Override
    public Loader<List<OfficeLocation>> onCreateLoader(int i, Bundle bundle) {
        return new OfficeLocationsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<OfficeLocation>> listLoader, List<OfficeLocation> officeLocations) {
        adapter.clear();
        adapter.addAll(officeLocations);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<OfficeLocation>> objectLoader) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
