package kuorum.security.rememberMe

import org.springframework.dao.DataAccessException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.rememberme.CookieTheftException
import org.springframework.security.web.authentication.rememberme.InvalidCookieException
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Extends PersistentTokenBasedRememberMeServices to tolerate a single reuse of the token
 * immediately preceding the last rotation, within a short grace period.
 *
 * Reason: PersistentTokenBasedRememberMeServices rotates the remember-me token (same series,
 * new token) on every request that uses it. With several ECS tasks behind an ALB, nearly
 * simultaneous requests from a real browser (parallel AJAX calls while a page loads) can all
 * arrive carrying the previous cookie value, before the browser has processed the Set-Cookie of
 * the rotation triggered by the first of them. Without this tolerance, Spring Security reads
 * that legitimate reuse as cookie theft (CookieTheftException), deletes the whole series and
 * forces a real login -- seen in production as a login loop in areas protected with
 * IS_AUTHENTICATED_FULLY (e.g. /sec/admin), reproduced and confirmed by checking in Mongo
 * (persistentLoginToken collection) that the token field of the same series is overwritten
 * between nearly simultaneous requests of the same user session.
 *
 * The rest of the algorithm (cookie decoding, expiration, Authentication construction) is the
 * same as PersistentTokenBasedRememberMeServices; the only addition is that, before throwing
 * CookieTheftException on a mismatch, it checks whether the presented token is the immediately
 * preceding one and we are still within GRACE_PERIOD_MS (see RememberMeTokenRepository).
 * Any other mismatch (older token, outside the window, or non-existent series) follows the
 * normal Spring Security path: it is treated as real cookie theft.
 */
class GracePeriodRememberMeServices extends PersistentTokenBasedRememberMeServices {

    RememberMeTokenRepository rememberMeTokenRepository

    // PersistentTokenBasedRememberMeServices (3.2.x) keeps the repository in a private field
    // with no getter, so we capture it when it is injected.
    private PersistentTokenRepository persistentTokenRepository

    @Override
    void setTokenRepository(PersistentTokenRepository tokenRepository) {
        super.setTokenRepository(tokenRepository)
        this.persistentTokenRepository = tokenRepository
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
                                                  HttpServletResponse response) {

        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain 2 tokens, but contained '" +
                    Arrays.asList(cookieTokens) + "'")
        }

        String presentedSeries = cookieTokens[0]
        String presentedToken = cookieTokens[1]

        PersistentRememberMeToken token = persistentTokenRepository.getTokenForSeries(presentedSeries)

        if (token == null) {
            // No cookie exists for that series, so it is an unknown login
            throw new RememberMeAuthenticationException(
                    "No persistent token found for series id: " + presentedSeries)
        }

        if (!presentedToken.equals(token.tokenValue)) {
            if (rememberMeTokenRepository?.isPreviousTokenWithinGracePeriod(presentedSeries, presentedToken)) {
                logger.debug("Remember-me token reuse within grace period for series '${presentedSeries}', " +
                        "tolerated as a benign race from concurrent requests, not treated as cookie theft.")
                // token already holds the CURRENT value (post-rotation); carry on with the real user.
            } else {
                persistentTokenRepository.removeUserTokens(token.username)
                throw new CookieTheftException(messages.getMessage(
                        "PersistentTokenBasedRememberMeServices.cookieStolen",
                        "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."))
            }
        }

        if (token.date.time + getTokenValiditySeconds() * 1000L < System.currentTimeMillis()) {
            throw new RememberMeAuthenticationException("Remember-me login has expired")
        }

        // Rotate the token like PersistentTokenBasedRememberMeServices does (same series, new
        // token, new Date), so a tolerated reuse does not leave the cookie "frozen" on the old
        // token indefinitely.
        PersistentRememberMeToken newToken = new PersistentRememberMeToken(
                token.username, token.series, generateTokenData(), new Date())
        try {
            persistentTokenRepository.updateToken(newToken.series, newToken.tokenValue, newToken.date)
            setCookie([newToken.series, newToken.tokenValue] as String[], getTokenValiditySeconds(), request, response)
        } catch (DataAccessException e) {
            logger.error("Failed to update token: ", e)
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem")
        }

        try {
            return getUserDetailsService().loadUserByUsername(token.username)
        } catch (UsernameNotFoundException e) {
            throw new RememberMeAuthenticationException("Remember-me login could not be performed; user not found", e)
        }
    }
}
