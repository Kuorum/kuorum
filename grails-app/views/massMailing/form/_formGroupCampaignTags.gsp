<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<fieldset class="form-group tags-campaign">
    <label for="tagsField" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.tags.label"/>: </label>
    <div class="col-sm-8 col-md-7">
        <label class="toggle-inputs" data-toggle="collapse" data-target="#tag-events">
            <g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.eventsWithTag.label"/>
            <span class="go-up stack">
                <span class="fa-stack fa-lg" aria-hidden="true">
                    <span class="fa fa-circle fa-stack-1x"></span>
                    <span class="fa fa-angle-down fa-stack-1x fa-inverse"></span>
                </span>
            </span>
        </label>
        <div class="tag-events collapse" id="tag-events">
            <g:each in="${events}" var="event">
                <div class="tag-event">
                    <span class="tag-event-label">
                        <g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.eventsWithTag.${event}"/>
                    </span>
                    <span class="tag-event-input">
                        <formUtil:tags command="${command}" field="tags.${event}"/>
                    </span>
                </div>
            </g:each>
        </div>
    </div>
</fieldset>