package com.twinsant.android2trello;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentBoard extends ListFragment 
		implements LoaderManager.LoaderCallbacks<List<JSONObject>> {
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), FragmentPagerBoard.class);
		try {
			intent.putExtra(AndrelloApplication.EXTRA_BOARD_ID, mData.get(position).getString("id"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startActivity(intent);
	}

	ArrayAdapter<String> mAdapter;
	List<JSONObject> mData;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		setListAdapter(mAdapter);
		
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<JSONObject>> onCreateLoader(int arg0, Bundle arg1) {
		return new TrelloBoardLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<JSONObject>> arg0, List<JSONObject> data) {
		mData = data;
		
		Iterator<JSONObject> it = mData.iterator();
		while (it.hasNext()) {
			JSONObject board = it.next();
			String name = "Loading...";
			try {
				name = board.getString("name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mAdapter.add(name);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<JSONObject>> arg0) {
		// TODO
	}

}
