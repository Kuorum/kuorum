package kuorum.payment.contact.kuorumApi.oauth

import grails.converters.JSON
import org.scribe.builder.api.DefaultApi20
import org.scribe.extractors.AccessTokenExtractor
import org.scribe.model.OAuthConfig
import org.scribe.model.Token
import org.scribe.utils.Preconditions

/**
 * TODO: THIS CLASS IS AN EMPTY CLASS TO LOAD OAUTH PLUGIN -> It will be used in the future
 */
class KuorumApiOauth2 extends DefaultApi20 {


	private static final String AUTHORIZE_URL = "https://API-KUORUM.ORG/v2.9/dialog/oauth?client_id=%s&redirect_uri=%s"

	@Override
    String getAccessTokenEndpoint()
	{
		return "NO DEFINED"
    }

	@Override
    String getAuthorizationUrl(OAuthConfig config)
	{
		return "NO CONFIGURED"
	}

	@Override
	AccessTokenExtractor getAccessTokenExtractor() {
		new AccessTokenExtractor() {
			Token extract(String response) {
				Preconditions.checkEmptyString(response, "Response body is incorrect. Can\'t extract a token from an empty string")
                def responseData = JSON.parse(response)
				return new Token((String) responseData.access_token,"", response)
            }
		}
	}

}
