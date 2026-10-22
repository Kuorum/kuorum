package kuorum.security.rememberMe

import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.rememberme.CookieTheftException
import org.springframework.security.web.authentication.rememberme.InvalidCookieException
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException
import spock.lang.Specification

class GracePeriodRememberMeServicesSpec extends Specification {

    static final String SERIES = "series1"
    static final String CURRENT_TOKEN = "tokenB"
    static final String OLD_TOKEN = "tokenA"

    PersistentTokenRepository tokenRepository = Mock(PersistentTokenRepository)
    UserDetails userDetails = Mock(UserDetails)
    UserDetailsService userDetailsService = Mock(UserDetailsService)
    boolean withinGrace = false
    GracePeriodRememberMeServices services

    def setup() {
        def graceRepository = [isPreviousTokenWithinGracePeriod: { String s, String t -> withinGrace }] as RememberMeTokenRepository
        services = new GracePeriodRememberMeServices()
        services.key = "testKey"
        services.cookieName = "testCookie"
        services.tokenValiditySeconds = 3600
        services.userDetailsService = userDetailsService
        services.tokenRepository = tokenRepository
        services.rememberMeTokenRepository = graceRepository
    }

    private PersistentRememberMeToken storedToken() {
        new PersistentRememberMeToken("user@kuorum.org", SERIES, CURRENT_TOKEN, new Date())
    }

    def "a matching token is rotated, the cookie is set and the user is returned"() {
        given:
        def response = new MockHttpServletResponse()
        tokenRepository.getTokenForSeries(SERIES) >> storedToken()

        when:
        def result = services.processAutoLoginCookie([SERIES, CURRENT_TOKEN] as String[], new MockHttpServletRequest(), response)

        then:
        result.is(userDetails)
        1 * tokenRepository.updateToken(SERIES, { it != CURRENT_TOKEN && it }, _ as Date)
        1 * userDetailsService.loadUserByUsername("user@kuorum.org") >> userDetails
        response.getCookie("testCookie") != null
    }

    def "the previous token within the grace period is tolerated and is not cookie theft"() {
        given:
        withinGrace = true
        tokenRepository.getTokenForSeries(SERIES) >> storedToken()

        when:
        def result = services.processAutoLoginCookie([SERIES, OLD_TOKEN] as String[], new MockHttpServletRequest(), new MockHttpServletResponse())

        then:
        result.is(userDetails)
        0 * tokenRepository.removeUserTokens(_)
        1 * tokenRepository.updateToken(SERIES, _, _)
        1 * userDetailsService.loadUserByUsername("user@kuorum.org") >> userDetails
    }

    def "a mismatching token outside the grace period is cookie theft and removes the series"() {
        given:
        withinGrace = false
        tokenRepository.getTokenForSeries(SERIES) >> storedToken()

        when:
        services.processAutoLoginCookie([SERIES, OLD_TOKEN] as String[], new MockHttpServletRequest(), new MockHttpServletResponse())

        then:
        thrown(CookieTheftException)
        1 * tokenRepository.removeUserTokens("user@kuorum.org")
        0 * tokenRepository.updateToken(*_)
    }

    def "an unknown series throws RememberMeAuthenticationException"() {
        given:
        tokenRepository.getTokenForSeries(SERIES) >> null

        when:
        services.processAutoLoginCookie([SERIES, CURRENT_TOKEN] as String[], new MockHttpServletRequest(), new MockHttpServletResponse())

        then:
        thrown(RememberMeAuthenticationException)
    }

    def "a cookie with the wrong number of tokens throws InvalidCookieException"() {
        when:
        services.processAutoLoginCookie([SERIES] as String[], new MockHttpServletRequest(), new MockHttpServletResponse())

        then:
        thrown(InvalidCookieException)
    }

    def "an expired token throws RememberMeAuthenticationException"() {
        given:
        tokenRepository.getTokenForSeries(SERIES) >> new PersistentRememberMeToken("user@kuorum.org", SERIES, CURRENT_TOKEN, new Date(0))

        when:
        services.processAutoLoginCookie([SERIES, CURRENT_TOKEN] as String[], new MockHttpServletRequest(), new MockHttpServletResponse())

        then:
        thrown(RememberMeAuthenticationException)
    }
}
