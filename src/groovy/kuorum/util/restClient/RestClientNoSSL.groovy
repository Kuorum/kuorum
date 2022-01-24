package kuorum.util.restClient

import grails.util.Environment
import groovyx.net.http.RESTClient
import org.apache.http.client.HttpClient
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.HttpParams

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.X509Certificate

class RestClientNoSSL extends RESTClient {

    RestClientNoSSL() {
        super()
    }

    RestClientNoSSL(Object defaultURI) throws URISyntaxException {
        super(defaultURI)
    }

    RestClientNoSSL(Object defaultURI, Object defaultContentType) throws URISyntaxException {
        super(defaultURI, defaultContentType)
    }

    @Override
    protected HttpClient createClient(HttpParams params) {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        SSLContext ssl_ctx = SSLContext.getInstance("TLS");
        TrustManager[] certs = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String t) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String t) {
            }
        };
        ssl_ctx.init(null, certs, new SecureRandom());
        SSLSocketFactory ssf = new SSLSocketFactory(ssl_ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = httpClient.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
        return new DefaultHttpClient(ccm, httpClient.getParams());
    }

}
