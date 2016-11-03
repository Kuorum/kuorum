package kuorum.payment.contact.outlook.oauth

import com.fasterxml.jackson.databind.ObjectMapper
import grails.converters.JSON
import kuorum.payment.contact.outlook.model.TokenResponse
import org.scribe.builder.api.DefaultApi20
import org.scribe.extractors.AccessTokenExtractor
import org.scribe.model.OAuthConfig
import org.scribe.model.OAuthConstants
import org.scribe.model.OAuthRequest
import org.scribe.model.Response
import org.scribe.model.Token
import org.scribe.model.Verb
import org.scribe.model.Verifier
import org.scribe.oauth.OAuth20ServiceImpl
import org.scribe.oauth.OAuthService
import org.scribe.utils.Preconditions
import org.springframework.web.util.UriComponentsBuilder

class OutlookApi extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize";

	@Override
	public String getAccessTokenEndpoint() {
		return "https://login.microsoftonline.com/common/oauth2/v2.0/token"
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig oAuthConfig) {
		UUID state = UUID.randomUUID();
		UUID nonce = UUID.randomUUID();

		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(
				AUTHORIZE_URL
		);
		urlBuilder.queryParam(OAuthConstants.CLIENT_ID, oAuthConfig.apiKey);
		urlBuilder.queryParam(OAuthConstants.REDIRECT_URI, oAuthConfig.callback);
		urlBuilder.queryParam("response_type", "code id_token");
		urlBuilder.queryParam(OAuthConstants.SCOPE, oAuthConfig.scope);
		urlBuilder.queryParam("state", state);
		urlBuilder.queryParam("nonce", nonce);
		urlBuilder.queryParam("response_mode", "form_post");

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
			request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
			request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
			request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
			request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
			request.addBodyParameter(GRANT_TYPE, GRANT_TYPE_AUTHORIZATION_CODE);
			Response response = request.send();
			return api.getAccessTokenExtractor().extract(response.getBody());
		}
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		new AccessTokenExtractor() {
			public Token extract(String response) {
				Preconditions.checkEmptyString(response, "Response body is incorrect. Can\'t extract a token from an empty string");
				def responseData = JSON.parse(response);
				//def mapper = new ObjectMapper();
				//return  mapper.readValue(response, TokenResponse.class);
				return new Token((String) responseData.access_token, "", response);
			}
		}
	}

}
