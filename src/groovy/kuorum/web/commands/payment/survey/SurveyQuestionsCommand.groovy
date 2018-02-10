package kuorum.web.commands.payment.survey

import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.util.TimeZoneUtil
import kuorum.web.constants.WebConstants
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO

/**
 * Created by toni on 26/4/17.
 */

@Validateable
class SurveyQuestionsCommand {

    Long surveyId
    List<QuestionCommand> questions = [];

    static KuorumUser currentUser(){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        SpringSecurityService springSecurityService = (SpringSecurityService)appContext.springSecurityService
        KuorumUser user = springSecurityService.currentUser

        return user
    }

    static constraints = {
        questions maxSize: 10000;
    }
}

@Validateable
class QuestionCommand{
    Long id
    String text
    QuestionTypeRSDTO questionType
    List<QuestionOptionCommand> options =[]

    static constraints = {
        text nullable: false
        questionType nullable: false
    }
}


@Validateable
class QuestionOptionCommand{
    Long id
    String text
    static constraints = {
        text nullable: false
    }
}
