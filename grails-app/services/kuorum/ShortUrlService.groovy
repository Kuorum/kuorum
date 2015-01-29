package kuorum

import grails.transaction.Transactional
import grails.util.Environment
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import kuorum.project.Project
import kuorum.post.Post
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.beans.factory.annotation.Value

@Transactional
class ShortUrlService {

    private static final String OWLY_SERVICE= "http://ow.ly"
    private static final String OWLY_SHORTEN_URL= "/api/1.1/url/shorten"

    @Value('${shortUrl.owly.apykey}')
    String OWLY_APY_KEY = 'XXXXXX'

    LinkGenerator grailsLinkGenerator

    URL shortUrl(URL longUrl) {
        URL shortUrl

        def http = new HTTPBuilder()

        http.request( OWLY_SERVICE, Method.GET, ContentType.JSON) { req ->
            uri.path = OWLY_SHORTEN_URL
            uri.query = [apiKey:OWLY_APY_KEY,longUrl:longUrl]
            headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
            headers.Accept = 'application/json'

            response.success = { resp, reader ->
                assert resp.statusLine.statusCode == 200
                shortUrl = new URL(reader.results.shortUrl)
            }

            response.'404' = {
                println 'Not found'
            }
        }
        log.info("URL corta para ${longUrl} : ${shortUrl}")
        shortUrl
    }

    // OW.LY hasn't testing key
    private URL checkEnviromentShortUrl(String link){
        Environment.executeForCurrentEnvironment {
            development{
                return new URL("http://ow.ly/3jLTrs")
            }
            test{
                return new URL("http://ow.ly/3jLTrs")
            }
            production{
                return shortUrl(new URL(link))
            }

        }



    }

    URL shortUrl(Post post) {
        String link = grailsLinkGenerator.link(mapping: 'postShow', params:post.encodeAsLinkProperties(), absolute: true)
        checkEnviromentShortUrl(link)
    }

    URL shortUrl(Project project) {
        String link = grailsLinkGenerator.link(mapping: 'projectShow', params:project.encodeAsLinkProperties(), absolute: true)
        checkEnviromentShortUrl(link)
    }

}
