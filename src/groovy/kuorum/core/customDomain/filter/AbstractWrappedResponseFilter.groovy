package kuorum.core.customDomain.filter

import kuorum.core.navigation.cache.CacheHttpServletResponseWrapper
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

abstract class AbstractWrappedResponseFilter extends GenericFilterBean{

    protected CacheHttpServletResponseWrapper wrapResponse(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper((HttpServletResponse) response)
        filterChain.doFilter(request, cacheResponse)
        cacheResponse.flushBuffer()
        return cacheResponse
    }

}
