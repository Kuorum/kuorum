<%@ page import="kuorum.core.FileGroup; org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker" />
<h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>
<formUtil:validateForm bean="${command}" form="politicianMassMailingForm" />
<form action="#" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="debateId" value="${debateId?:''}"/>

    <!-- Hidden inputs -->
    <input type="hidden" name="tags" value="${command.tags?:''}"/>
    <input type="hidden" name="photoUrl" value="${command.headerPictureId?:''}"/>
    <input type="hidden" name="videoPost" value="${command.videoPost?:''}"/>
    <g:each in="${command.eventsWithTag}" var="inputEvent">
        <g:if test="${inputEvent == org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.OPEN}">
            <input type="hidden" name="eventsWithTag" value="${inputEvent}"/>
        </g:if>
        <g:elseif test="${inputEvent == org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.CLICK}">
            <input type="hidden" name="eventsWithTag" value="${inputEvent}"/>
        </g:elseif>
    </g:each>

    <g:render template="/massMailing/filter" model="[command: command, filters: filters,anonymousFilter: anonymousFilter, totalContacts: totalContacts, hideSendTestButton: true, showOnly: true, totalContacts: totalContacts]"/>

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

    <fieldset class="buttons">
        <div class="text-right">
            <ul class="form-final-options">
                <li><input class="btn btn-blue inverted" id="save-debate" type="submit" value="${g.message(code: "tools.massMailing.save")}" /></li>
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
            </div>
        </div>
    </div>
</div>
