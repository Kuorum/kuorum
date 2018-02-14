package kuorum.web.commands.payment.survey

import grails.validation.Validateable
import org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO

/**
 * Created by toni on 26/4/17.
 */


@Validateable
class QuestionAnswerCommand{
    Long campaignId
    Long questionId
    List<Long> answersIds

    static constraints = {
        campaignId nullable: false
        questionId nullable: false
        answersIds nullable: false, minSize: 1
    }
}


