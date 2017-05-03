<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>

<li class="${post.newsletter.status} postItem" id="campaignPos_${idx}">
    <span class="id sr-only">${post.id}</span>
    <span class="state">${post.newsletter.status}</span>
    <span class="type">post</span>
    <span class="fa fa-newspaper-o"></span>
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
                <g:link mapping="politicianMassMailingShow" params="[campaignId: post.newsletter.id]" role="button" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats</span></g:link>
            </li>
        </g:if>
        <li>
            <g:link mapping="postEditSettings" params="[postId: post.id]" role="button" class="postEdit"><span class="fa fa-edit"></span><span class="sr-only">Edit</span></g:link>
        </li>
    </ul>

    %{-- This delete function is not implemented --}%
    %{--<g:link mapping="debateRemove" params="[debateId: debate.id]"  role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></g:link>--}%
</li>
