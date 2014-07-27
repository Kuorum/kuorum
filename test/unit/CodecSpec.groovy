

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 24/03/14.
 */
class CodecSpec extends Specification {

    def setup(){
        String.metaClass.encodeAsHTML={->
            delegate
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
        "un barco http://hola.com con vela"     | "un barco <a href='http://hola.com' target='_blank' rel='nofollow'>http://hola.com</a> con vela"
        "un barco http://hola.com con vela"     | "un barco <a href='http://hola.com' target='_blank' rel='nofollow'>http://hola.com</a> con vela"
        "dos http://urls.com seguidas http://hola.com"     | "dos <a href='http://urls.com' target='_blank' rel='nofollow'>http://urls.com</a> seguidas <a href='http://hola.com' target='_blank' rel='nofollow'>http://hola.com</a>"
        "urls.com"     | "<a href='http://urls.com' target='_blank' rel='nofollow'>http://urls.com</a>"
        "urls.co"     | "<a href='http://urls.co' target='_blank' rel='nofollow'>http://urls.co</a>"
        "urls.es"      | "<a href='http://urls.es' target='_blank' rel='nofollow'>http://urls.es</a>"
        "urls.mobi"      | "<a href='http://urls.mobi' target='_blank' rel='nofollow'>http://urls.mobi</a>"
    }
}
