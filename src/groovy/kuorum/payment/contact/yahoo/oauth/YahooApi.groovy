package kuorum.payment.contact.yahoo.oauth

import grails.converters.JSON
import org.scribe.builder.api.DefaultApi20
import org.scribe.extractors.AccessTokenExtractor
import org.scribe.model.*
import org.scribe.oauth.OAuth20ServiceImpl
import org.scribe.oauth.OAuthService
import org.scribe.utils.Preconditions
import org.springframework.web.util.UriComponentsBuilder

class YahooApi extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://api.login.yahoo.com/oauth2/request_auth";
	@Override
	String getAccessTokenEndpoint() {
		return "https://api.login.yahoo.com/oauth2/get_token"
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	String getAuthorizationUrl(OAuthConfig oAuthConfig) {

		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(
				AUTHORIZE_URL
		);
		urlBuilder.queryParam(OAuthConstants.CLIENT_ID, oAuthConfig.apiKey);
		urlBuilder.queryParam(OAuthConstants.REDIRECT_URI, oAuthConfig.callback);
		urlBuilder.queryParam("response_type", "code");
//		urlBuilder.queryParam(OAuthConstants.SCOPE, oAuthConfig.scope);
		return urlBuilder.build().toUriString();
	}

	@Override
	public OAuthService createService(OAuthConfig config) {
		return new OutlookOAuth2Service(this, config);
	}

	private class OutlookOAuth2Service extends OAuth20ServiceImpl {

		private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
		private static final String GRANT_TYPE = "grant_type";

		private DefaultApi20 api;
		private OAuthConfig config;

		public OutlookOAuth2Service(DefaultApi20 api, OAuthConfig config) {
			super(api, config);
			this.api = api;
			this.config = config;
		}

		@Override
		public Token getAccessToken(Token requestToken, Verifier verifier) {
			OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
//			OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), "http://198cca75.ngrok.io");
			request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
			request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
//			request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
//			request.addBodyParameter(OAuthConstants.REDIRECT_URI, URLEncoder.encode("http://local.kuorum.org:8080/kuorum/oauth/yahoo/callback", "UTF-8"));
			request.addBodyParameter(OAuthConstants.REDIRECT_URI, "http://local.kuorum.org/kuorum/oauth/yahoo/callback");
			request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
			request.addBodyParameter(GRANT_TYPE, GRANT_TYPE_AUTHORIZATION_CODE);

			byte[] encodedBytes = org.apache.commons.codec.binary.Base64.encodeBase64("${config.getApiKey()}:${config.getApiSecret()}".getBytes("UTF-8"));
//			request.addBodyParameter("Authorization: Basic", new String(encodedBytes));
			request.addHeader("Authorization", "Basic " + new String(encodedBytes));


			Response response = request.send();
			return api.getAccessTokenExtractor().extract(response.getBody());
		}
	}

	@Override
	AccessTokenExtractor getAccessTokenExtractor() {
		new AccessTokenExtractor() {
			public Token extract(String response) {
				Preconditions.checkEmptyString(response, "Response body is incorrect. Can\'t extract a token from an empty string");
				def responseData = JSON.parse(response)
				return new Token((String) responseData.access_token, responseData.xoauth_yahoo_guid, response);
			}
		}
	}

}
