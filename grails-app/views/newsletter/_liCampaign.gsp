<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>

<li class="${campaign.status} newsletterItem" id="campaignPos_${idx}">
    <span class="id sr-only">${campaign.id}</span>
    <span class="state" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${g.message(code: "org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.${campaign.status}")}">
        ${campaign.status}
    </span>
    <span class="type">newsletter</span>
    <span class="fa fa-envelope-o" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${g.message(code: 'tools.campaign.new.newsletter')}"></span>
    <h3>
        <g:set var="modal" value="${campaign.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ?'modalEditScheduled':''}"/>
        <g:link mapping='politicianMassMailingShow' params="[campaignId:campaign.id]" class="title ${modal}" absolute="true">
            ${campaign.name}<span></span>
        </g:link>
    </h3>
    <p class="name">
        <g:message code="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.class.name}.${campaign.status}"/>
        <input class="timestamp" type="hidden" val="${campaign?.sentOn?.time?:'LAST'}" />  %{-- Letters are after than numbers--}%
        <g:if test="${campaign.sentOn}">
            <span class="date">
                (<g:formatDate date="${campaign.sentOn}" type="datetime" style="LONG" timeStyle="SHORT" timeZone="${user.timeZone}"/>)
            </span>
            - ${campaign.filter?.name?:g.message(code:'tools.massMailing.fields.filter.to.all')}
        </g:if>
    </p>
    <ul class="list-campaign-stats">
        <li class="recipients">
            <span class="recip-number"><newsletterUtil:campaignsSent campaign="${campaign}"/></span>
            <g:message code="tools.massMailing.list.recipients"/>

        </li>
        <li class="open">
            <newsletterUtil:openRate campaign="${campaign}"/>
            <g:message code="tools.massMailing.list.opens"/>
        </li>
        <li class="click">
            <newsletterUtil:clickRate campaign="${campaign}"/>
            <g:message code="tools.massMailing.list.click"/>
        </li>
    </ul>
    <ul class="list-actions">
    <g:if test="${campaign.status==org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
        <li>
            <g:link mapping="politicianMassMailingShow" params="[campaignId:campaign.id]" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats</span></g:link>
        </li>
    </g:if>
    <g:else>
        <li>
            <g:link mapping="politicianMassMailingShow" params="[campaignId:campaign.id]" class="campaignEdit ${modal}"><span class="fa fa-edit"></span> <span class="sr-only">Edit</span></g:link>
        </li>
    </g:else>
        <li>
            <g:link mapping="politicianMassMailingRemove" params="[campaignId:campaign.id]"  role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></g:link>
        </li>
    </ul>
</li>
