package com.twinsant.android2trello;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.AsyncTaskLoader;

public class TrelloBoardLoader extends AsyncTaskLoader<List<JSONObject>> {
	private AndrelloApplication app;

	@Override
	protected void onStartLoading() {
		forceLoad();
	}

	public TrelloBoardLoader(Context context) {
		super(context);
		
		FragmentActivity activity = (FragmentActivity)context;
		app = (AndrelloApplication)activity.getApplication();
	}

	@Override
	public List<JSONObject> loadInBackground() {
		return app.getBoards();
	}

}
