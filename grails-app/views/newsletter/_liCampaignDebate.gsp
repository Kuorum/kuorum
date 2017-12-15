<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>
<g:set var="type" value="debate"/>
<g:set var="faIcon" value="fa-comments-o"/>
<g:set var="typeName" value="${g.message(code: 'tools.campaign.new.debate')}"/>
<g:if test="${debate.event}">
    <g:set var="type" value="event"/>
    <g:set var="faIcon" value="fa-calendar-check-o"/>
    <g:set var="typeName" value="${g.message(code: 'tools.campaign.new.event')}"/>
</g:if>

<li class="${debate.newsletter.status} debateItem" id="campaignPos_${idx}">
    <span class="id sr-only">${debate.id}</span>
    <span class="state" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${g.message(code: "org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.${debate.campaignStatusRSDTO}")}">
        ${debate.campaignStatusRSDTO}
    </span>
    <span class="type">${type}</span>
    <span class="fa ${faIcon}" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${typeName}"></span>
    <h3>
        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" class="title">
            ${debate.name}
        </g:link>
    </h3>
    <p class="name">
        <g:message code="${CampaignStatusRSDTO.class.name}.${debate.campaignStatusRSDTO}"/>
        <input class="timestamp" type="hidden" val="${debate?.datePublished?.time?:'LAST'}" />  %{-- Letters after numbers--}%
        <g:if test="${debate.datePublished}">
            <span class="date">
                (<g:formatDate date="${debate.datePublished}" type="datetime" style="LONG" timeStyle="SHORT" timeZone="${user.timeZone}"/>)
            </span>
            - ${debate.anonymousFilter?.name?:g.message(code:'tools.massMailing.fields.filter.to.all')}
        </g:if>
    </p>
    <ul class="list-campaign-stats">
        <li class="recipients">
            <span class="recip-number"><campaignUtil:campaignsSent campaign="${debate.newsletter}"/></span>
            <g:message code="tools.massMailing.list.recipients"/>
        </li>
        <li class="open">
            <campaignUtil:openRate campaign="${debate.newsletter}"/>
            <g:message code="tools.massMailing.list.opens"/>
        </li>
        <li class="click">
            <campaignUtil:clickRate campaign="${debate.newsletter}"/>
            <g:message code="tools.massMailing.list.click"/>
        </li>
    </ul>
    <ul class="list-actions">
        <g:if test="${debate.campaignStatusRSDTO == CampaignStatusRSDTO.SENT}">
            <li>
                <g:link mapping="politicianDebateStatsShow" params="[debateId: debate.id]" role="button" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats</span></g:link>
            </li>
        </g:if>
        <li>
            <g:set var="modal" value="${debate.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ?'modalEditScheduled':''}"/>
            <g:link mapping="debateEditContent" params="${debate.encodeAsLinkProperties()}" role="button" class="campaignEdit ${modal}"><span class="fa fa-edit"></span><span class="sr-only">Edit</span></g:link>
        </li>
        <li>
            <g:link mapping="debateRemove" params="${debate.encodeAsLinkProperties()}"  role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></g:link>
        </li>
    </ul>
</li>
