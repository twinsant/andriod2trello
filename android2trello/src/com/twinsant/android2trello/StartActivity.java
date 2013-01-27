package com.twinsant.android2trello;

import org.scribe.model.Token;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class StartActivity extends Activity {
	private AndrelloApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		app = (AndrelloApplication)getApplication();
		
		Token accessToken = app.loadAccessToken();
		if (accessToken == null) {
			AuthUrlAsyncTask task = new AuthUrlAsyncTask(this);
			task.execute();
		}else{
			// Start list activity
			Intent intent = new Intent(this, TrelloBoardActivity.class);
			startActivity(intent);
		}
	}

	class AuthUrlAsyncTask extends AsyncTask<Void, Void, String> {
		private StartActivity _activity;
		
		AuthUrlAsyncTask(StartActivity activity) {
			_activity = activity;
		}
		@Override
		protected String doInBackground(Void... params) {
			String authUrl = _activity.app.getAuthUrl();
			return authUrl;
		}		
		@Override
		protected void onPostExecute(String result) {
			Intent intent = new Intent(_activity, TrelloOAuthActivity.class);
			intent.putExtra(AndrelloApplication.EXTRA_AUTHURL, result);
			startActivity(intent);
		}
	}

}
