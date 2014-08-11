import org.codehaus.groovy.grails.plugins.codecs.HTMLEncoder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 24/03/14.
 */
class CodecSpec extends Specification {

    def setup(){
        String.metaClass.encodeAsHTML={->
            new HTMLEncoder().encode(delegate)
        }
    }

    @Unroll
    void "test KuorumUrlCodec #orgString == #tranformedString"() {
        given:"The kuorumCodec"
        when:
        def res = KuorumUrlCodec.encode(orgString)
        then:
        res == tranformedString
        where:
        orgString           | tranformedString
        "holá pepé"         | "hola-pepe"
        ""                  | ""
        "aâäáeéëê iÍÏï#%/"  |"aaaaeeee-iiii"
        "Aa Ha ÄÁa"         |"aa-ha-aaa"
    }

    @Unroll
    void "test HtmlLinksCodec #orgString == #tranformedString"() {
        given:"The kuorumCodec"
        when:
        def res = HtmlLinksCodec.encode(orgString)
        then:
        res == tranformedString
        where:
        orgString                               | tranformedString
        "http://hola.com"                       | "<a href='http://hola.com' target='_blank' rel='nofollow'>http://hola.com</a>"
        "https://hola.com"                      | "<a href='https://hola.com' target='_blank' rel='nofollow'>https://hola.com</a>"
        "httpss://hola.xwzNoExiste"             | "httpss://hola.xwzNoExiste"
        "un barco http://hola.com con vela"     | "un barco <a href='http://hola.com' target='_blank' rel='nofollow'>http://hola.com</a> con vela"
        "un barco http://hola.com con vela"     | "un barco <a href='http://hola.com' target='_blank' rel='nofollow'>http://hola.com</a> con vela"
        "dos http://urls.com seguidas http://hola.com"     | "dos <a href='http://urls.com' target='_blank' rel='nofollow'>http://urls.com</a> seguidas <a href='http://hola.com' target='_blank' rel='nofollow'>http://hola.com</a>"
        "urls.com"                              | "<a href='http://urls.com' target='_blank' rel='nofollow'>urls.com</a>"
        "urls.co"                               | "<a href='http://urls.co' target='_blank' rel='nofollow'>urls.co</a>"
        "urls.es"                               | "<a href='http://urls.es' target='_blank' rel='nofollow'>urls.es</a>"
        "urls.mobi"                             | "<a href='http://urls.mobi' target='_blank' rel='nofollow'>urls.mobi</a>"
        "http://hola.com/kk/hola"               | "<a href='http://hola.com/kk/hola' target='_blank' rel='nofollow'>http://hola.com/kk/hola</a>"
        "hola.com/kk/hola"                      | "<a href='http://hola.com/kk/hola' target='_blank' rel='nofollow'>hola.com/kk/hola</a>"
        "hola.com/kk/hola?hh=23"                | "<a href='http://hola.com/kk/hola?hh=23' target='_blank' rel='nofollow'>hola.com/kk/hola?hh=23</a>"
        "hola. com/kk/hola"                     | "hola. com/kk/hola"
        ".com"                                  | ".com"
        "Estos:\nhttp://elpais.com/elpais/2014/06/23/media/1403547645_646044.html" | "Estos:\n<a href='http://elpais.com/elpais/2014/06/23/media/1403547645_646044.html' target='_blank' rel='nofollow'>http://elpais.com/elpais/2014/06/23/media/1403547645_646044.html</a>"
        "Estos:\rhttp://elpais.com/elpais/2014/06/23/media/1403547645_646044.html" | "Estos:\r<a href='http://elpais.com/elpais/2014/06/23/media/1403547645_646044.html' target='_blank' rel='nofollow'>http://elpais.com/elpais/2014/06/23/media/1403547645_646044.html</a>"
    }
}
