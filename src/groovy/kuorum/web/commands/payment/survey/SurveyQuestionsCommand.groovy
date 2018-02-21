package kuorum.web.commands.payment.survey

import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.util.TimeZoneUtil
import kuorum.web.commands.payment.CampaignContentCommand
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

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType


    static validateQuestions = {val, obj ->
        String error = null;
        if (obj.sendType != "DRAFT"){
            if (val.size()<1){
                error = "minSize.error"
            }
            val.each{
                if (!error)
                    error = it.validate()?'':'invalidOptions'
            }
        }
        return error
    }
    static KuorumUser currentUser(){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        SpringSecurityService springSecurityService = (SpringSecurityService)appContext.springSecurityService
        KuorumUser user = springSecurityService.currentUser

        return user
    }

    static constraints = {
        importFrom CampaignContentCommand, include: ["publishOn", "sendType"]
        questions maxSize: 10000, validator: validateQuestions
    }
}

@Validateable
class QuestionCommand{
    Long id
    String text
    QuestionTypeRSDTO questionType
    List<QuestionOptionCommand> options =[new QuestionOptionCommand()]

    static constraints = {
        id nullable: true
        text nullable: false
        questionType nullable: false
        options minSize: 2
    }
}


@Validateable
class QuestionOptionCommand{
    Long id
    String text
    static constraints = {
        text nullable: false
        id nullable: true
    }
}
