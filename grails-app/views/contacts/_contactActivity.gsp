<%@ page import="kuorum.core.model.solr.SolrType; org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<g:each in="${activites.data}" var="activity">
    <div class="contact-activity">
        <h3><campaignUtil:showIcon campaignType="${activity.campaignType}" defaultType="${kuorum.core.model.solr.SolrType.NEWSLETTER}"/> ${activity.campaignName}</h3>
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
                        <g:if test="${event.status == org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.SENT && activity.trackingId}">
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

<!-- Modal registro/validation -->
<div class="modal fade modal-activity-resend" id="activity-resend-confirm" tabindex="-1" role="dialog" aria-labelledby="modal-activity-resend" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><g:message code="tools.massMailing.actions.resend.modal.title"/></h4>
            </div>
            <div class="modal-body">
                <p><g:message code="tools.massMailing.actions.resend.modal.text" /></p>
            </div>
            <div class="modal-footer">
                <a href="#" class="cancel" data-dismiss="modal"><g:message code="profile.modal.cropImage.cancel"/> </a>
                <button type="button" class="btn">
                    <g:message code="tools.massMailing.actions.resend.modal.submit"/>
                    <span class="fal fa-angle-double-right"></span>
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>