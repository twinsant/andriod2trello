package com.twinsant.android2trello;

import org.scribe.model.Token;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class StartActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		AndrelloApplication app = (AndrelloApplication)getApplication();
		Token accessToken = app.loadAccessToken();
		if (accessToken == null) {
			AuthUrlAsyncTask task = new AuthUrlAsyncTask(this);
			task.execute(app);
		}else{
			Intent intent = new Intent(this, BoardsActivity.class);
			startActivity(intent);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode) {
		if (requestCode == AndrelloApplication.OAUTH_REQUEST) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(this, BoardsActivity.class);
				startActivity(intent);
			}
		}
	}

	class AuthUrlAsyncTask extends AsyncTask<AndrelloApplication, Void, String> {
		private Context mContext;
		
		AuthUrlAsyncTask(Context context) {
			mContext = context;
		}
		@Override
		protected String doInBackground(AndrelloApplication... params) {
			return params[0].getAuthUrl();
		}		
		@Override
		protected void onPostExecute(String result) {
			Intent intent = new Intent(mContext, OAuthActivity.class);
			intent.putExtra(AndrelloApplication.EXTRA_AUTHURL, result);
			startActivityForResult(intent, AndrelloApplication.OAUTH_REQUEST);
		}
	}

}
