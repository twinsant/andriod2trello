package com.twinsant.android2trello;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


public class ListsPagerActivity extends FragmentActivity {
	private AndrelloApplication app;
	
	private ListPagerAdapter mAdapter;
	private ViewPager mPager;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_lists_pager);
		
		app = (AndrelloApplication)getApplication();
		
		Intent intent = getIntent();
        String board_id = intent.getStringExtra(AndrelloApplication.EXTRA_BOARD_ID);      
		ListsAsyncTask task = new ListsAsyncTask();
		task.execute(board_id);
	}
	
	class ListsAsyncTask extends AsyncTask<String, Void, List<JSONObject>> {
		@Override
		protected List<JSONObject> doInBackground(String... params) {
			return app.getLists(params[0]);
		}
		@Override
		protected void onPostExecute(List<JSONObject> result) {
			mAdapter = new ListPagerAdapter(getSupportFragmentManager(), result);
			mPager = (ViewPager)findViewById(R.id.pager);
			mPager.setAdapter(mAdapter);
		}
	}
	
	public static class ListPagerAdapter extends FragmentPagerAdapter {
		List<JSONObject> mLists;
		SparseArray<ListFragment>mPages;
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			mPages.remove(position);
		}
		
		ListPagerAdapter(FragmentManager fm, List<JSONObject> result) {
			super(fm);
			mLists = result;
			mPages = new SparseArray<ListFragment>();
		}
		@Override
		public int getCount() {
			return mLists.size();
		}
		@Override
		public Fragment getItem(int position) {
			JSONObject list = mLists.get(position);
			String id = "Loading...";
			try {
				id = list.getString("id");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String name = "Loading...";
			try {
				name = list.getString("name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mPages.get(position) != null) {
				return mPages.get(position);
			}else{
				ListFragment fragment = CardsListFragment.newInstance(id, name);
				mPages.put(position, fragment);
				return fragment;
			}
		}
	}
	
	public static class CardsListFragment extends ListFragment
		implements LoaderManager.LoaderCallbacks<List<JSONObject>>, AddCardDialogFragment.AddCardDialogListener{
		
		ArrayAdapter<String> mAdapter;
		String mId;
		String mName;
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
			setListAdapter(mAdapter);
			
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			Bundle args = getArguments();
			if (args != null) {
				mId = args.getString("id");
				mName = args.getString("name");
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_pager_list, container, false);
			TextView textView = (TextView)view.findViewById(R.id.text);
			textView.setText(mName);
			
			Button button = (Button)view.findViewById(R.id.add_card);
			button.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					AddCardDialogFragment newFragment = new AddCardDialogFragment();
					newFragment.setListener(CardsListFragment.this);
					newFragment.show(getFragmentManager(), "addCard");
				}
			});
			
			final CardsListFragment fragment = this;
			button = (Button)view.findViewById(R.id.refresh_list);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AndrelloApplication app = (AndrelloApplication)fragment.getActivity().getApplicationContext();
					app.clear_list_cache(mId);
					
					fragment.getLoaderManager().restartLoader(0, null, fragment);
				}
			});
			return view;
		}

		static CardsListFragment newInstance(String id, String name) {
			CardsListFragment f = new CardsListFragment();
			Bundle args = new Bundle();
			args.putString("id", id);
			args.putString("name", name);
			f.setArguments(args);
			
			return f;
		}

		@Override
		public Loader<List<JSONObject>> onCreateLoader(int arg0, Bundle arg1) {
			return new ListsLoader(getActivity(), mId);
		}

		@Override
		public void onLoadFinished(Loader<List<JSONObject>> loader,
				List<JSONObject> result) {
			mAdapter.clear();
			
			Iterator<JSONObject> it = result.iterator();
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
			System.out.println("reset");		
		}
		
		@Override
		public void onAddCard(String text) {
			AddCardAsyncTask task = new AddCardAsyncTask(getActivity());
			task.execute(mId, text);
		}
		
		class AddCardAsyncTask extends AsyncTask<String, Void, JSONObject> {
			@Override
			protected void onPostExecute(JSONObject result) {
				ListsPagerActivity activity = (ListsPagerActivity)mContext;
				int position = activity.mPager.getCurrentItem();
				CardsListFragment fragment = (CardsListFragment)activity.mAdapter.getItem(position);
				fragment.getLoaderManager().restartLoader(0, null, fragment);
				super.onPostExecute(result);
			}
			Context mContext;
			AddCardAsyncTask(Context context) {
				mContext = context;
			}
			@Override
			protected JSONObject doInBackground(String... params) {
				String listId = params[0];
				String text = params[1];
				
				AndrelloApplication app = (AndrelloApplication)mContext.getApplicationContext();
				
				return app.addCard(listId, text);
			}
			
		}
	}
}
