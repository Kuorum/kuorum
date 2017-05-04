<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>

<li class="${debate.newsletter.status} debateItem" id="campaignPos_${idx}">
    <span class="id sr-only">${debate.id}</span>
    <span class="state">${debate.campaignStatusRSDTO}</span>
    <span class="type">debate</span>
    <span class="fa fa-comments-o"></span>
    <h3>
        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" class="title">
            ${debate.name}<span></span>
        </g:link>
    </h3>
    <p class="name">
        <g:message code="${CampaignStatusRSDTO.class.name}.${debate.campaignStatusRSDTO}"/>
        <input class="timestamp" type="hidden" val="${debate?.datePublished?.time?:'LAST'}" />  %{-- Letters are after than numbers--}%
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
                <g:link mapping="politicianMassMailingShow" params="[campaignId: debate.newsletter.id]" role="button" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats</span></g:link>
            </li>
        </g:if>
        <li>
            <g:link mapping="debateEdit" params="[debateId: debate.id]" role="button" class="campaignEdit"><span class="fa fa-edit"></span><span class="sr-only">Edit</span></g:link>
        </li>
    </ul>

    %{-- This delete function is not implemented --}%
    %{--<g:link mapping="debateRemove" params="[debateId: debate.id]"  role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></g:link>--}%
</li>
