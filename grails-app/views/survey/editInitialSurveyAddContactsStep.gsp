<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="domain.config.starting.steps.step3.header" args="[survey.name]"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="disableLogoLink" value="true"/>
    <!-- Schema.org markup for Google+ -->
    <r:require modules="datepicker, surveyForms"/>
</head>

<content tag="mainContent">
    <div class="box-steps container-fluid campaign-steps">
        <g:set var="mappings" value="${
            [ step: 'questions',
            saveAndSentButtons: true,
            next: 'surveyEditContent',
            settings: 'surveyEditSettings',
            questions: 'surveyEditQuestions',
            content: 'surveyEditContent',
            showResult: 'surveyShow' ]}"/>
        <ol class="stepsSign stepsSignSecondaryColor">
            <li class="active">
                <div class="step-label"><g:message code="domain.config.starting.steps.step1"/></div>
            </li>
            <li class="active">
                <div class="step-label"><g:message code="domain.config.starting.steps.step2"/></div>
            </li>
            <li class="active">
                <div class="step-label"><g:message code="domain.config.starting.steps.step3"/></div>
            </li>
        </ol>
    </div>

    <div class="col-md-8">
        <div class="box-ppal campaign-new">
            <h1><g:message code="domain.config.starting.steps.step3.title"/></h1>
            <formUtil:validateForm bean="${command}" form="surveyInitAddContactsForm" dirtyControl="true"/>
            <form action="#"
                  class="campaign-form ${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT || status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PAUSE ? 'campaign-published' : ''}"
                  id="surveyInitAddContactsForm"
                  method="POST"
                  data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">

                <g:render template="/contacts/addContactsForm" model="[command: command]"/>
                <fieldset class="buttons">
                    <div class="text-right">
                        <ul class="form-final-options">
                            <li>
                                <g:link mapping="politicianCampaigns" class="btn btn-grey-light">
                                    <g:message code="default.paginate.skip"/>
                                </g:link>
                            </li>
                            <li class="left">
                                <g:link mapping="surveyInitDomainEditQuestions" params="[campaignId: survey.id]"
                                        class="btn btn-grey inverted">
                                    <g:message code="default.paginate.back"/>
                                </g:link>
                            </li>
                            <li>
                                <a href="#" class="btn btn-blue inverted" id="next"
                                   data-redirectLink="${mappings.next}">
                                    <g:message code="tools.massMailing.next"/>
                                </a>
                            </li>
                        </ul>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>

    <div class="col-md-4">
        <div class="comment-box call-to-action inverted">
            <div class="comment-header">
                <span class="call-title">
                    <g:message code="domain.config.starting.steps.step3.explanation.title"/>
                </span>
            </div>

            <div class="comment-body">
                <g:message code="domain.config.starting.steps.step3.explanation.text1"/>
            </div>
        </div>
    </div>
</content>