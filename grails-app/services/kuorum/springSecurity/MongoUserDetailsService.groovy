package kuorum.springSecurity

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import kuorum.users.KuorumUser
import org.apache.log4j.Logger
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

//import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
//import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService
//import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
//import org.springframework.security.core.authority.GrantedAuthorityImpl
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.userdetails.UsernameNotFoundException


class MongoUserDetailsService  implements GrailsUserDetailsService {


    private Logger log = Logger.getLogger(getClass())

    /**
     * Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least one role, so
     * we give a user with no granted roles this one which gets past that restriction but
     * doesn't grant anything.
     */
    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]


    @Override
    UserDetails loadUserByUsername(String username, boolean loadRoles) {
        if(log.debugEnabled) {
            log.debug("Logarndose el usuario: $username")
        }
        KuorumUser.withTransaction { status ->

            if (!username) {
                log.warn("Empty username: $username")
                throw new UsernameNotFoundException('Empty username', username)
            }

            log.debug("KuorumUser not found using username: $username")
            KuorumUser user = KuorumUser.findByEmail(username.toLowerCase())

            if (!user) {
                log.warn("KuorumUser not found: $username")
                throw new UsernameNotFoundException('KuorumUser not found', username)
            }

            if(log.debugEnabled) {
                log.debug("KuorumUser found: $username")
            }

            return createUserDetails(user)
        }
    }

    @Override
    UserDetails loadUserByUsername(String username) {
        return loadUserByUsername(username, true)
    }

    protected KuorumUserSession createUserDetails(user, Collection authorities) {
        new KuorumUserSession(user.email, user.password, user.enabled,
                !user.accountExpired, !user.passwordExpired,
                !user.accountLocked, authorities, user.id, user.name)
    }

    UserDetails createUserDetails(KuorumUser user, Boolean loadRoles = Boolean.TRUE){
        def roles = getRoles(user, loadRoles)
        return createUserDetails(user, roles)
    }

    Collection<GrantedAuthority> getRoles(KuorumUser user,Boolean loadRoles = Boolean.TRUE){
        def roles = NO_ROLES
        if (loadRoles) {
            def authorities = user.authorities?.collect {new GrantedAuthorityImpl(it.authority)}
            if(authorities) {
                roles = authorities
            }
        }

        if(log.debugEnabled) {
            log.debug("KuorumUser roles: $roles")
        }
        roles
    }
}

public class KuorumUserSession extends GrailsUser{
    String name
    public KuorumUserSession(String username, String password, boolean enabled, boolean accountNonExpired,
                      boolean credentialsNonExpired, boolean accountNonLocked,
                      Collection<GrantedAuthority> authorities, Object id, String name) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id);
        this.name = name;
    }
}