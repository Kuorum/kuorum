package kuorum.core.security.passwordEncoders

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser
import kuorum.users.KuorumUser
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails

import javax.naming.AuthenticationException

/**
 * Extracted from web
 * @link http://burtbeckwith.com/blog/?p=2017
 */
class PasswordFixingDaoAuthenticationProvider extends DaoAuthenticationProvider {

    def grailsApplication

    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        super.additionalAuthenticationChecks userDetails, authentication

        // if we got this far the password was ok

        String oldHashedPassword = userDetails.getPassword()
        if (oldHashedPassword.startsWith('$2a$10$') &&
                oldHashedPassword.length() == 60) {
            // already bcrypt
            return
        }

        if (oldHashedPassword.length() != 64) {
            // TODO
            return
        }

        String bcryptPassword = getPasswordEncoder().encodePassword(authentication.credentials, null)


        //UPDATE PASSWORD TO BCRYPT
        KuorumUser.withNewSession {
            KuorumUser.collection.update([_id:userDetails.id],['$set':[password:bcryptPassword]])
        }
    }
}