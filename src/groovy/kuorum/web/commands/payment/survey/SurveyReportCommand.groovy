package kuorum.web.commands.payment.survey

import grails.validation.Validateable

@Validateable
class SurveyReportCommand {

    Long campaignId;
    SurveyReportType surveyReportType;
    Boolean pdfFormat = Boolean.FALSE

    static constraints = {
        campaignId nullable: false
        surveyReportType nullable: false
        pdfFormat nullable: true
    }
}



enum SurveyReportType {
    SURVEY_RAW_DATA,
    SURVEY_STATS
}