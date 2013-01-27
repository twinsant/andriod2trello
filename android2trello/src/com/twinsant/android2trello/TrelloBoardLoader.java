package com.twinsant.android2trello;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.AsyncTaskLoader;

public class TrelloBoardLoader extends AsyncTaskLoader<List<String>> {
	private AndrelloApplication app;

	@Override
	protected void onForceLoad() {
		super.onForceLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
	}

	public TrelloBoardLoader(Context context) {
		super(context);
		
		FragmentActivity activity = (FragmentActivity)context;
		app = (AndrelloApplication)activity.getApplication();
	}

	@Override
	public List<String> loadInBackground() {
		return app.getBoards();
	}

}
