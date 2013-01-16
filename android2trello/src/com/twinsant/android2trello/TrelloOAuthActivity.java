package com.twinsant.android2trello;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TrelloOAuthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        OAuthService service = new ServiceBuilder()
        .provider(TrelloApi.class)
        .apiKey("e65da061e7d7575e01ac1aa758c4ba86")
        .apiSecret("e4275c46827eebf137a0bb41b5b65af404d58650c8738b8fdb93a55e8d5e1431")
        .build();
        
        Token requestToken = service.getRequestToken();
        
        String authUrl = service.getAuthorizationUrl(requestToken);
        
        /*Verifier v = new Verifier("verifier you got from the user");
        Token accessToken = service.getAccessToken(requestToken, v); // the requestToken you had from step 2
        
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.twitter.com/1/account/verify_credentials.xml");
        service.signRequest(accessToken, request); // the access token from step 4
        Response response = request.send();
        System.out.println(response.getBody());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
