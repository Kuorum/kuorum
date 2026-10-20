/*
 * Copyright 2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kuorum.web.users
/**
 * Token for remember me stored on mongo for a clustered service
 */
class PersistentLoginToken implements Serializable {

    String id
    String username
    String token
    Date lastUsed

    // Token anterior a la ultima rotacion + cuando se roto. Usado por RememberMeTokenRepository
    // para tolerar un breve reuse del token viejo (peticiones concurrentes del navegador que
    // llegan con la cookie aun no actualizada), sin perder la deteccion real de robo de cookie
    // (reuse fuera de la ventana de gracia, o reuse del token anterior a ese). Ver GRACE_PERIOD_MS
    // en RememberMeTokenRepository.
    String previousToken
    Date previousTokenRotatedAt

    static constraints = {
        username maxSize: 64
        token maxSize: 64
        id maxSize: 64
        previousToken nullable: true, maxSize: 64
        previousTokenRotatedAt nullable: true
    }

    static transients = ['series']

    void setSeries(String series) { id = series }
    String getSeries() { id }

    static mapping = {
//        table 'persistent_logins'
        id column: 'series', generator: 'assigned'
        version false
    }
}
