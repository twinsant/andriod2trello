package com.twinsant.android2trello;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BoardsFragment extends ListFragment  {
	AndrelloApplication app;
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
		
		app = (AndrelloApplication)getActivity().getApplication();
        
        Intent intent = getActivity().getIntent();
        List<String> data = intent.getStringArrayListExtra(AndrelloApplication.EXTRA_BOARDS);
		for (String board_id : data) {
			String name = app.getBoardName(board_id);
			mAdapter.add(name);
		}
		
		getFragmentManager().popBackStack();
	}
}
