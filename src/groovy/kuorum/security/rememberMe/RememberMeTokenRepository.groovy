package kuorum.security.rememberMe

import grails.plugin.springsecurity.web.authentication.rememberme.GormPersistentTokenRepository
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken

/**
 * Created by iduetxe on 15/06/16.
 */
class RememberMeTokenRepository extends GormPersistentTokenRepository{

    // Ventana de gracia para tolerar un reuse del token justo anterior a la ultima rotacion.
    // PersistentTokenBasedRememberMeServices rota el token en cada peticion que usa la cookie
    // remember-me; con varias tareas ECS detras del ALB, peticiones casi simultaneas del
    // navegador (AJAX en paralelo) pueden llegar todas con el valor de cookie previo a que el
    // navegador procese el Set-Cookie de la rotacion. Sin esta tolerancia, Spring Security
    // interpreta ese reuse como robo de cookie (CookieTheftException), invalida toda la serie
    // y fuerza re-login real (visto como bucle de login en zonas IS_AUTHENTICATED_FULLY).
    // 10s cubre con margen cualquier jitter de red/carga de pagina real; sigue siendo corto
    // frente a un ataque real (que tendria que robar la cookie Y usarla en esos 10s exactos
    // tras la ultima rotacion legitima del usuario, una ventana ya muy reducida).
    static final long GRACE_PERIOD_MS = 10_000L

    void updateToken(String series, String tokenValue, Date lastUsed) {
        def clazz = lookupDomainClass()
        if (!clazz) return
        def current = clazz.collection.findOne(['_id': series])
        def previousToken = current?.token
        //NO Transaction. Mongo Gorm is not saving.
//        def persistentLogin = clazz.get(series)
        clazz.collection.update(['_id':series],['$set':[
                token: tokenValue,
                lastUsed: lastUsed,
                previousToken: previousToken,
                previousTokenRotatedAt: lastUsed
        ]])
    }

    // Usado por GracePeriodRememberMeServices: comprueba si presentedToken es el token
    // inmediatamente anterior a la ultima rotacion de esta serie, dentro de la ventana de
    // gracia. Solo tolera UN reuse (el inmediatamente anterior), no cualquier token historico.
    boolean isPreviousTokenWithinGracePeriod(String seriesId, String presentedToken) {
        def clazz = lookupDomainClass()
        if (!clazz) return false
        def doc = clazz.collection.findOne(['_id': seriesId])
        if (!doc?.previousToken || !doc?.previousTokenRotatedAt) return false
        if (doc.previousToken != presentedToken) return false
        long age = System.currentTimeMillis() - ((Date) doc.previousTokenRotatedAt).time
        age >= 0 && age <= GRACE_PERIOD_MS
    }
}
