package com.twinsant.android2trello;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.app.Application;
import android.content.SharedPreferences;

public class AndrelloApplication extends Application {	
	private static final String TRELLO_KEY = "e65da061e7d7575e01ac1aa758c4ba86";
	private static final String TRELLO_SECRET = "e4275c46827eebf137a0bb41b5b65af404d58650c8738b8fdb93a55e8d5e1431";
	private static final String TRELLO_CALLBACK = "http://android2trello.twinsant.com/callback";
	
	public static final String EXTRA_AUTHURL = "com.twinsant.android2trello.authUrl";
	public static final Object HOST_CALLBACK = "android2trello.twinsant.com";
	
	private static final String PREFS_NAME = "com.twinsant.android2treelo.settings";
	private static final String PREFS_NAME_SECRET = "com.twinsant.android2treelo.secret"; // static final String could not s1 + "foo"
	private static final String PREFS_NAME_TOKEN = "com.twinsant.android2treelo.token";
	public static final String EXTRA_BOARD_ID = "com.twinsant.android2treelo.boardId";
	
	public static final int OAUTH_REQUEST = 0;
	public static final String TRELLO_HOME = "https://trello.com/home";
	
	private OAuthService trello;
	private Token requestToken;
	
	HashMap<String, List<JSONObject>> cache;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
        trello = new ServiceBuilder()
        	.provider(TrelloApi.class)
        	.apiKey(TRELLO_KEY)
        	.apiSecret(TRELLO_SECRET)
        	.callback(TRELLO_CALLBACK)
        	.build();
        
        cache = new HashMap<String, List<JSONObject>>();
	}

	public String getAuthUrl() {
        requestToken = trello.getRequestToken();     
        String authUrl = trello.getAuthorizationUrl(requestToken);
		return authUrl;
	}

	public Token getAccessToken(String oauth_verifier) {
        Verifier v = new Verifier(oauth_verifier);
        Token accessToken = trello.getAccessToken(requestToken, v);
        return accessToken;
        
        /*            
        String idList = "50f5f5ab08c2e28877008678";
        request = new OAuthRequest(Verb.POST, "https://trello.com/1/cards/?idList=" + idList + "&name=" + 
        URLEncoder.encode("≤‚ ‘") + "&pos=top");
        trello.signRequest(accessToken, request); // the access token from step 4
        response = request.send();
        System.out.println(response.getBody());*/
	}

	public void saveAccessToken(Token accessToken) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PREFS_NAME_SECRET, accessToken.getSecret());
		editor.putString(PREFS_NAME_TOKEN, accessToken.getToken());
		editor.commit();
	}

	public Token loadAccessToken() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String token = settings.getString(PREFS_NAME_TOKEN, null);
		String secret = settings.getString(PREFS_NAME_SECRET, null);
		if (token !=null && secret != null) {
			return new Token(token, secret);
		}
		return null;
	}

	public List<JSONObject> getBoards() {
		if (cache.containsKey("boards"))
			return cache.get("boards");
		
		List<JSONObject> boards = new ArrayList<JSONObject>();
		
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://trello.com/1/members/me?fields=username&boards=open&board_fields=name");
        trello.signRequest(loadAccessToken(), request); // the access token from step 4
        Response response = request.send();
        try {
			JSONObject user = new JSONObject(response.getBody());
			JSONArray boardsArray = user.getJSONArray("boards");
			for (int i=0;i<boardsArray.length();i++) {
				JSONObject board = boardsArray.getJSONObject(i);
				boards.add(board);
			}
			cache.put("boards", boards);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return boards;
	}

	public List<JSONObject> getLists(String board_id) {
		if (cache.containsKey("lists" + board_id))
			return cache.get("lists" + board_id);
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://trello.com/1/boards/" + board_id + "?fields=name&lists=open&list_fields=name,pos");
        trello.signRequest(loadAccessToken(), request); // the access token from step 4
        Response response = request.send();
        try {
        	String body = response.getBody();
        	
			JSONObject trelloList = new JSONObject(body);
			JSONArray listsArray = trelloList.getJSONArray("lists");
			for (int i=0;i<listsArray.length();i++) {
				JSONObject obj = listsArray.getJSONObject(i);
				list.add(obj);
			}
			cache.put("lists" + board_id, list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public List<JSONObject> getCards(String list_id) {
		if (cache.containsKey("cards" + list_id))
			return cache.get("cards" + list_id);
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://trello.com/1/lists/" + list_id + "?fields=name&cards=open&card_fields=name,pos");
        trello.signRequest(loadAccessToken(), request); // the access token from step 4
        Response response = request.send();
        try {
        	String body = response.getBody();
        	
			JSONObject trelloList = new JSONObject(body);
			JSONArray listsArray = trelloList.getJSONArray("cards");
			for (int i=0;i<listsArray.length();i++) {
				JSONObject obj = listsArray.getJSONObject(i);
				list.add(obj);
			}
			cache.put("cards" + list_id, list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public String getBoardId(int position) {
		try {
			return cache.get("boards").get(position).getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
