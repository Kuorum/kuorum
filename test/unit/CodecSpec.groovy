

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 24/03/14.
 */
class CodecSpec extends Specification {


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
    }
}
