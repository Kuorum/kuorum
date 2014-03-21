package kuorum.mail

import com.microtripit.mandrillapp.lutung.view.MandrillMessage
import grails.test.mixin.TestFor
import kuorum.helper.Helper
import kuorum.users.KuorumUser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MandrillAppService)
class MandrillAppServiceSpec extends Specification {

    @Shared
    MailData mailData = new MailData()
    def setup() {
        mailData = new MailData()
        mailData.mailType = MailType.NOTIFICATION_CLUCK
        mailData.fromName = "From name"
        mailData.globalBindings = [gvar1:"gvar1",  gvar2:"gvar2"]
        mailData.userBindings = (1..3).collect{
            KuorumUser user = Helper.createDefaultUser("email$it@email.com")
            new MailUserData(user:user, bindings: [var1:"var1",  var2:"var2"])
        }
    }

    def cleanup() {
    }

    @Unroll
    void "test createRecipients with #numUsers"() {
        given:"A mailData"
        if (numUsers){
            mailData.userBindings = (1..numUsers).collect{
                KuorumUser user = Helper.createDefaultUser("test$it@email.com")
                def bindings = numBindings?(1..numBindings).collectEntries{["var$it":"var$it"]}:[:]
                new MailUserData(user:user, bindings: bindings)
            }
        }else{
            mailData.userBindings = []
        }
        when: "Creates the recipients"
        List<MandrillMessage.Recipient> recipients= service.createRecipients(mailData)
        then:""
        recipients.size() == numUsers
        if (numUsers){
            recipients[0].email =="test1@email.com"
            recipients[0].name =="test1"
            recipients[0].type ==MandrillMessage.Recipient.Type.TO
        }
        where:""
        numUsers | numBindings
        1        | 2
        0        | 2
        3        | 2
    }

    @Unroll
    void "test createMergeVars with #numUsers users and #mailType "() {
        given:"A mailData"
        mailData.mailType = mailType
        if (numUsers){
            mailData.userBindings = (1..numUsers).collect{
                KuorumUser user = Helper.createDefaultUser("test$it@email.com")
                def bindings = mailType.requiredBindings?mailType.requiredBindings.collectEntries{["testVar$it":"testVar$it"]}:[:]
                new MailUserData(user:user, bindings: bindings)
            }
        }else{
            mailData.userBindings = []
        }
        when: "Creates the recipients"
        List<MandrillMessage.MergeVarBucket> vars= service.createMergeVars(mailData)
        then:""
        vars.size() == numUsers
        if (numUsers){
            MandrillMessage.MergeVarBucket var = vars.first()
            var.rcpt = "test0@email.com"
            var.vars.size() == mailType.requiredBindings.size()
            !mailType.requiredBindings.find{binding -> var.vars.find{it.name==binding}}.find{it == false}
        }
        where:""
        numUsers | mailType
        1        | MailType.REGISTER_VERIFY_EMAIL
        4        | MailType.REGISTER_VERIFY_EMAIL
        2        | MailType.NOTIFICATION_CLUCK
        0        | MailType.NOTIFICATION_CLUCK
    }

    @Unroll
    void "test createGlobalMergeVars with #mailType "() {
        given:"A global bindings"
        mailData.mailType = mailType
        when: "Creates the recipients"
        List<MandrillMessage.MergeVar>  vars= service.createGlobalMergeVars(mailData)
        then:""
        vars.size() == mailType.globalBindings.size()
        !mailType.globalBindings.find{binding -> vars.find{it.name==binding}}.find{it == false}

        where:""
        numUsers | mailType
        1        | MailType.REGISTER_VERIFY_EMAIL
        1        | MailType.NOTIFICATION_CLUCK

    }
}
