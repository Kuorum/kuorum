package kuorum.web.commands.payment.survey

import grails.validation.Validateable
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO
import org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO

/**
 * Created by toni on 26/4/17.
 */
@Validateable
class QuestionAnswerCommand{
    Long campaignId
    Long questionId
    List<QuestionAnswerDataCommand> answers = []
    QuestionTypeRSDTO questionType

    static constraints = {
        campaignId nullable: false
        questionId nullable: false
        questionType nullable: false
        answers nullable: false, minSize: 1
    }
}

@Validateable
class QuestionAnswerDataCommand{
    Long answerId;
    QuestionOptionTypeRDTO questionOptionType;

    /* Main text field */
    String text;

    /* Secondary text field (phone -> prefixPhone)*/
    String text2;


    /* Filled if it is a date */
    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date date
    /* Filled if it is a number */
    Double number

    static constraints = {
        answerId nullable: false
        questionOptionType nullable: false
        text nullable: true, maxSize: 500;
        text2 nullable: true, maxSize: 500;
        date nullable: true
        number nullable: true
    }
}


