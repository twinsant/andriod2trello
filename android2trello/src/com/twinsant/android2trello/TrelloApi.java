package com.twinsant.android2trello;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class TrelloApi extends DefaultApi10a
{
	private static final String REQUEST_TOKEN_URL = "https://trello.com/1/OAuthGetRequestToken";
	private static final String ACCESS_TOKEN_URL = "https://trello.com/1/OAuthGetAccessToken";
	private static final String AUTHORIZE_URL = "https://trello.com/1/OAuthAuthorizeToken?oauth_token=%s";

	@Override
	public String getRequestTokenEndpoint()
	{
		return REQUEST_TOKEN_URL;
	}

	@Override
	public String getAccessTokenEndpoint()
	{
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken)
	{
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}
}