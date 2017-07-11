package kuorum.payment.contact.facebook.oauth

import grails.converters.JSON
import org.apache.commons.codec.binary.Base64
import org.scribe.builder.api.DefaultApi20
import org.scribe.builder.api.FacebookApi
import org.scribe.extractors.AccessTokenExtractor
import org.scribe.model.*
import org.scribe.oauth.OAuth20ServiceImpl
import org.scribe.oauth.OAuthService
import org.scribe.utils.OAuthEncoder
import org.scribe.utils.Preconditions
import org.springframework.web.util.UriComponentsBuilder

//class FacebookApiOauth2 extends FacebookApi {
class FacebookApiOauth2 extends DefaultApi20 {


	private static final String AUTHORIZE_URL = "https://www.facebook.com/v2.9/dialog/oauth?client_id=%s&redirect_uri=%s";
	private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

	@Override
	public String getAccessTokenEndpoint()
	{
		return "https://graph.facebook.com/oauth/access_token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config)
	{
		Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");

		// Append scope if present
		if(config.hasScope())
		{
			return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
		}
		else
		{
			return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
		}
	}

	@Override
	AccessTokenExtractor getAccessTokenExtractor() {
		new AccessTokenExtractor() {
			public Token extract(String response) {
				Preconditions.checkEmptyString(response, "Response body is incorrect. Can\'t extract a token from an empty string");
				def responseData = JSON.parse(response)
				return new Token((String) responseData.access_token,"", response);
			}
		}
	}

}
