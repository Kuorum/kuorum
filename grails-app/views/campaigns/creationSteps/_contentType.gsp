<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, debateForm" />
<h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>
<formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
<form action="#" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="debateId" value="${debateId?:''}"/>

    <fieldset class="form-group">
        <h4 for="contentType" class="col-sm-3 col-md-2 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.contentType.label"/>:</h4>
        <div class="col-sm-3 col-md-2">
            <formUtil:checkBox command="${command}" field="simpleTemplate"/>
        </div>
        <div class="col-sm-3 col-md-2">
            <formUtil:checkBox command="${command}" field="uploadHTML"/>
        </div>
        <div class="col-sm-3 col-md-2">
            <formUtil:checkBox command="${command}" field="plainText"/>
        </div>
    </fieldset>
</form>
