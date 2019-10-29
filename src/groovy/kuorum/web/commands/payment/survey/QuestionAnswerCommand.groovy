package kuorum.web.commands.payment.survey

import grails.validation.Validateable

/**
 * Created by toni on 26/4/17.
 */
@Validateable
class QuestionAnswerCommand{
    Long campaignId
    Long questionId
    List<QuestionAnswerDataCommand> answers = []

    static constraints = {
        campaignId nullable: false
        questionId nullable: false
        answers nullable: false, minSize: 1
    }
}

@Validateable
class QuestionAnswerDataCommand{
    Long answerId;
    String text;

    static constraints = {
        answerId nullable: false
        text nullable: true, maxSize: 500;
    }
}


