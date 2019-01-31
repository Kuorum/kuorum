package kuorum.web.filters

import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class KuorumSecuritySwitchFilter extends GenericFilterBean  {


    def authenticationManager
//    def interchangeAuthenticationProvider
    def securityMetadataSource
    SpringSecurityService springSecurityService


//    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
//    void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req
//        HttpServletResponse response = (HttpServletResponse) res
//
//
//
//    if (SecurityContextHolder.getContext().getAuthentication() == null) {
//            logger.info("FILTER ");
//            def myAuth = new InterchangeAuthenticationToken()
//            myAuth.setName(1680892)
//            myAuth.setCredentials('SDYLWUYa:nobody::27858cff')
//            myAuth.setPrincipal(1680892)
//            myAuth = authenticationManager.authenticate(myAuth);
//            if (myAuth) {
//                println "Successfully Authenticated ${userId} in object ${myAuth}"
//
//                // Store to SecurityContextHolder
//                SecurityContextHolder.getContext().setAuthentication(myAuth);
//            }
//        }
//        chain.doFilter(request, response)
//    }

    void afterPropertiesSet() {
//        def providers = authenticationManager.providers
//        providers.add(interchangeAuthenticationProvider)
//        authenticationManager.providers = providers
    }

    @Override
    void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = ((HttpServletResponse) res)
        HttpServletRequest request = ((HttpServletRequest) req)

        if (isUserAllowedToSwitchToUser()){
            chain.doFilter(request, response)
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "NOT ALLOWED")
//            TODO: No poner la URL de unauthorized a pelo
//            response.sendRedirect(request.getContextPath()+"/login/denied")
        }
    }

    private boolean isUserAllowedToSwitchToUser(){
        def roles = springSecurityService.getPrincipal().getAuthorities()
        roles.find{"ROLE_SUPER_ADMIN"}
//        roles.find{"ROLE_ADMIN"} || roles.find{"ROLE_ADVANCED"} && user.allowedAdminUsers.find{kuorumUser.id}
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        SecurityContextHolder.getContext().setAuthentication(authResult)
//        rememberMeServices.onLoginSuccess(request, response, authResult)
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        SecurityContextHolder.clearContext()
//        rememberMeServices.loginFail(request, response)
    }
}