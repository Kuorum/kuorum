<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="head.logged.account.tools.massMailing.edit" args="[campaign.name]"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <r:require modules="datepicker, forms" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>

    <div class="box-steps container-fluid choose-campaign">
        <g:set var="mappings" value="${
            [step:'questions',
             next:'surveyEditContent',
             settings:'surveyEditSettings',
             questions:'surveyEditQuestions',
             content:'surveyEditContent',
             showResult: 'surveyShow']}"/>
        <g:render template="/campaigns/steps/questionSteps" model="[mappings: mappings]"/>
    </div>

    <div class="box-ppal campaign-new">
        <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>

        <formUtil:validateForm bean="${command}" form="questionsSurveyForm" dirtyControl="true"/>
        <form action="#" class="form-horizontal" id="questionsSurveyForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
            <input type="hidden" name="redirectLink" id="redirectLink"/>
            <input type="hidden" name="surveyId" value="${command.surveyId}"/>
            <formUtil:dynamicComplexInputs
                    command="${command}"
                    field="questions"
                    listClassName="kuorum.web.commands.payment.survey.QuestionCommand"
                    cssParentContainer="profile-dynamic-fields"
                    customRemoveButton="true"
                    formId="questionsSurveyForm">
                <fieldset class="row">
                    <formUtil:input field="id" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                    <div class="form-group col-md-5">
                        <formUtil:input field="test" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                    </div>
                    <div class="form-group col-md-5">
                        <formUtil:selectEnum field="questionType" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                    </div>
                    <div class="form-group col-md-1 form-group-remove">
                        <button type="button" class="btn btn-transparent removeButton"><i class="fa fa-trash"></i></button>
                    </div>
                </fieldset>
            </formUtil:dynamicComplexInputs>
        </form>
    </div>
</content>