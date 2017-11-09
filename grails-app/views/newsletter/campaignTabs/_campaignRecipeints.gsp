<r:require modules="campaignList"/>
<h2 class="sr-only"><g:message code="tools.massMailing.list.recipients"/></h2>
<div class="pag-list-contacts clearfix">
    <div class="actions">
        <g:link mapping="politicianMassMailingTrackEventsReport" params="[campaignId:campaignId]" class="btn btn-blue inverted" elementId="exportCampaignEvents" data-modalId="export-campaignEvents-modal">
            <span class="fa fa-file-excel-o"></span>
            <g:message code="tools.massMailing.list.recipients.export.csv"/>
        </g:link>
    </div>
    <nav:contactPagination
            link="${g.createLink(mapping:"politicianMassMailingTrackEvents", params: [campaignId:campaignId], absolute:true)}"
            currentPage="${trackingPage.page}"
            sizePage="${trackingPage.size}"
            ulClasss="paginationTop"
            total="${trackingPage.total}"/>
</div>
<table class="table list-tracking">
    <thead>
    <tr>
        <th><g:message code="kuorum.web.commands.payment.contact.ContactCommand.name.label"/></th>
        <th><g:message code="tools.massMailing.list.status"/></th>
        <th><g:message code="tools.massMailing.list.opens"/></th>
        <th><g:message code="tools.massMailing.list.click"/></th>
        <th><g:message code="tools.massMailing.list.bounced"/></th>
        <th><g:message code="tools.massMailing.list.hardBounced"/></th>
        <th><g:message code="tools.contact.list.contact.engagement"/></th>
    </tr>
    </thead>
    <tbody>
    <!-- EACH BLOCK -->
    <g:each in="${trackingPage.data}" var="trackingMail">
        <tr>
            <td class="recipient-name">
                <span class="only-mobile"><g:message code="kuorum.web.commands.payment.contact.ContactCommand.name.label"/>: </span>
                <g:link mapping="politicianContactEdit" params="[contactId:trackingMail.contact.id]">${trackingMail.contact.name}</g:link>
                <g:link mapping="politicianContactEdit" params="[contactId:trackingMail.contact.id]" target="_blank">

                    <span class="fa fa-external-link fa-sm"></span><span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
                </g:link>
            </td>
            <td><span class="only-mobile"><g:message code="tools.massMailing.list.status"/>: </span><g:message code="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.${trackingMail.mailStatus}"/> </td>
            <td><span class="only-mobile"><g:message code="tools.massMailing.list.opens"/>: </span>${trackingMail.numOpens}</td>
            <td><span class="only-mobile"><g:message code="tools.massMailing.list.click"/>: </span>${trackingMail.numClicks}</td>
            <td><span class="only-mobile"><g:message code="tools.massMailing.list.bounced"/>: </span>${trackingMail.numBounced}</td>
            <td><span class="only-mobile"><g:message code="tools.massMailing.list.hardBounced"/>: </span>${trackingMail.numHardBounced}</td>
            <td class="tracking-engagement">
                <span class="only-mobile"><g:message code="tools.contact.list.contact.engagement"/></span>
                <contactUtil:engagement concat="${trackingMail.contact}"/>
            </td>
        </tr>
    </g:each>
    <!-- END EACH BLOCK -->
    </tbody>
</table>
<div class="pag-list-contacts clearfix">
    <nav:contactPagination
            link="${g.createLink(mapping:"politicianMassMailingTrackEvents", params: [campaignId:campaignId], absolute:true)}"
            currentPage="${trackingPage.page}"
            sizePage="${trackingPage.size}"
            ulClasss="paginationBottom"
            total="${trackingPage.total}"/>
</div>


<div id="export-campaignEvents-modal" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="exportTagsTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header"><h4><g:message code="modal.exportedTrackingEvents.title"/></h4></div>
            <div class="modal-body">
                <p>
                    <g:message code="modal.exportedTrackingEvents.explanation"/>
                    <g:message code="modal.exported.explanation"/>
                </p>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message code="modal.exportedTrackingEvents.close"/></a>
            </div>
        </div>
    </div>
</div>