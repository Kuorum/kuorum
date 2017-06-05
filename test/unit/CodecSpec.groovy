import kuorum.postalCodeHandlers.YoutubeNameCodec
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
        "-1-2-3"            |"1-2-3"
        "---1-2-3"          |"1-2-3"
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
        "pongo www.rae.es\nUn" | "pongo <a href='http://www.rae.es' target='_blank' rel='nofollow'>www.rae.es</a>\nUn"
    }

    @Unroll
    void "test deleting script tag #orgString == #tranformedString"() {
        given:"The kuorumCodec"
        when:
        def res = RemovingScriptTagsCodec.encode(orgString)
        then:
        res == tranformedString
        where:
        orgString                                            | tranformedString
        "hol <script> alert('') </script>"                   | "hol  alert('') "
        "hol <ScripT> alert('') </script>"                   | "hol  alert('') "
        "hol <SCRIPT> alert('') </scrip>"                    | "hol  alert('') </scrip>"
        "hol <SCRIPT src=''/>"                               | "hol "
        "hol <SCRIPT src='http://jsChungo.com/js'/>"         | "hol "
        "hol <SCRIPT> alert('') </scripT> <B>BLACK</b>"      | "hol  alert('')  <B>BLACK</b>"

    }

    @Unroll
    void "Test youtubeName encoder [youtubeUrl: #youtubeUrl]"(){
        given:"The youtubeNameCodec"
        when:
        String ytbCode = YoutubeNameCodec.decode(youtubeUrl)
        String ytbUrl = YoutubeNameCodec.encode(youtubeCode)
        then:
        resultEmpty?ytbCode =="":ytbCode == youtubeCode
        resultEmpty?ytbUrl == "":ytbUrl == "https://www.youtube.com/watch?v=$youtubeCode"
        where:
        youtubeUrl                                      | youtubeCode   | resultEmpty
        "https://youtu.be/5fTsCcUD8Kg"                  | "5fTsCcUD8Kg" | false
        "http://youtu.be/5fTsCcUD8Kg"                   | "5fTsCcUD8Kg" | false
        "http://youtu.be/5fTsC-UD8Kg"                   | "5fTsC-UD8Kg" | false
        "http://youtube/5fTsC-UD8Kg"                    | ""            | true
        "https://www.youtube.com/watch?v=5fTsCcUD8Kg"   | "5fTsCcUD8Kg" | false
        "https://youtube.com/watch?v=5fTsCcUD8Kg"       | "5fTsCcUD8Kg" | false
        "http://www.youtube.com/watch?v=5fTsCcUD8Kg"    | "5fTsCcUD8Kg" | false
        "http://youtube.com/watch?v=5fTsCcUD8Kg"        | "5fTsCcUD8Kg" | false
        "http://youtube.com/watch v=5fTsCcUD8Kg"        | ""            | true
        ""                                              | ""            | true
        "kkafuti"                                       | ""            | true
    }

    @Unroll
    void "Test twitter encoder [twitterString: #twitterString]"(){
        given:"The twitterString"
        when:
        String twitterNickname = TwitterCodec.encode(twitterString)
        String twitterUrl = TwitterCodec.decode(twitterString)
        then:
        twitterNickname == twitter
        twitterUrl == url
        where:
        twitterString                       | twitter     | url
        "@nick-Name_23"                     | "@nick-name_23" | "https://twitter.com/nick-name_23"
        "nick-Name_23"                      | "@nick-name_23" | "https://twitter.com/nick-name_23"
        "http://twitter/nick-Name_23"       | "@nick-name_23" | "https://twitter.com/nick-name_23"
        "http://twitter/nick-Name_23 ?pp=kk"| "@nick-name_23" | "https://twitter.com/nick-name_23"
        "https://twitter/nick-Name_23"      | "@nick-name_23" | "https://twitter.com/nick-name_23"
        "https://twitter/nick-Name_23 "     | "@nick-name_23" | "https://twitter.com/nick-name_23"
        "http://XXXX/nick-Name_23"          | "@nick-name_23" | "https://twitter.com/nick-name_23"
        "https://XXXX/nick-Name_23"         | "@nick-name_23" | "https://twitter.com/nick-name_23"
        "httpNOT_VALID_URL/nick-Name_23 "   | "@nick-name_23" | "https://twitter.com/nick-name_23"
        " https://twitter.com/nick-Name_23" | "@nick-name_23" | "https://twitter.com/nick-name_23" //El primer espacio no es un espacio
        "    "                              | ""              | ""
    }


    @Unroll
    void "test TargetBlanck #orgString == #tranformedString"() {
        given:"The kuorumCodec"
        when:
        def res = TargetBlankCodec.encode(orgString)
        then:
        res == tranformedString
        where:
        orgString                               | tranformedString
        "<a href='http://hola.com'>hola</a>"    | "<a href='http://hola.com' target='_blank' rel='nofollow'>hola</a>"
        "<a href=\"http://hola.com\">hola</a>"  | "<a href='http://hola.com' target='_blank' rel='nofollow'>hola</a>"
        "<a href=\"http://hola.com\" target='_blank'>hola</a>"  | "<a href='http://hola.com' target='_blank' rel='nofollow'>hola</a>"
        "<a href=\"http://hola.com\" target='_blank' rel='nofollow'>hola</a>"  | "<a href='http://hola.com' target='_blank' rel='nofollow'>hola</a>"
        "Nada"                                  | "Nada"
        "<p>Nada</p>"                           | "<p>Nada</p>"
    }
}
