import kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 24/03/14.
 */
class UserBioPartsSpec extends Specification {


    @Unroll
    void "test split user bio #bio filling command and recovering bio1 #bio1 and b io 2 #bio2"() {
        given: "The orginal bio"
        when:
        FunnelFillBasicDataCommand command = new FunnelFillBasicDataCommand()
        command.fillBioParts(bio)
        then:
        command.bio == bio1
        command.bio2 == bio2
        where:
        bio                                                                                 | bio1                             | bio2
        "<h5>Titulo 1</h5><p>TEXTO 1</p><br/><h5>titulo 2</h5><p>TEXTO 2</p>"               | "<p>TEXTO 1</p>"                 | "<p>TEXTO 2</p>"
        "<h5>Titulo 1</h5><p>TEXTO 1</p><br/><h5>titulo 2</h5><p>TEXTO 2</p><p>TEXTO 3</p>" | "<p>TEXTO 1</p>"                 | "<p>TEXTO 2</p><p>TEXTO 3</p>"
        "<h5>Titulo 1</h5><p>TEXTO 1</p><br/><h5>titulo 2</h5><p>TEXTO 2</p><p>TEXTO 3</p>" | "<p>TEXTO 1</p>"                 | "<p>TEXTO 2</p><p>TEXTO 3</p>"
        "<p>TEXTO 1</p>"                                                                    | "<p>TEXTO 1</p>"                 | ""
        "<p>TEXTO 1</p><p>TEXTO 1.1</p>"                                                    | "<p>TEXTO 1</p><p>TEXTO 1.1</p>" | ""

    }

}
