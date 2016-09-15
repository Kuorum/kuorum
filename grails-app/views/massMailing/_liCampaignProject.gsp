<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>

<g:set var="status" value="${project.published?org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT:org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.DRAFT}"/>
<li class="${status}" id="campaignPos_${idx}">
    <span class="id sr-only">${project.id}</span>
    <span class="state">${status}</span>
    <h3>
        <g:link mapping="projectShow" params="${project.encodeAsLinkProperties()}" class="title">
            ${project.shortName}<span></span>
        </g:link>
    </h3>
    <p class="name">
        <g:message code="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.class.name}.${status}"/>
        <input class="timestamp" type="hidden" val="${project.deadline?.time?:'LAST'}" />  %{-- Letters are after than numbers--}%
        <g:if test="${project.deadline}">
            <span class="date">
                (<g:formatDate date="${project.deadline}" type="datetime" style="LONG" timeStyle="SHORT" timeZone="${user.timeZone}"/>)
            </span>
        </g:if>
    </p>
    <ul>
        <li class="recipients">
            <span class="recip-number">${project.peopleVotes?.total}</span>
            <g:message code="project.stats.total.numVotes.total"/>

        </li>
        <li class="open">
            <span class='open-number'><g:formatNumber number="${project.peopleVotes?.total?(project.peopleVotes?.yes?:0/project.peopleVotes.total)*100:0}" type="number"/></span>
            <g:message code="project.stats.columnC.vote.yes"/>
        </li>
        <li class="click">
            <span class='click-number'><g:formatNumber number="${project.peopleVotes?.total?(project.peopleVotes?.no?:0/project.peopleVotes.total)*100:0}" type="number"/></span>
            <g:message code="project.stats.columnC.vote.no"/>
        </li>
    </ul>

    <g:link mapping="projectEdit" params="${project.encodeAsLinkProperties()}" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit</span></g:link>
</li>
