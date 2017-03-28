package kuorum.payment.contact.facebook.oauth

import grails.converters.JSON
import org.apache.commons.codec.binary.Base64
import org.scribe.builder.api.DefaultApi20
import org.scribe.builder.api.FacebookApi
import org.scribe.extractors.AccessTokenExtractor
import org.scribe.model.*
import org.scribe.oauth.OAuth20ServiceImpl
import org.scribe.oauth.OAuthService
import org.scribe.utils.Preconditions
import org.springframework.web.util.UriComponentsBuilder

class FacebookApiOauth2 extends FacebookApi {


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
