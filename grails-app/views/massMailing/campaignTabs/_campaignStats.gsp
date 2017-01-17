<h2 class="sr-only"><g:message code="tools.massMailing.view.stats"/></h2>
<ul class="activity">
    <li class="posts">
        <span class='recip-number'><campaignUtil:campaignsSent campaign="${campaign}"/></span>
        <g:message code="tools.massMailing.list.recipients"/>
    </li>
    <li class="posts">
        <span class='open-number'><campaignUtil:openRate campaign="${campaign}"/>%</span>
        <g:message code="tools.massMailing.list.opens"/>
    </li>
    <li class="posts">
        <span class='click-number'><campaignUtil:clickRate campaign="${campaign}"/>%</span>
        <g:message code="tools.massMailing.list.click"/>
    </li>
</ul>