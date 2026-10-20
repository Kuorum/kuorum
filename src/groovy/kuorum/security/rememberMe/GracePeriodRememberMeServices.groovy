package kuorum.security.rememberMe

import org.springframework.dao.DataAccessException
import org.springframework.security.authentication.RememberMeAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.rememberme.CookieTheftException
import org.springframework.security.web.authentication.rememberme.InvalidCookieException
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Extiende PersistentTokenBasedRememberMeServices para tolerar un unico reuse del token
 * inmediatamente anterior a la ultima rotacion, dentro de una ventana de gracia corta.
 *
 * Motivo: PersistentTokenBasedRememberMeServices rota el token remember-me (serie fija,
 * token nuevo) en cada peticion que lo usa. Con varias tareas ECS detras de un ALB, peticiones
 * casi simultaneas del navegador real (AJAX en paralelo durante la carga de una pagina) pueden
 * llegar todas con el valor de cookie previo a que el navegador procese el Set-Cookie de la
 * rotacion disparada por la primera de ellas. Sin esta tolerancia, Spring Security interpreta
 * ese reuse legitimo como robo de cookie (CookieTheftException), borra toda la serie y fuerza
 * un login real -- visto en produccion como un bucle de login en zonas protegidas con
 * IS_AUTHENTICATED_FULLY (p.ej. /sec/admin), reproducido y confirmado verificando en Mongo
 * (coleccion persistentLoginToken) como el campo token de la misma serie se sobreescribe entre
 * peticiones casi simultaneas de la misma sesion de usuario.
 *
 * El resto del algoritmo (decodificacion de cookie, expiracion, construccion de la
 * Authentication) es el mismo que PersistentTokenBasedRememberMeServices; solo se añade, antes
 * de lanzar CookieTheftException por mismatch, una comprobacion de si el token presentado es el
 * inmediatamente anterior y seguimos dentro de GRACE_PERIOD_MS (ver RememberMeTokenRepository).
 * Cualquier otro mismatch (token mas antiguo, fuera de ventana, o serie inexistente) sigue el
 * camino normal de Spring Security: se considera robo de cookie real.
 */
class GracePeriodRememberMeServices extends PersistentTokenBasedRememberMeServices {

    RememberMeTokenRepository rememberMeTokenRepository

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
                                                  HttpServletResponse response) {

        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain 2 tokens, but contained '" +
                    Arrays.asList(cookieTokens) + "'")
        }

        String presentedSeries = cookieTokens[0]
        String presentedToken = cookieTokens[1]

        PersistentRememberMeToken token = getTokenRepository().getTokenForSeries(presentedSeries)

        if (token == null) {
            // No existe cookie para esa serie, es un login desconocido
            throw new RememberMeAuthenticationException(
                    "No persistent token found for series id: " + presentedSeries)
        }

        if (!presentedToken.equals(token.tokenValue)) {
            if (rememberMeTokenRepository?.isPreviousTokenWithinGracePeriod(presentedSeries, presentedToken)) {
                logger.debug("Remember-me token reuse within grace period for series '${presentedSeries}', " +
                        "tolerated as a benign race from concurrent requests, not treated as cookie theft.")
                // token ya apunta al valor ACTUAL (post-rotacion); seguimos con el usuario real.
            } else {
                getTokenRepository().removeUserTokens(token.username)
                throw new CookieTheftException(messages.getMessage(
                        "PersistentTokenBasedRememberMeServices.cookieStolen",
                        "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."))
            }
        }

        if (token.date.time + getTokenValiditySeconds() * 1000L < System.currentTimeMillis()) {
            throw new RememberMeAuthenticationException("Remember-me login has expired")
        }

        // Rota el token igual que el flujo normal (nueva Date, mismo series), para que el
        // reuse tolerado no deje la cookie "congelada" en el token viejo indefinidamente.
        refreshToken(token)

        try {
            return getUserDetailsService().loadUserByUsername(token.username)
        } catch (UsernameNotFoundException e) {
            throw new RememberMeAuthenticationException("Remember-me login could not be performed; user not found", e)
        }
    }
}
