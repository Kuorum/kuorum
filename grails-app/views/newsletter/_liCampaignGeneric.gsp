<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>
<g:set var="type" value="post"/>
<g:set var="urlMappingNameEditStep" value="EditContent"/>
<g:set var="faIcon" value="fa-newspaper-o"/>
<g:if test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.DEBATE}">
    <g:set var="type" value="debate"/>
    <g:set var="faIcon" value="fa-comments-o"/>
</g:if>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.SURVEY}">
    <g:set var="urlMappingNameEditStep" value="EditQuestions"/>
    <g:set var="type" value="survey"/>
    <g:set var="faIcon" value="fa-bar-chart-o"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.PARTICIPATORY_BUDGET}">
    <g:set var="urlMappingNameEditStep" value="EditDistricts"/>
    <g:set var="type" value="participatoryBudget"/>
    <g:set var="faIcon" value="fa-money"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.DISTRICT_PROPOSAL}">
    <g:set var="urlMappingNameEditStep" value="EditContent"/>
    <g:set var="type" value="districtProposal"/>
    <g:set var="faIcon" value="fa-rocket"/>
</g:elseif>
<g:set var="typeName" value="${g.message(code: 'tools.campaign.new.'+type)}"/>
<g:set var="campaignGenericMappings" value="[show:type+'Show', edit:type+urlMappingNameEditStep, remove:type+'Remove']"/>
<g:if test="${campaign.event}">
    <g:set var="type" value="event"/>
    <g:set var="faIcon" value="fa-calendar-check-o"/>
    <g:set var="typeName" value="${g.message(code: 'tools.campaign.new.event')}"/>
</g:if>


<li class="${campaign.newsletter.status}" id="campaignPos_${idx}">
    <span class="id sr-only">${campaign.id}</span>
    <span class="state" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${g.message(code: "org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.${campaign.campaignStatusRSDTO}")}">
        ${campaign.campaignStatusRSDTO}
    </span>
    <span class="type">${type}</span>
    <span class="fa ${faIcon}" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${typeName}"></span>
    <h3>
        <g:link mapping="${campaignGenericMappings.show}" params="${campaign.encodeAsLinkProperties()}" class="title">
            ${campaign.name}<span></span>
        </g:link>
    </h3>
    <p class="name">
        <g:message code="${CampaignStatusRSDTO.class.name}.${campaign.campaignStatusRSDTO}"/>
        <input class="timestamp" type="hidden" val="${campaign?.datePublished?.time?:'LAST'}" />  %{-- Letters are after than numbers--}%
        <g:if test="${campaign.datePublished}">
            <span class="date">
                (<g:formatDate date="${campaign.datePublished}" type="datetime" style="LONG" timeStyle="SHORT" timeZone="${user.timeZone}"/>)
            </span>
            - ${campaign.anonymousFilter?.name?:g.message(code:'tools.massMailing.fields.filter.to.all')}
        </g:if>
    </p>
    <ul class="list-campaign-stats">
        <li class="recipients">
            <span class="recip-number"><newsletterUtil:campaignsSent campaign="${campaign.newsletter}"/></span>
            <g:message code="tools.massMailing.list.recipients"/>
        </li>
        <li class="open">
            <newsletterUtil:openRate campaign="${campaign.newsletter}"/>
            <g:message code="tools.massMailing.list.opens"/>
        </li>
        <li class="click">
            <newsletterUtil:clickRate campaign="${campaign.newsletter}"/>
            <g:message code="tools.massMailing.list.click"/>
        </li>
    </ul>
    <ul class="list-actions">
        <g:if test="${campaign.campaignStatusRSDTO == CampaignStatusRSDTO.SENT}">
            <li>
                <g:link mapping="politicianCampaignStatsShow" params="[campaignId: campaign.id]" role="button" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats</span></g:link>
            </li>
        </g:if>
        <li>
            <g:set var="modal" value="${campaign.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ?'modalEditScheduled':''}"/>
            <g:link mapping="${campaignGenericMappings.edit}" params="${campaign.encodeAsLinkProperties()}" role="button" class="campaignEdit ${modal}"><span class="fa fa-edit"></span><span class="sr-only">Edit</span></g:link>
        </li>
        <li>
            <g:link mapping="${campaignGenericMappings.remove}" params="${campaign.encodeAsLinkProperties()}"  role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></g:link>
        </li>
    </ul>
</li>
