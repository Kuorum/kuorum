<h2 class="sr-only"><g:message code="tools.massMailing.list.recipients"/></h2>
<div class="pag-list-contacts clearfix">
    <nav:contactPagination
            link="${g.createLink(mapping:"politicianMassMailingTrackEvents", params: [campaignId:campaignId], absolute:true)}"
            currentPage="${trackingPage.page}"
            sizePage="${trackingPage.size}"
            ulClasss="paginationTop"
            total="${trackingPage.total}"/>
</div>
<table class="table">
    <thead>
    <tr>
        <th><g:message code="kuorum.web.commands.payment.contact.ContactCommand.name.label"/></th>
        <th><g:message code="tools.massMailing.list.opens"/></th>
        <th><g:message code="tools.massMailing.list.click"/></th>
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
            <td><span class="only-mobile"><g:message code="tools.massMailing.list.opens"/>: </span>${trackingMail.numOpens}</td>
            <td><span class="only-mobile"><g:message code="tools.massMailing.list.click"/>: </span>${trackingMail.numClicks}</td>
            <td>
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
            currentPage="${trackingPage.page}"
            sizePage="${trackingPage.size}"
            ulClasss="paginationBottom"
            total="${trackingPage.total}"/>
</div>