<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>
<g:set var="type" value="${campaign.campaignType}"/>
<g:set var="urlMappingNameEditStep" value="${campaign.campaignType.toString().toLowerCase() + 'EditContent'}"/>
<g:set var="urlMappingNameShow" value="${campaign.campaignType.toString().toLowerCase() + 'Show'}"/>
<g:set var="urlMappingNameRemove" value="${campaign.campaignType.toString().toLowerCase() + 'Remove'}"/>
<g:set var="urlMappingNameCopy" value="${campaign.campaignType.toString().toLowerCase() + 'Copy'}"/>
<g:set var="faIcon" value="fa-newspaper"/>
<g:set var="hidePause"
       value="${campaign.campaignStatusRSDTO != org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT && campaign.campaignStatusRSDTO != org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PAUSE}"/>
<g:set var="hideCopy" value="${false}"/>
<g:if test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.DEBATE}">
    <g:set var="faIcon" value="fa-comments"/>
</g:if>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.SURVEY}">
    <g:set var="urlMappingNameEditStep" value="surveyEditQuestions"/>
    <g:set var="faIcon" value="fa-chart-bar"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.PARTICIPATORY_BUDGET}">
    <g:set var="urlMappingNameEditStep" value="participatoryBudgetEditContent"/>
    <g:set var="urlMappingNameShow" value="participatoryBudgetShow"/>
    <g:set var="urlMappingNameRemove" value="participatoryBudgetRemove"/>
    <g:set var="faIcon" value="fa-money-bill-alt"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.DISTRICT_PROPOSAL}">
    <g:set var="urlMappingNameEditStep" value="districtProposalEditContent"/>
    <g:set var="urlMappingNameShow" value="districtProposalShow"/>
    <g:set var="urlMappingNameRemove" value="districtProposalRemove"/>
    <g:set var="faIcon" value="fa-rocket"/>
    <g:set var="hideEdit" value="${!campaign.editable}"/>
    <g:set var="hideRemove"
           value="${!(campaign.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS || campaign.newsletter.status == CampaignStatusRSDTO.DRAFT)}"/>
    <g:set var="hidePause" value="${true}"/>
    <g:set var="hideCopy" value="${true}"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.PETITION}">
    <g:set var="faIcon" value="fa-microphone"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.CONTEST}">
    <g:set var="faIcon" value="fa-trophy"/>
</g:elseif>
<g:elseif test="${campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.CONTEST_APPLICATION}">
    <g:set var="urlMappingNameEditStep" value="contestApplicationEditContent"/>
    <g:set var="urlMappingNameShow" value="contestApplicationShow"/>
    <g:set var="urlMappingNameRemove" value="contestApplicationRemove"/>
    <g:set var="urlMappingNameCopy" value="contestApplicationCopy"/>
    <g:set var="faIcon" value="far fa-scroll"/>
    <g:set var="hideEdit" value="${!campaign.editable}"/>
    <g:set var="hidePause" value="${true}"/>
    <g:set var="hideRemove" value="${!campaign.editable}"/>
    <g:set var="hideCopy" value="${true}"/>
</g:elseif>
<g:set var="typeName" value="${g.message(code: 'tools.campaign.new.' + type)}"/>
<g:set var="campaignGenericMappings"
       value="[show: urlMappingNameShow, edit: urlMappingNameEditStep, remove: urlMappingNameRemove, copy: urlMappingNameCopy]"/>
<g:if test="${campaign.event}">
    <g:set var="type" value="EVENT"/>
    <g:set var="faIcon" value="fa-calendar-star"/>
    <g:set var="typeName" value="${g.message(code: 'tools.campaign.new.EVENT')}"/>
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
        <input class="timestamp" type="hidden"
               val="${campaign?.datePublished?.time ?: 'LAST'}"/>  %{-- Letters are after than numbers--}%
        <g:if test="${campaign.datePublished}">
            <span class="date">
                (<g:formatDate date="${campaign.datePublished}" type="datetime" style="LONG" timeStyle="SHORT"
                               timeZone="${user.timeZone}"/>)
            </span>
            <g:if test="${campaign.anonymousFilter || campaign.getCampaignType() == org.kuorum.rest.model.communication.CampaignTypeRSDTO.BULLETIN}">
                :: ${campaign.anonymousFilter?.name ?: g.message(code: 'tools.massMailing.fields.filter.to.all')}
            </g:if>
        </g:if>
    </p>
<g:if test="${campaign.getCampaignType() == org.kuorum.rest.model.communication.CampaignTypeRSDTO.BULLETIN}">
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
</g:if>
    <ul class="list-actions">
        <g:if test="${campaign.getCampaignType() == org.kuorum.rest.model.communication.CampaignTypeRSDTO.SURVEY}">
            <li>
                <g:link mapping="surveySummoning" params="${campaign.encodeAsLinkProperties()}"
                        class="summoing-call"><span class="fal fa-envelope" aria-hidden="true"></span><span
                        class="sr-only">Summoning button</span></g:link>
            </li>
        </g:if>

        <g:if test="${!hidePause}">
            <li>
                <g:link
                        mapping="campaignPause"
                        params="${campaign.encodeAsLinkProperties()}"
                        role="button"
                        class="campaignPause"
                        data-text-sent="${g.message(code: 'org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT')}"
                        data-text-paused="${g.message(code: 'org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PAUSE')}">
                    <span class="fal ${campaign.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT ? 'fa-pause-circle' : 'fa-play-circle'}"></span>
                    <span class="sr-only">Pause</span>
                </g:link>
            </li>
        </g:if>
        <g:if test="${campaign.campaignStatusRSDTO == CampaignStatusRSDTO.SENT || campaign.campaignStatusRSDTO == CampaignStatusRSDTO.PAUSE}">
            <li>
                <g:link mapping="politicianCampaignStatsShow" params="[campaignId: campaign.id]" role="button"
                        class="campaignStats"><span class="fal fa-chart-line"></span> <span
                        class="sr-only">Stats</span></g:link>
            </li>
        </g:if>
        <g:if test="${!hideEdit}">
            <li>
                <g:set var="modal"
                       value="${campaign.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ? 'modalEditScheduled' : ''}"/>
                <g:link mapping="${campaignGenericMappings.edit}" params="${campaign.encodeAsLinkProperties()}"
                        role="button" class="campaignEdit ${modal}"><span class="fal fa-edit"></span><span
                        class="sr-only">Edit</span></g:link>
            </li>
        </g:if>
        <g:if test="${!hideCopy}">
            <li>
                <g:link mapping="${campaignGenericMappings.copy}" params="[campaignId: campaign.id]"
                        class="campaignStats"><span class="fal fa-copy"></span> <span
                        class="sr-only">Copy</span></g:link>
            </li>
        </g:if>
        <g:if test="${!hideRemove}">
            <li>
                <g:link mapping="${campaignGenericMappings.remove}" params="${campaign.encodeAsLinkProperties()}"
                        role="button" class="campaignDelete"><span class="fal fa-trash"></span> <span
                        class="sr-only">Delete</span></g:link>
            </li>
        </g:if>
    </ul>
</li>
