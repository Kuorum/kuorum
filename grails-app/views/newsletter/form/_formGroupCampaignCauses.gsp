<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<fieldset class="form-group tags-campaign">
    <label for="causes" class="col-sm-2 col-md-1 control-label">
        <span class="fa fa-info-circle" data-toggle="tooltip" data-placement="top" title="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.causes.label.info')}"></span>
        <g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.causes.label"/>:
    </label>
    <div class="col-sm-8 col-md-7">
        <formUtil:dynamicListInput command="${command}" field="causes" showLabel="false" autocompleteUrl="${g.createLink(mapping: 'suggestTags', absolute: true)}"/>
    </div>
</fieldset>