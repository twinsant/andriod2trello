package com.twinsant.android2trello;

import java.net.MalformedURLException;
import java.net.URL;

import org.scribe.model.Token;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TrelloOAuthActivity extends Activity {
	private AndrelloApplication app;
    private WebView myWebView;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        app = (AndrelloApplication)getApplication();
        
        Intent intent = getIntent();
        String authUrl = intent.getStringExtra(AndrelloApplication.EXTRA_AUTHURL);
        
        myWebView = (WebView) findViewById(R.id.webview);
        final Activity activity = this;
        myWebView.setWebViewClient(new MyWebViewClient(activity));
        myWebView.loadUrl(authUrl);
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
	    		if (host.equals(AndrelloApplication.HOST_CALLBACK)) {
	    			Uri uri=Uri.parse(url);
	    			String oauth_verifier = uri.getQueryParameter("oauth_verifier");
	    			Token accessToken = app.getAccessToken(oauth_verifier);
	    			app.saveAccessToken(accessToken);
	                
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
