<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>

<g:set var="newsletter" value="${bulletin.newsletter}"/>
<li class="${newsletter.status}" id="campaignPos_${idx}">
    <span class="id sr-only">${bulletin.id}</span>
    <span class="state" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${g.message(code: "org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.${newsletter.status}")}">
        ${newsletter.status}
    </span>
    <span class="type">newsletter</span>
    <span class="fal fa-envelope" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${g.message(code: 'tools.campaign.new.newsletter')}"></span>

    <h3>
        <g:set var="modal"
               value="${newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ? 'modalEditScheduled' : ''}"/>
        <g:link mapping='politicianMassMailingShow' params="[campaignId: bulletin.id]" class="title ${modal}"
                absolute="true">
            ${newsletter.name}<span></span>
        </g:link>
    </h3>

    <p class="name">
        <g:message
                code="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.class.name}.${newsletter.status}"/>
        <input class="timestamp" type="hidden"
               val="${newsletter?.sentOn?.time ?: 'LAST'}"/>  %{-- Letters are after than numbers--}%
        <g:if test="${newsletter.sentOn}">
            <span class="date">
                (<g:formatDate date="${newsletter.sentOn}" type="datetime" style="LONG" timeStyle="SHORT"
                               timeZone="${user.timeZone}"/>)
            </span>
            :: ${newsletter.filter?.name ?: g.message(code: 'tools.massMailing.fields.filter.to.all')}
        </g:if>
    </p>
    <ul class="list-campaign-stats">
        <li class="recipients">
            <span class="recip-number"><newsletterUtil:campaignsSent campaign="${newsletter}"/></span>
            <g:message code="tools.massMailing.list.recipients"/>

        </li>
        <li class="open">
            <newsletterUtil:openRate campaign="${newsletter}"/>
            <g:message code="tools.massMailing.list.opens"/>
        </li>
        <li class="click">
            <newsletterUtil:clickRate campaign="${newsletter}"/>
            <g:message code="tools.massMailing.list.click"/>
        </li>
    </ul>
    <ul class="list-actions">
        <g:if test="${newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
            <li>
                <g:link mapping="politicianMassMailingShow" params="[campaignId: bulletin.id]"
                        class="campaignStats"><span class="fal fa-chart-line"></span> <span
                        class="sr-only">Stats</span></g:link>
            </li>
        </g:if>
        <g:else>
            <li>
                <g:link mapping="politicianMassMailingShow" params="[campaignId: bulletin.id]"
                        class="campaignEdit ${modal}"><span class="fal fa-edit"></span> <span
                        class="sr-only">Edit</span></g:link>
            </li>
        </g:else>
        <li>
            <g:link mapping="politicianMassMailingCopy" params="[campaignId: bulletin.id]" class="campaignStats"><span
                    class="fal fa-copy"></span> <span class="sr-only">Copy</span></g:link>
        </li>
        <li>
            <g:link mapping="politicianMassMailingRemove" params="[campaignId: bulletin.id]" role="button"
                    class="campaignDelete"><span class="fal fa-trash"></span> <span
                    class="sr-only">Delete</span></g:link>
        </li>
    </ul>
</li>
