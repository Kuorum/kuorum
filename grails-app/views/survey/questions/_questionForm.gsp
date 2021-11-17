<%@ page import="kuorum.web.commands.payment.survey.QuestionOptionCommand" %>
<formUtil:validateForm bean="${command}" form="questionsSurveyForm" dirtyControl="true"/>
<form action="#"
      class="form-horizontal campaign-form ${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT || status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PAUSE ? 'campaign-published' : ''}"
      id="questionsSurveyForm"
      method="POST"
      data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType"
           value="${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT ? 'SEND' : 'DRAFT'}"
           id="sendMassMailingType"/>
    <input type="hidden" name="surveyId" value="${command.surveyId}"/>

    <formUtil:dynamicComplexInputs
            command="${command}"
            field="questions"
            listClassName="kuorum.web.commands.payment.survey.QuestionCommand"
            cssParentContainer="quesiton-dynamic-fields"
            customRemoveButton="true"
            customAddButton="true"
            appendLast="true"
            formId="questionsSurveyForm">
        <fieldset class="row question-data">
            <formUtil:input cssClass="hidden" field="id" command="${listCommand}" prefixFieldName="${prefixField}"/>
            <div class="form-group">
                <label for="text" class="col-sm-2 col-md-1 control-label"><g:message
                        code="kuorum.web.commands.payment.survey.QuestionCommand.text.label"/>:</label>

                <div class="col-xs-12 col-sm-10">
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-8 col-md-7 no-padding">
                            <formUtil:input field="text" command="${listCommand}" prefixFieldName="${prefixField}"/>
                        </div>

                        <div class="col-xs-10 col-sm-3 col-md-3 question-type">
                            <span class="question-type-noEditable">${message(code: "org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO.${listCommand.questionType}")}</span>
                            <formUtil:selectEnum field="questionType" command="${listCommand}"
                                                 prefixFieldName="${prefixField}" showLabel="false"/>
                        </div>

                        <div class="hidden-xs hidden-sm col-md-1 question-type-info">
                            <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top"
                                  title="XX"></span>
                        </div>

                        <div class="col-xs-1 col-sm-1 col-md-1 no-label-lg">
                            <button type="button" class="btn btn-transparent btn-lg btn-icon removeButton"><i
                                    class="fal fa-trash"></i></button>
                        </div>
                    </div>

                    <div class="form-group question-data-extra">
                        <div class="col-xs-12 no-padding question-data-extra-multi-limit">
                            <div class="col-xs-4 col-md-2 no-padding question-data-extra-multi-limit-type">
                                <formUtil:selectEnum field="questionLimitAnswersType" command="${listCommand}"
                                                     prefixFieldName="${prefixField}" showLabel="true"/>
                            </div>

                            <div class="col-xs-4 col-md-3 question-data-exta-multi-limit-min">
                                <formUtil:input field="minAnswers" command="${listCommand}"
                                                prefixFieldName="${prefixField}" showLabel="true"/>
                            </div>

                            <div class="col-xs-4 col-md-3 question-data-exta-multi-limit-max">
                                <formUtil:input field="maxAnswers" command="${listCommand}"
                                                prefixFieldName="${prefixField}" showLabel="true"/>
                            </div>
                        </div>

                        <div class="col-xs-12 no-padding question-data-extra-multi-points">
                            <div class="col-xs-4 col-md-2 no-padding">
                                <formUtil:input field="points" command="${listCommand}" prefixFieldName="${prefixField}"
                                                showLabel="true"/>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </fieldset>

        <fieldset class="row question-options">
            <div class="form-group">
                <label for="text" class="col-xs-12"><g:message code="kuorum.web.commands.payment.survey.QuestionCommand.options.label"/>:</label>
                <label for="text" class="col-xs-12 hidden-xs">
                    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-1 col-sm-4">
                        <g:message code="kuorum.web.commands.payment.survey.QuestionOptionCommand.text.label"/>
                    </div>
                    <div class="col-xs-1 center">
                        <g:message code="kuorum.core.FileType.IMAGE"/>
                    </div>
                    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-0 col-sm-2">
                        <g:message code="org.kuorum.rest.model.communication.survey.QuestionOptionTypeRDTO.label"/>
                    </div>
                    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-0 col-sm-3 question-logic">
                        <g:message code="kuorum.web.commands.payment.survey.QuestionOptionCommand.nextQuestionId.label"/>
                    </div>
                </label>
                <div class="questionOption-template" style="display: none">
                    <g:render template="/survey/questions/questionOptionFields" model="[survey:survey,prefixField:'template-'+prefixField, pos:0, option:new QuestionOptionCommand()]"/>
                </div>
                <div class="col-xs-12 questionOption clearfix">
                    <g:set var="i" value="${0}"/>
                    <g:each in="${listCommand.options}" var="option">
                        <g:render template="/survey/questions/questionOptionFields" model="[survey:survey,prefixField:prefixField, pos:i, option:option]"/>
                        <g:set var="i" value="${i+1}"/>
                    </g:each>
                    <g:if test="${!listCommand.options}">
                        <g:render template="/survey/questions/questionOptionFields" model="[survey:survey,prefixField:prefixField, pos:i, option:new kuorum.web.commands.payment.survey.QuestionOptionCommand()]"/>
                    </g:if>
                </div>
                <div class="col-xs-12 col-sm-offset-2 col-sm-10 questionOptionActions">
                    <fieldset class="row">
                        <div class="col-xs-offset-1 col-xs-11">
                            <button type="button" class="btn btn-lg btn-icon btn-transparent addQuestionOptionButton">
                                <g:message code="survey.form.button.addOption"/>
                                <i class="far fa-plus"></i>
                            </button>
                        </div>
                    </fieldset>
                </div>
            </div>
        </fieldset>
    </formUtil:dynamicComplexInputs>
    <fieldset class="row dynamic-fieldset-addbutton">
        <div class="form-group">
            <div class="col-xs-12 center">
                <button type="button" class="btn btn-lg btn-grey addButton">
                    <g:message code="survey.form.button.addQuestion"/>
                    <i class="far fa-plus"></i>
                </button>
                <button type="button" class="btn btn-lg btn-grey inverted reorderQuestionsButton">
                    <g:message code="survey.form.button.reorderQuestions.start"/>
                    <i class="far fa-sort-alt"></i>
                </button>
                <button type="button" class="btn btn-lg btn-blue endReorderQuestionsButton">
                    <g:message code="survey.form.button.reorderQuestions.end"/>
                    <i class="far fa-sort-alt"></i>
                </button>
            </div>
        </div>
    </fieldset>
    <g:render template="/campaigns/edit/stepButtons" model="[
            saveAndSentButtons: true,
            mappings          : mappings,
            status            : status,
            command           : command,
            numberRecipients  : numberRecipients]"/>
</form>