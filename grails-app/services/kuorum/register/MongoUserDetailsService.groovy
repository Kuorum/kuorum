package kuorum.register

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import org.apache.log4j.Logger
import org.bson.types.ObjectId
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

class MongoUserDetailsService  implements GrailsUserDetailsService {


    KuorumUserService kuorumUserService

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
        if (!username) {
            log.warn("Empty username: $username")
            throw new UsernameNotFoundException('Empty username', username)
        }

        log.debug("KuorumUser not found using username: $username")
        KuorumUser user = KuorumUser.findByEmailAndDomain(username.toLowerCase(), CustomDomainResolver.domain)
        KuorumUserRSDTO userRSDTO = kuorumUserService.findUserRSDTO(user.id.toString())

        if (!user) {
            log.warn("KuorumUser not found: $username")
            throw new UsernameNotFoundException('KuorumUser not found', username)
        }

        if(log.debugEnabled) {
            log.debug("KuorumUser found: $username")
        }

        return createUserDetails(userRSDTO)
    }

    @Override
    UserDetails loadUserByUsername(String username) {
        return loadUserByUsername(username, true)
    }

    protected KuorumUserSession createUserDetails(KuorumUserRSDTO userRSDTO, Collection authorities) {
        KuorumUser user = KuorumUser.get(new ObjectId(userRSDTO.id))
        new KuorumUserSession(
                userRSDTO.alias,
                userRSDTO.email,
                user.password,
                userRSDTO.active,
                true,
                true,
                true,
                authorities,
                new ObjectId(userRSDTO.id),
                userRSDTO.name,
                userRSDTO.timeZone,
                userRSDTO.avatarUrl,
                AvailableLanguage.valueOf(userRSDTO.language.toString()))
    }

    UserDetails createUserDetails(KuorumUserRSDTO user, Boolean loadRoles = Boolean.TRUE){
        Collection<GrantedAuthority> roles = getRoles(user, loadRoles)
        return createUserDetails(user, roles)
    }

    Collection<GrantedAuthority> getRoles(KuorumUserRSDTO userRSDTO,Boolean loadRoles = Boolean.TRUE){
        def roles = NO_ROLES
        if (loadRoles) {
            KuorumUser user = KuorumUser.get(new ObjectId(userRSDTO.id))
            def authorities = userRSDTO.roles?.collect {new SimpleGrantedAuthority(it.toString())}
            if (user.authorities.find{it.authority == "ROLE_INCOMPLETE_USER"}){
                authorities.add(new SimpleGrantedAuthority('ROLE_INCOMPLETE_USER'))
            }
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

class KuorumUserSession extends GrailsUser{
    String name
    String alias
    String regionName
    TimeZone timeZone
    String avatarUrl
    AvailableLanguage language

    String getEmail(){
        username
    }

    KuorumUserSession(String alias, String username, String password, boolean enabled, boolean accountNonExpired,
                      boolean credentialsNonExpired, boolean accountNonLocked,
                      Collection<GrantedAuthority> authorities, Object id, String name, TimeZone timeZone, String avatarUrl, AvailableLanguage language) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id)
        this.name = name
        this.alias = alias
        this.timeZone = timeZone
        this.avatarUrl = avatarUrl
        this.language = language
    }


    @Override
    String toString() {
        return "KuorumUserSession{" +
                "alias='" + alias + '\'' +
                '}'
    }
}