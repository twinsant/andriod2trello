package com.twinsant.android2trello;

import java.net.MalformedURLException;
import java.net.URL;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TrelloOAuthActivity extends Activity {

    private WebView myWebView;
    private OAuthService service;
    private Token requestToken;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        service = new ServiceBuilder()
        .provider(TrelloApi.class)
        .apiKey("e65da061e7d7575e01ac1aa758c4ba86")
        .apiSecret("e4275c46827eebf137a0bb41b5b65af404d58650c8738b8fdb93a55e8d5e1431")
        .callback("http://android2trello.twinsant.com/callback")
        .build();
        
        requestToken = service.getRequestToken();
        
        String authUrl = service.getAuthorizationUrl(requestToken);
        
        myWebView = (WebView) findViewById(R.id.webview);
        final Activity activity = this;
        myWebView.setWebViewClient(new MyWebViewClient(activity));
        myWebView.loadUrl(authUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    final class MyWebViewClient extends WebViewClient {
    	Context mContext;
    	
    	MyWebViewClient(Context c) {
    		mContext = c;
    	}
    	
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		URL urlCallbak;
			try {
				urlCallbak = new URL(url);
	    		String host = urlCallbak.getHost();
	    		if (host.equals("android2trello.twinsant.com")) {
	    			Uri uri=Uri.parse(url);
	    			String oauth_verifier = uri.getQueryParameter("oauth_verifier");
	        		System.out.println(oauth_verifier);
	                Verifier v = new Verifier(oauth_verifier);
	                Token accessToken = service.getAccessToken(requestToken, v);
	                String token = accessToken.getToken();
	                
	                OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.twitter.com/1/account/verify_credentials.xml");
	                service.signRequest(accessToken, request); // the access token from step 4
	                Response response = request.send();
	                System.out.println(response.getBody());
	    			return true;
	    		}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		return false;
    	}
    	
    	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    		Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();
    	}
    	
    	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
    		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    		result.confirm();
    		return true;
    	}
    }
}
