<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, newsletter" />
<g:set var="contentType" value="${contentType}" />
<div class="box-steps container-fluid campaign-steps">
    <g:set var="mappings" value="${[step:'content',
                                    saveAndSentButtons:true,
                                    settings:'politicianMassMailingSettings',
                                    template:'politicianMassMailingTemplate',
                                    content:'politicianMassMailingContent',
                                    showResult: 'politicianCampaigns',
                                    next: 'politicianMassMailingContent']}"/>
    <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only">Newsletter</h1>

        <g:if test="${contentType==org.kuorum.rest.model.notification.campaign.NewsletterTemplateDTO.HTML}">
            <g:render template="types/contentType/customHTML" model="[command: command, filters: filters, totalContacts: totalContacts, campaign: campaign, anonymousFilter: anonymousFilter, mappings: mappings]"></g:render>
        </g:if>
        <g:elseif test="${contentType==org.kuorum.rest.model.notification.campaign.NewsletterTemplateDTO.PLAIN_TEXT}">
            <g:render template="types/contentType/plainText" model="[command: command, filters: filters, totalContacts: totalContacts, campaign: campaign, anonymousFilter: anonymousFilter,  mappings: mappings]"></g:render>
        </g:elseif>
        <g:else>
            <g:render template="types/contentType/simpleTemplate" model="[command: command, filters: filters, totalContacts: totalContacts, campaign: campaign, anonymousFilter: anonymousFilter,  mappings: mappings]"></g:render>
        </g:else>
</div>
