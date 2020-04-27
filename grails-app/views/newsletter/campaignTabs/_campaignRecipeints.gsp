<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<h2 class="sr-only"><g:message code="tools.massMailing.list.recipients"/></h2>
<div class="pag-list-contacts clearfix">
    <div class="actions">
        <g:link mapping="politicianMassMailingTrackEventsReport" params="[newsletterId:newsletterId]" class="btn btn-blue inverted export-modal-button" data-modalId="export-campaignEvents-modal">
            <span class="fal fa-file-excel"></span>
            <g:message code="tools.massMailing.list.recipients.export.csv"/>
        </g:link>
    </div>
    <nav:contactPagination
            link="${g.createLink(mapping:"politicianMassMailingTrackEvents", params: [newsletterId:newsletterId], absolute:true)}"
            currentPage="${trackingPage.page}"
            sizePage="${trackingPage.size}"
            ulClasss="paginationTop"
            total="${trackingPage.total}"/>
</div>
<table class="table list-tracking">
    <thead>
    <tr>
        <th><g:message code="kuorum.web.commands.payment.contact.ContactCommand.name.label"/></th>
        <th class="dropdown">
            <input type="hidden" name="filter-status" id="filter-status" value="${status}"/>
            <a data-target="#" href="#" id="status-filter" class="dropdown-toggle dropdown-menu-right" data-toggle="dropdown">
                <g:message code="tools.massMailing.list.status"/>
                <span class="fal fa-angle-down"></span>
            </a>
            <ul id="status-filter-options" class="dropdown-menu" aria-labelledby="status-filter" role="menu">
                <li><g:message code="tools.massMailing.list.status.dropdown.title"/></li>
                <li>
                    <a href="#">
                        <g:message code="tools.massMailing.list.status.dropdown.all"/>
                        <g:if test="${!status}">
                            <span class="far fa-check"/>
                        </g:if>
                    </a>
                </li>
                <g:each in="${[
                        org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.OPEN,
                        org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.CLICK,
                        org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.BOUNCED,
                        org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.HARD_BOUNCED,
                        org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.NOT_SENT,
                        org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.UNSUBSCRIBE,
                ]}" var="filterOptionMailStatus">
                    <li>
                        <a href="#${filterOptionMailStatus}">
                            <g:message code="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO.${filterOptionMailStatus}"/>
                            <g:if test="${filterOptionMailStatus == status}">
                                <span class="far fa-check"/>
                            </g:if>
                        </a>
                    </li>
                </g:each>
            </ul>
        </th>
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

                    <span class="fal fa-external-link fa-sm"></span><span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
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
            link="${g.createLink(mapping:"politicianMassMailingTrackEvents", params: [newsletterId:newsletterId], absolute:true)}"
            currentPage="${trackingPage.page}"
            sizePage="${trackingPage.size}"
            ulClasss="paginationBottom"
            total="${trackingPage.total}"/>
</div>