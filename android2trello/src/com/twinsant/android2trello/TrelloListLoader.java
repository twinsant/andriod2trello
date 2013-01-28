package com.twinsant.android2trello;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.AsyncTaskLoader;

public class TrelloListLoader extends AsyncTaskLoader<List<JSONObject>> {
	private AndrelloApplication app;
	private String mListId;

	@Override
	protected void onStartLoading() {
		forceLoad();
	}

	public TrelloListLoader(Context context, String list_id) {
		super(context);
		
		FragmentActivity activity = (FragmentActivity)context;
		app = (AndrelloApplication)activity.getApplication();
		
		mListId = list_id;
	}

	@Override
	public List<JSONObject> loadInBackground() {
		return app.getCards(mListId);
	}
}
