<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>
<g:set var="type" value="post"/>
<g:set var="faIcon" value="fa-newspaper-o"/>
<g:set var="typeName" value="${g.message(code: 'tools.campaign.new.post')}"/>
<g:if test="${post.event}">
    <g:set var="type" value="event"/>
    <g:set var="faIcon" value="fa-calendar-check-o"/>
    <g:set var="typeName" value="${g.message(code: 'tools.campaign.new.event')}"/>
</g:if>

<li class="${post.newsletter.status} postItem" id="campaignPos_${idx}">
    <span class="id sr-only">${post.id}</span>
    <span class="state" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${g.message(code: "org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.${post.campaignStatusRSDTO}")}">
        ${post.newsletter.status}
    </span>
    <span class="type">${type}</span>
    <span class="fa ${faIcon}" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${typeName}"></span>
    <h3>
        <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" class="title">
            ${post.name}<span></span>
        </g:link>
    </h3>
    <p class="name">
        <g:message code="${CampaignStatusRSDTO.class.name}.${post.campaignStatusRSDTO}"/>
        <input class="timestamp" type="hidden" val="${post?.datePublished?.time?:'LAST'}" />  %{-- Letters are after than numbers--}%
        <g:if test="${post.datePublished}">
            <span class="date">
                (<g:formatDate date="${post.datePublished}" type="datetime" style="LONG" timeStyle="SHORT" timeZone="${user.timeZone}"/>)
            </span>
            - ${post.anonymousFilter?.name?:g.message(code:'tools.massMailing.fields.filter.to.all')}
        </g:if>
    </p>
    <ul class="list-campaign-stats">
        <li class="recipients">
            <span class="recip-number"><campaignUtil:campaignsSent campaign="${post.newsletter}"/></span>
            <g:message code="tools.massMailing.list.recipients"/>
        </li>
        <li class="open">
            <campaignUtil:openRate campaign="${post.newsletter}"/>
            <g:message code="tools.massMailing.list.opens"/>
        </li>
        <li class="click">
            <campaignUtil:clickRate campaign="${post.newsletter}"/>
            <g:message code="tools.massMailing.list.click"/>
        </li>
    </ul>
    <ul class="list-actions">
        <g:if test="${post.campaignStatusRSDTO == CampaignStatusRSDTO.SENT}">
            <li>
                <g:link mapping="politicianPostStatsShow" params="[postId: post.id]" role="button" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats</span></g:link>
            </li>
        </g:if>
        <li>
            <g:set var="modal" value="${post.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ?'modalEditScheduled':''}"/>
            <g:link mapping="postEditContent" params="${post.encodeAsLinkProperties()}" role="button" class="postEdit ${modal}"><span class="fa fa-edit"></span><span class="sr-only">Edit</span></g:link>
        </li>
        <li>
            <g:link mapping="postRemove" params="${post.encodeAsLinkProperties()}"  role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></g:link>
        </li>
    </ul>
</li>
