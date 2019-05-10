<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, postForm, debateForm" />

<div class="box-steps container-fluid campaign-steps">
    <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings, attachEvent:attachEvent]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>
    <formUtil:validateForm bean="${command}" form="politicianMassMailingForm"  dirtyControl="true"/>
    <form action="#" class="form-horizontal campaign-form" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <fieldset class="form-group">
            <label for="title" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.title.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <formUtil:input command="${command}" field="title"/>
            </div>
        </fieldset>

        <fieldset class="form-group">
            <label for="text" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.text.label"/>:</label>
            <div class="textareaContainer col-sm-8 col-md-7">
                <formUtil:textArea command="${command}" field="body" rows="8" texteditor="texteditor" placeholder="${customPlaceHolderBody}"/>
            </div>
        </fieldset>

        <fieldset class="form-group">
            <label for="files" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.files.label"/>:</label>
            <div class="textareaContainer col-sm-8 col-md-7">
                <formUtil:uploadCampaignFiles campaign="${campaign}"/>
            </div>
        </fieldset>

        <fieldset class="form-group multimedia">
            <label for="headerPictureId" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.image.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <span class="span-label sr-only"><g:message code="default.upload.title" /></span>
                <input type="hidden" name="fileType" value="${(command.fileType == kuorum.core.FileType.YOUTUBE.toString())?'YOUTUBE':'IMAGE'}" id="fileType">
                <ul class="nav nav-pills nav-justified">
                    <li class="${command.headerPictureId || (command.errors?.getFieldError('headerPictureId')?.codes?.contains('imageOrUrlYoutubeRequired') && command.errors?.getFieldError('videoPost')?.codes?.contains('imageOrUrlYoutubeRequired'))?'active':''}">
                        <a href="#projectUploadImage" data-toggle="tab" data-filetype="IMAGE"><g:message code="default.upload.image" /></a>
                    </li>
                    <li class="${command.videoPost?'active':''}">
                        <a href="#projectUploadYoutube" data-toggle="tab" data-filetype="YOUTUBE"><g:message code="default.upload.youtube" /></a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane fade ${command.headerPictureId || (command.errors?.getFieldError('headerPictureId')?.codes?.contains('imageOrUrlYoutubeRequired') && command.errors?.getFieldError('videoPost')?.codes?.contains('imageOrUrlYoutubeRequired'))?'in active':''}" id="projectUploadImage">
                        <div class="form-group image" data-multimedia-switch="on" data-multimedia-type="IMAGE">
                            <formUtil:editImage command="${command}" field="headerPictureId" fileGroup="${kuorum.core.FileGroup.PROJECT_IMAGE}"/>
                        </div>
                    </div>

                    <div class="tab-pane fade ${command.videoPost?'in active':''}" id="projectUploadYoutube">
                        <div class="video" data-multimedia-switch="on" data-multimedia-type="YOUTUBE">
                            <formUtil:url command="${command}" field="videoPost" placeHolder="${g.message(code: "kuorum.web.commands.payment.massMailing.DebateCommand.videoPost.placeholder")}"/>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>
        <g:render template="/campaigns/edit/stepButtons" model="[mappings:mappings, status:status, command: command, numberRecipients:numberRecipients]"/>
    </form>
</div>

<g:render template="/newsletter/timeZoneSelectorPopUp"/>
