<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="head.logged.account.tools.massMailing.edit" args="[survey.name]"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <r:require modules="datepicker, surveyForms" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>

    <div class="box-steps container-fluid campaign-steps">
        <g:set var="mappings" value="${
            [step:'questions',
            saveAndSentButtons: true,
            next: 'surveyEditContent',
            settings: 'surveyEditSettings',
            questions: 'surveyEditQuestions',
            content: 'surveyEditContent',
            showResult: 'surveyShow' ]}"/>
        <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings]"/>
    </div>

    <div class="box-ppal campaign-new">
        <formUtil:validateForm bean="${command}" form="questionsSurveyForm" dirtyControl="true"/>
        <form action="#"
              class="form-horizontal campaign-form ${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT || status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PAUSE ? 'campaign-published' : ''}"
              id="questionsSurveyForm"
              method="POST"
              data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">

            <g:render template="/survey/questions/questionForm" model="[
                    command: command,
                    status : survey.campaignStatusRSDTO,
                    survey : survey]"/>
            <g:render template="/campaigns/edit/stepButtons" model="[
                    saveAndSentButtons: true,
                    campaign          : survey,
                    mappings          : mappings,
                    status            : status,
                    command           : command,
                    numberRecipients  : numberRecipients]"/>
        </form>
    </div>
</content>