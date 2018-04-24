package kuorum.security.permission

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.project.Project
import kuorum.users.KuorumUser
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication

/**
 * Created by iduetxe on 15/02/16.
 */
class KuorumPermissionEvaluator implements PermissionEvaluator {

    public static final String EDIT_GRANT="edit"
    public static final String REMOVE_GRANT="edit"

    def grailsApplication
    SpringSecurityService springSecurityService


    public boolean hasPermission(Authentication authentication, KuorumUser editedUser, Object permission) {
        def loggedUser = springSecurityService.getCurrentUser();
        return  editedUser == loggedUser ||
                SpringSecurityUtils.ifAnyGranted("ROLE_SUPER_ADMIN")

    }

    @Override
    public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
        if (domainObject.class == kuorum.users.KuorumUser){
            return hasPermission(authentication, (KuorumUser) domainObject, permission)
        }
        return false
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // get domain class with name targetType
        Class domainClass = grailsApplication.getDomainClass(targetType).clazz

        if (domainClass == kuorum.users.KuorumUser){
            KuorumUser user = domainClass.get(targetId)
            if (!user){
                //Using alias which is the other possible id
                user = KuorumUser.findByAliasAndDomain(targetId.toLowerCase(), CustomDomainResolver.domain)
            }
            return hasPermission(authentication, user, permission)
        }else if (domainClass == kuorum.project.Project){
            Project project = domainClass.get(targetId)
            return hasPermission(authentication, project, permission)
        }
        return false;
    }
}