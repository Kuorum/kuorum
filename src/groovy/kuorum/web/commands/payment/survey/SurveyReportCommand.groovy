package kuorum.web.commands.payment.survey

import grails.validation.Validateable

@Validateable
class SurveyReportCommand {

    Long campaignId;
    SurveyReportType surveyReportType;

    static constraints = {
        campaignId nullable: false
        surveyReportType nullable: false
    }
}



enum SurveyReportType {
    SURVEY_RAW_DATA,
    SURVEY_STATS
}