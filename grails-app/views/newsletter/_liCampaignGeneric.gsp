<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>
<g:set var="type" value="post"/>
<g:set var="urlMappingNameEditStep" value="EditContent"/>
<g:set var="faIcon" value="fa-newspaper"/>
<g:set var="hidePause" value="${campaign.campaignStatusRSDTO != org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT && campaign.campaignStatusRSDTO != org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PAUSE}"/>
<g:if test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.DEBATE}">
    <g:set var="type" value="debate"/>
    <g:set var="faIcon" value="fa-comments"/>
</g:if>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.SURVEY}">
    <g:set var="urlMappingNameEditStep" value="EditQuestions"/>
    <g:set var="type" value="survey"/>
    <g:set var="faIcon" value="fa-chart-bar"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.PARTICIPATORY_BUDGET}">
    <g:set var="urlMappingNameEditStep" value="EditDistricts"/>
    <g:set var="type" value="participatoryBudget"/>
    <g:set var="faIcon" value="fa-money-bill-alt"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.DISTRICT_PROPOSAL}">
    <g:set var="urlMappingNameEditStep" value="EditContent"/>
    <g:set var="type" value="districtProposal"/>
    <g:set var="faIcon" value="fa-rocket"/>
    <g:set var="hideEdit" value="${campaign.participatoryBudget.status != org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS}"/>
    <g:set var="hidePause" value="${campaign.participatoryBudget.status != org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS}"/>
    <g:set var="hideRemove" value="${!(campaign.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS || campaign.newsletter.status == CampaignStatusRSDTO.DRAFT)}"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.PETITION}">
    <g:set var="urlMappingNameEditStep" value="EditContent"/>
    <g:set var="type" value="petition"/>
    <g:set var="faIcon" value="fa-microphone"/>
</g:elseif>
<g:set var="typeName" value="${g.message(code: 'tools.campaign.new.'+type)}"/>
<g:set var="campaignGenericMappings" value="[show:type+'Show', edit:type+urlMappingNameEditStep, remove:type+'Remove']"/>
<g:if test="${campaign.event}">
    <g:set var="type" value="event"/>
    <g:set var="faIcon" value="fa-calendar-check"/>
    <g:set var="typeName" value="${g.message(code: 'tools.campaign.new.event')}"/>
</g:if>


<li class="${campaign.newsletter.status}" id="campaignPos_${idx}">
    <span class="id sr-only">${campaign.id}</span>
    <span class="state" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${g.message(code: "org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.${campaign.campaignStatusRSDTO}")}">
        ${campaign.campaignStatusRSDTO}
    </span>
    <span class="type">${type}</span>
    <span class="fal ${faIcon}" aria-hidden="true" rel="tooltip" data-toggle="tooltip" data-placement="bottom"
          data-original-title="${typeName}"></span>
    <h3>
        <g:link mapping="${campaignGenericMappings.show}" params="${campaign.encodeAsLinkProperties()}" class="title">
            ${campaign.name}<span></span>
        </g:link>
    </h3>
    <p class="name">
        <span class="state"><g:message code="${CampaignStatusRSDTO.class.name}.${campaign.campaignStatusRSDTO}"/></span>
        <input class="timestamp" type="hidden" val="${campaign?.datePublished?.time?:'LAST'}" />  %{-- Letters are after than numbers--}%
        <g:if test="${campaign.datePublished}">
            <span class="date">
                (<g:formatDate date="${campaign.datePublished}" type="datetime" style="LONG" timeStyle="SHORT" timeZone="${user.timeZone}"/>)
            </span>
            :: ${campaign.anonymousFilter?.name?:g.message(code:'tools.massMailing.fields.filter.to.all')}
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
        <g:if test="${campaign.campaignStatusRSDTO == CampaignStatusRSDTO.SENT || campaign.campaignStatusRSDTO == CampaignStatusRSDTO.PAUSE}">
            <li>
                <g:link mapping="politicianCampaignStatsShow" params="[campaignId: campaign.id]" role="button" class="campaignStats"><span class="fal fa-chart-line"></span> <span class="sr-only">Stats</span></g:link>
            </li>
        </g:if>
        <g:if test="${!hideEdit}">
            <li>
                <g:set var="modal" value="${campaign.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ?'modalEditScheduled':''}"/>
                <g:link mapping="${campaignGenericMappings.edit}" params="${campaign.encodeAsLinkProperties()}" role="button" class="campaignEdit ${modal}"><span class="fal fa-edit"></span><span class="sr-only">Edit</span></g:link>
            </li>
        </g:if>
        <g:if test="${!hideRemove}">
            <li>
                <g:link mapping="${campaignGenericMappings.remove}" params="${campaign.encodeAsLinkProperties()}"  role="button" class="campaignDelete"><span class="fal fa-trash"></span> <span class="sr-only">Delete</span></g:link>
            </li>
        </g:if>
        <g:if test="${!hidePause}">
            <li>
                <g:link
                        mapping="campaignPause"
                        params="${campaign.encodeAsLinkProperties()}"
                        role="button"
                        class="campaignPause"
                        data-text-sent="${g.message(code:'org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT')}"
                        data-text-paused="${g.message(code:'org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PAUSE')}">
                    <span class="fal ${campaign.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT?'fa-pause-circle':'fa-play-circle'}"></span>
                    <span class="sr-only">Pause</span>
                </g:link>
            </li>
        </g:if>
    </ul>
</li>
