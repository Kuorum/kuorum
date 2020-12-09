<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<g:each in="${activites.data}" var="activity">
    <div class="contact-activity">
        <h2>${activity.campaignName}</h2>
        <table class="table list-tracking">
            <thead>
                <tr>
                    <th><g:message code="tools.massMailing.list.status"/></th>
                    <th><g:message code="tools.massMailing.timestamp"/></th>
                    <th></th>
                    <th><g:message code="tools.massMailing.actions"/></th>
                </tr>
            </thead>
            <tbody>
            <g:each in="${activity.events}" var="event">
                <tr>
                    <td><g:message code="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.${event.status}"/></td>
                    <td><g:formatDate formatName="default.date.format" date="${event.timestamp}"/> </td>
                    <td><kuorumDate:humanDate date="${event.timestamp}"/></td>
                    <td>
                        <g:if test="${event.status == org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.SENT}">
                            <g:link mapping="politicianMassMailingTrackEventsResend" params="[newsletterId:activity.newsletterId,tackingMailId:activity.trackingId]" class="btn btn-blue inverted resend-email">
                                <g:message code="tools.massMailing.actions.resend"/>
                                <span class="fal fa-angle-double-right"></span>
                            </g:link>
                        </g:if>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
</g:each>