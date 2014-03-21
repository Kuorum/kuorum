package kuorum.mail

import com.ecwid.mailchimp.MailChimpClient
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.helper.Helper
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.context.MessageSource
import spock.lang.Specification

/**
 * Created by iduetxe on 21/03/14.
 */
@TestFor(KuorumMailService)
@Mock([KuorumUser])
class KuorumMailServiceSpec extends Specification {

    IndexSolrService indexSolrService = Mock(IndexSolrService)
    LinkGenerator grailsLinkGenerator = Mock(LinkGenerator)
    MandrillAppService mandrillAppService = Mock(MandrillAppService)
    MailchimpService mailchimpService = Mock(MailchimpService)
    MessageSource messageSource = Mock(MessageSource)

    def setup() {
        service.indexSolrService = indexSolrService
        service.grailsLinkGenerator = grailsLinkGenerator
        service.mandrillAppService = mandrillAppService
        service.mailchimpService = mailchimpService
        service.messageSource = messageSource
    }

    def cleanup() {
    }

    void "test verifing a user"() {
        given: "A user"
        KuorumUser user = Helper.createDefaultUser("email@email.com").save()
        when:"Is verified"
        service.verifyUser(user)
        then:
        1 * indexSolrService.index(user)
        1 * mailchimpService.addSubscriber(user)
        1 * mandrillAppService.sendTemplate({it.mailType == MailType.REGISTER_ACCOUNT_COMPLETED})
    }
}
