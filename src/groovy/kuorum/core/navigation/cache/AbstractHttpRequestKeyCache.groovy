package kuorum.core.navigation.cache


import javax.servlet.http.HttpServletRequest

abstract class AbstractHttpRequestKeyCache implements ServletRequestResponseCache {

    PathKeyExtractor pathKeyExtractor = new PathKeyExtractor()

    String buildKey(HttpServletRequest request) {
        return buildKey(request, null)
    }

    String buildKey(HttpServletRequest request, Locale locale) {
        String key = request.getLocalAddr() + request.getContextPath()
        key += pathKeyExtractor.extractKey(request.getServletPath())
        if (locale) {
            key += locale.getLanguage()
        }
        return key
    }
}
