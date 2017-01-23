<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>

<g:set var="status" value="${(debate.campaignStatusRSDTO == CampaignStatusRSDTO.SENT) ? CampaignStatusRSDTO.SENT : CampaignStatusRSDTO.DRAFT}"/>
<li class="${status}" id="campaignPos_${idx}">
    <span class="id sr-only">${debate.id}</span>
    <span class="state">${debate.campaignStatusRSDTO}</span>
    <h3>
        <g:link mapping="debateShow" params="[debateId: debate.id]" class="title">
            ${debate.title}<span></span>
        </g:link>
    </h3>
    <p class="name">
        <g:message code="${CampaignStatusRSDTO.class.name}.${debate.campaignStatusRSDTO}"/>
        <input class="timestamp" type="hidden" value="${debate?.publishOn?.time?:'LAST'}" />  %{-- Letters are after than numbers--}%
        <g:if test="${debate.publishOn}">
            <span class="date">
                (<g:formatDate date="${debate.publishOn}" type="datetime" style="LONG" timeStyle="SHORT" timeZone="${user.timeZone}"/>)
            </span>
            - ${debate.anonymousFilter?.name?:g.message(code:'tools.massMailing.fields.filter.to.all')}
        </g:if>
    </p>
    <ul>
        <li class="recipients">
            <span class="recip-number">0</span>
            <g:message code="tools.massMailing.list.recipients"/>

        </li>
        <li class="open">
            <span class='open-number'>0</span>
            <g:message code="tools.massMailing.list.opens"/>
        </li>
        <li class="click">
            <span class='click-number'>0</span>
            <g:message code="tools.massMailing.list.click"/>
        </li>
    </ul>
    <g:if test="${debate.campaignStatusRSDTO == CampaignStatusRSDTO.SENT}">
        <g:link mapping="debateShow" params="[debateId: debate.id]" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats</span></g:link>
    </g:if>
    <g:else>
        <g:link mapping="debateEdit" params="[debateId: debate.id]" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit</span></g:link>

        %{-- This delete function is not implemented --}%
        <g:link mapping="debateRemove" params="[debateId: debate.id]"  role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></g:link>
    </g:else>
</li>
