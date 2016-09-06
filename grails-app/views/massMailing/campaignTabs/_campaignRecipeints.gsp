<h2 class="sr-only"><g:message code="tools.massMailing.list.recipients"/></h2>
<div class="pag-list-contacts clearfix">
    <nav:contactPagination
            currentPage="${0}"
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
            <td><span class="only-mobile"><g:message code="kuorum.web.commands.payment.contact.ContactCommand.name.label"/>: </span>${trackingMail.contact.name}</td>
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
            currentPage="${0}"
            sizePage="${trackingPage.size}"
            ulClasss="paginationBottom"
            total="${trackingPage.total}"/>
</div>