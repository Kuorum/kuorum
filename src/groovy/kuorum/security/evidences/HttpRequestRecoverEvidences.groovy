package kuorum.security.evidences

import org.apache.http.HttpRequest

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class HttpRequestRecoverEvidences implements Evidences{
    HttpServletRequest httpRequest;

    public HttpRequestRecoverEvidences(HttpServletRequestWrapper httpServletRequestWrapper){
        this.httpRequest = httpServletRequestWrapper;
    }

    String getIp(){
        String xForwardedForHeader = httpRequest.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return httpRequest.getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }

    String getBrowser(){
        return httpRequest.getHeader("user-agent");
    }
}
