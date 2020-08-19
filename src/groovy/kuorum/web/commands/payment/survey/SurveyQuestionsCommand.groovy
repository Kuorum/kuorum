package kuorum.web.commands.payment.survey

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO
import org.kuorum.rest.model.communication.survey.QuestionRSDTO
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
                if (!error && it)
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
    QuestionLimitAnswersType questionLimitAnswersType
    List<QuestionOptionCommand> options =[new QuestionOptionCommand(),new QuestionOptionCommand()]
    Integer maxAnswers = 1;
    Integer minAnswers = 1;

    static validateOptions = {val, obj ->
        String error = null
        if (obj.questionType != QuestionTypeRSDTO.TEXT_OPTION){
            val.each{
                if (!error && it)
                    error = it.validate()?null:'invalidOptions'
            }
        }
        return error
    }

    static validateMaxAnswer = {val, obj ->
        Integer numOptions = obj.options.size()
        List<QuestionLimitAnswersType> checkMaxValidation = [QuestionLimitAnswersType.RANGE, QuestionLimitAnswersType.FORCE, QuestionLimitAnswersType.MAX]
        if (checkMaxValidation.contains(obj.questionLimitAnswersType) && val > numOptions){
            return "invalidMax"
        }
        return null
    }
    static validateMinAnswer = {val, obj ->
        Integer numOptions = obj.options.size()
        List<QuestionLimitAnswersType> checkMaxValidation = [QuestionLimitAnswersType.RANGE, QuestionLimitAnswersType.MIN]
        if (checkMaxValidation.contains(obj.questionLimitAnswersType) && val > numOptions ){
            return "invalidMin"
        }
        if (checkMaxValidation.contains(obj.questionLimitAnswersType) && val > obj.maxAnswers ){
            return "invalidMinGTMax"
        }
        return null
    }

    static constraints = {
        id nullable: true
        text nullable: false, blank: false
        questionType nullable: false
        options minSize: 2, validator: validateOptions
        maxAnswers min:0, validator: validateMaxAnswer
        minAnswers min:1, validator: validateMinAnswer
        questionLimitAnswersType nullable: false
    }
}

enum QuestionLimitAnswersType{
    MIN,MAX,RANGE,FORCE

    public static QuestionLimitAnswersType inferType(QuestionRSDTO questionRSDTO){
        if (questionRSDTO.maxAnswers == questionRSDTO.minAnswers){
            return QuestionLimitAnswersType.FORCE
        }else if (questionRSDTO.minAnswers != 1 && questionRSDTO.maxAnswers != 0 ){
            return QuestionLimitAnswersType.RANGE
        }else if (questionRSDTO.minAnswers == 1 && questionRSDTO.maxAnswers != 0 ){
            return QuestionLimitAnswersType.MAX
        }else{
            return QuestionLimitAnswersType.MIN
        }
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
