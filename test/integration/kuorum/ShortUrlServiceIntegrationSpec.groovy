package kuorum

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created with IntelliJ IDEA.
 * KuorumUser: iduetxe
 * Date: 8/12/13
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
class ShortUrlServiceIntegrationSpec extends Specification{

    def shortUrlService

    def setup(){
    }


    @Unroll
    void "test shorting urls: #longUrl -> #shortUrl"() {
        given: "Long url ..."
        URL url = new URL(longUrl)
        URL shortUrl = shortUrlService.shortUrl(url)
        expect: "KuorumUser details"
        shortUrl.toString()== expectedShortUrl
        where: "Urls..."
        longUrl || expectedShortUrl
        "http://www.hootsuite.com" || "http://ow.ly/3jLTrs"
    }
}
