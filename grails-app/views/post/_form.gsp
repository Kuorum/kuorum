<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, postForm" />
<h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>
<formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
<form action="#" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="debateId" value="${debateId?:''}"/>

    <g:render template="/massMailing/filter" model="[command: command, filters: filters,anonymousFilter: anonymousFilter, totalContacts: totalContacts, hideSendTestButton: true]"/>

    <fieldset class="form-group">
        <label for="title" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.title.label"/>:</label>
        <div class="col-sm-8 col-md-7">
            <formUtil:input command="${command}" field="title"/>
        </div>
    </fieldset>

    <fieldset class="form-group">
        <label for="text" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.text.label"/>:</label>
        <div class="textareaContainer col-sm-8 col-md-7">
            <formUtil:textArea command="${command}" field="body" rows="8" texteditor="texteditor"/>
        </div>
    </fieldset>

    <g:render template="/massMailing/form/formGroupCampaignTags" model="[command:command, events:[TrackingMailStatusRSDTO.OPEN,TrackingMailStatusRSDTO.CLICK,TrackingMailStatusRSDTO.POST_LIKE]]"/>

    <fieldset class="form-group multimedia">
        <label for="headerPictureId" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.image.label"/>:</label>
        <div class="col-sm-8 col-md-7">
            <span class="span-label sr-only"><g:message code="admin.createProject.upload.imageOrVideo" /></span>
            <input type="hidden" name="fileType" value="${(command.fileType == kuorum.core.FileType.YOUTUBE.toString())?'YOUTUBE':'IMAGE'}" id="fileType">
            <ul class="nav nav-pills nav-justified">
                <li class="${command.headerPictureId || (command.errors?.getFieldError('headerPictureId')?.codes?.contains('imageOrUrlYoutubeRequired') && command.errors?.getFieldError('videoPost')?.codes?.contains('imageOrUrlYoutubeRequired'))?'active':''}">
                    <a href="#projectUploadImage" data-toggle="tab" data-filetype="IMAGE"><g:message code="admin.createProject.upload.image" /></a>
                </li>
                <li class="${command.videoPost?'active':''}">
                    <a href="#projectUploadYoutube" data-toggle="tab" data-filetype="YOUTUBE"><g:message code="admin.createProject.upload.video" /></a>
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

    <fieldset class="buttons">
        <div class="text-right">
            <ul class="form-final-options">
                <li>
                    <a href="#" id="save-draft-debate">
                        <g:message code="tools.massMailing.saveDraft"/>
                    </a>
                </li>
                <li>
                    <a href="#" class="btn btn-blue inverted" role="button" id="openCalendar">
                        <span class="fa fa-clock-o"></span>
                        <g:message code="tools.massMailing.schedule"/>
                    </a>
                    <div id="selectDate">
                        <label class="sr-only"><g:message code="tools.massMailing.schedule.label"/></label>
                        <formUtil:date command="${command}" field="publishOn" cssClass="form-control" time="true"/>
                        <a href="#" class="btn btn-blue inverted" id="send-debate-later">
                            <g:message code="tools.massMailing.schedule.sendLater"/>
                        </a>
                    </div>
                </li>
                <li><a href="#" class="btn btn-blue inverted" id="send-draft"><g:message code="tools.massMailing.send"/></a></li>
            </ul>
        </div>
    </fieldset>
</form>

<!-- MODAL CONFIRM -->
<div class="modal fade in" id="campaignConfirm" tabindex="-1" role="dialog" aria-labelledby="campaignConfirmTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="campaignConfirmTitle">
                    <g:message code="tools.massMailing.confirmationModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <a href="#" class="btn btn-blue inverted btn-lg" id="saveCampaignBtn">
                    <g:message code="tools.massMailing.confirmationModal.button"/>
                </a>
            </div>
        </div>
    </div>
</div>

<!-- MODAL TEST ADVISE -->
<div class="modal fade in" id="sendTestModal" tabindex="-1" role="dialog" aria-labelledby="sendTestModalTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="sendTestModalTitle">
                    <g:message code="tools.massMailing.sendTestModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p><g:message code="tools.massMailing.sendTestModal.text"/></p>
                <a href="#" class="btn btn-blue inverted btn-lg" id="sendTestModalButonOk">
                    <g:message code="tools.massMailing.sendTestModal.button"/>
                </a>
            </div>
        </div>
    </div>
</div>

<!-- WARN USING ANONYMOUS FILTER -->
<div class="modal fade in" id="campaignWarnFilterEdited" tabindex="-1" role="dialog" aria-labelledby="campaignWarnFilterEditedTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4>
                    <g:message code="tools.massMailing.warnFilterEdited.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p><g:message code="tools.massMailing.warnFilterEdited.text"/> </p>
                <a href="#" class="btn btn-blue inverted btn-lg" id="campaignWarnFilterEditedButtonOk">
                    <g:message code="tools.massMailing.warnFilterEdited.button"/>
                </a>
                <a href="#" class="btn btn-grey-light btn-lg" data-dismiss="modal" id="campaignWarnFilterEditedButtonClose">
                    <g:message code="tools.massMailing.warnFilterEdited.cancel"/>
                </a>

            </div>
        </div>
    </div>
</div>
