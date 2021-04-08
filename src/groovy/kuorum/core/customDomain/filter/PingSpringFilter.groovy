package kuorum.core.customDomain.filter

import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

class PingSpringFilter extends GenericFilterBean {

    void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("Ping alive")
        response.setStatus(HttpServletResponse.SC_OK);
    }
}