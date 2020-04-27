<h2 class="sr-only"><g:message code="tools.massMailing.view.stats"/></h2>
<div class="actions">

</div>
<ul class="activity">
    <li class="posts">
        <span class='recip-number'><newsletterUtil:campaignsSent campaign="${newsletter}"/></span>
        <g:message code="tools.massMailing.list.recipients"/>
    </li>
    <li class="posts">
        <newsletterUtil:openRate campaign="${newsletter}"/>
        <g:message code="tools.massMailing.list.opens"/>
    </li>
    <li class="posts">
        <newsletterUtil:clickRate campaign="${newsletter}"/>
        <g:message code="tools.massMailing.list.click"/>
    </li>
    <li class="posts">
        <newsletterUtil:unsubscribeRate campaign="${newsletter}"/>
        <g:message code="tools.massMailing.list.unsubscribe"/>
    </li>
</ul>