<%@ page import="kuorum.web.commands.payment.CampaignContentCommand; org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, postForm, debateForm"/>

<div class="box-steps container-fluid campaign-steps"
     xmlns="http://www.w3.org/1999/html">
    <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings, attachEvent: attachEvent]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>
    <formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
    <form action="#" class="form-horizontal campaign-form" id="politicianMassMailingForm" method="POST"
          data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="sendType"
               value="${kuorum.web.commands.payment.CampaignContentCommand.CAMPAIGN_SEND_TYPE_DRAFT}"
               id="sendMassMailingType"/>
    <input type="hidden" name="redirectLink" id="redirectLink"/>

    <fieldset aria-live="polite" class="form-group">
        <label for="title" class="col-sm-2 col-md-1 control-label"><g:message
                    code="kuorum.web.commands.payment.massMailing.DebateCommand.title.label"/>:</label>

            <div class="col-sm-8 col-md-7">
                <formUtil:input command="${command}" field="title"/>
            </div>
    </fieldset>

    <fieldset aria-live="polite" class="form-group">
        <label for="text" class="col-sm-2 col-md-1 control-label"><g:message
                code="kuorum.web.commands.payment.massMailing.MassMailingCommand.text.label"/>:</label>

        <div class="textareaContainer col-sm-8 col-md-7">
            <formUtil:textArea command="${command}" field="body" rows="8" texteditor="texteditor"
                               placeholder="${customPlaceHolderBody}"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group multimedia">
        <label for="headerPictureId" class="col-sm-2 col-md-1 control-label"><g:message
                code="kuorum.web.commands.payment.massMailing.DebateCommand.image.label"/>:
            <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top"
                  title="${g.message(code: 'kuorum.web.commands.payment.contest.NewContestApplicationCommand.headerPictureId.info.'+campaign.campaignType)}"></span></label>

        <div class="col-sm-8 col-md-7">
            <span class="span-label sr-only"><g:message code="default.upload.title"/></span>
            <input type="hidden"
                   value="IMAGE">
            <formUtil:editImage command="${command}" field="headerPictureId"
                                fileGroup="${kuorum.core.FileGroup.PROJECT_IMAGE}"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group multimedia">
        <label for="videoPost" class="col-sm-2 col-md-1 control-label"><g:message
                code="kuorum.web.commands.payment.massMailing.DebateCommand.video.label"/>:</label>

        <div class="col-sm-8 col-md-7">
            <span class="span-label sr-only"><g:message code="default.upload.title"/></span>
            <input type="hidden"
                   value="YOUTUBE">
            <formUtil:url command="${command}" field="videoPost"
                          placeHolder="${g.message(code: "kuorum.web.commands.payment.massMailing.DebateCommand.videoPost.placeholder")}"/>
        </div>
    </fieldset>

        <g:if test="${campaign}">
            <fieldset aria-live="polite" class="form-group">
                <label for="files" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.files.label"/>:</label>
                <div class="textareaContainer col-sm-8 col-md-7">
                    <formUtil:uploadCampaignFiles campaign="${campaign}"/>
                </div>
            </fieldset>
        </g:if>

        <g:render template="/campaigns/edit/stepButtons" model="[campaign:campaign, mappings:mappings, status:status, command: command, numberRecipients:numberRecipients]"/>
    </form>
</div>

<g:render template="/newsletter/timeZoneSelectorPopUp"/>
