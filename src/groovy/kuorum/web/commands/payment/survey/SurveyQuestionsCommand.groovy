package kuorum.web.commands.payment.survey

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO
import org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO

/**
 * Created by toni on 26/4/17.
 */

@Validateable
class SurveyQuestionsCommand {

    Long surveyId
    List<QuestionCommand> questions = []

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType


    static validateQuestions = {val, obj ->
        String error = null
        if (obj.sendType != "DRAFT"){
            if (val.size()<1){
                error = "minSize.error"
            }
            val.each{
                if (!error)
                    error = it.validate()?null:'invalidQuestions'
            }
        }
        return error
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

    static validateOptions = {val, obj ->
        String error = null
        val.each{
            if (!error)
                error = it.validate()?null:'invalidOptions'
        }
        return error
    }

    static constraints = {
        id nullable: true
        text nullable: false, blank: false
        questionType nullable: false
        options minSize: 1, validator: validateOptions
    }
}


@Validateable
class QuestionOptionCommand{
    Long id
    String text
    QuestionOptionTypeRDTO questionOptionType = QuestionOptionTypeRDTO.ANSWER_PREDEFINED
    static constraints = {
        text nullable: false, blank: false
        id nullable: true
        questionOptionType nullable: false
    }
}
