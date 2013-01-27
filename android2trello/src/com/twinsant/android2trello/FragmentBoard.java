package com.twinsant.android2trello;

import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ArrayAdapter;

public class FragmentBoard extends ListFragment 
		implements LoaderManager.LoaderCallbacks<List<String>> {
	ArrayAdapter<String> mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		setListAdapter(mAdapter);
		
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<String>> onCreateLoader(int arg0, Bundle arg1) {
		return new TrelloBoardLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<String>> arg0, List<String> data) {
		Iterator<String> it = data.iterator();
		while (it.hasNext()) {
			mAdapter.add(it.next());
		}
	}

	@Override
	public void onLoaderReset(Loader<List<String>> arg0) {
		System.out.println("onLoaderReset");
	}

}
