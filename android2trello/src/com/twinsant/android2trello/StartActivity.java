package com.twinsant.android2trello;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.Token;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Context;
import android.content.Intent;

public class StartActivity extends FragmentActivity 
	implements LoaderManager.LoaderCallbacks<List<JSONObject>> {
	AndrelloApplication mApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		mApp = (AndrelloApplication)getApplication();
		Token accessToken = mApp.loadAccessToken();
		if (accessToken == null) {
			auth();
		}else{
			getSupportLoaderManager().initLoader(0, null, this);
		}
	}

	private void auth() {
		AuthUrlAsyncTask task = new AuthUrlAsyncTask(this);
		task.execute(mApp);
	}
	
	protected void onActivityResult(int requestCode, int resultCode) {
		if (requestCode == AndrelloApplication.OAUTH_REQUEST) {
			if (resultCode == RESULT_OK) {
				System.out.println("authed");
				getSupportLoaderManager().initLoader(0, null, this);
			}else{
				System.out.println("Denied");
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

	// Loader
	@Override
	public Loader<List<JSONObject>> onCreateLoader(int arg0, Bundle arg1) {
		return new BoardsLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<List<JSONObject>> arg0, List<JSONObject> data) {
		if (data == null) {
			auth();
			return;
		}
		ArrayList<String> board_ids = new ArrayList<String>();
		for (JSONObject json : data) {
			try {
				board_ids.add(json.getString("id"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Intent intent = new Intent(this, BoardsActivity.class);
		intent.putStringArrayListExtra(AndrelloApplication.EXTRA_BOARDS, board_ids);
		startActivity(intent);
	}

	@Override
	public void onLoaderReset(Loader<List<JSONObject>> arg0) {
		System.out.println("onLoaderReset");
	}
}
