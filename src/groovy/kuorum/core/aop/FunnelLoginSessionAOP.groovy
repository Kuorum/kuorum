package kuorum.core.aop

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.users.CookieUUIDService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.annotation.Before
import org.springframework.core.annotation.Order
import org.springframework.security.access.AccessDeniedException

@Aspect
@Order(0)
class FunnelLoginSessionAOP {


    CookieUUIDService cookieUUIDService;
    SpringSecurityService springSecurityService

    @Pointcut("@annotation(kuorum.core.annotations.FunnelLoginSessionValid)")
    public void funnelLoginSessionMethod() {}

    @Around("funnelLoginSessionMethod()")
    public Object checkUserCredentials(final ProceedingJoinPoint pjp) throws Throwable {
        if (cookieUUIDService.isUserUUIDSet() || springSecurityService.isLoggedIn()) {
            return pjp.proceed();
        } else {
            throw new AccessDeniedException("Please follow the authorization flow");
        }

    }
}
