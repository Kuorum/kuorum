<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<fieldset class="form-group tags-campaign">
    <label for="tagsField" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.tags.label"/>: </label>
    <div class="col-sm-8 col-md-7">
        <label class="toggle-inputs">
            <g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.eventsWithTag.label"/>
        </label>
        <div class="tag-events collapse">
            <g:each in="${events}" var="event">
                <div class="tag-event">
                    <span class="tag-event-label">
                        <g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.eventsWithTag.${event}"/>
                    </span>
                    <span class="tag-event-input">
                        <formUtil:tags command="${command.tags}" field="${event}" prefixFieldName="tags" label="${g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.eventsWithTag.'+event )}"/>
                    </span>
                </div>
            </g:each>
        </div>
    </div>
</fieldset>