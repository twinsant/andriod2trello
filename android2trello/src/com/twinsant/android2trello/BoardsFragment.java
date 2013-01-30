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

public class BoardsFragment extends ListFragment 
		implements LoaderManager.LoaderCallbacks<List<JSONObject>> {
	ArrayAdapter<String> mAdapter;
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), ListsPagerActivity.class);
		AndrelloApplication app = (AndrelloApplication)getActivity().getApplication();
		intent.putExtra(AndrelloApplication.EXTRA_BOARD_ID, app.getBoardId(position));
		startActivity(intent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		setListAdapter(mAdapter);
		
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<JSONObject>> onCreateLoader(int arg0, Bundle arg1) {
		return new BoardsLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<JSONObject>> arg0, List<JSONObject> data) {		
		Iterator<JSONObject> it = data.iterator();
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
