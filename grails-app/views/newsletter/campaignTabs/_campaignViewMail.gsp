<h2 class="sr-only"><g:message code="tools.massMailing.view.viewMail"/></h2>
<g:set var="mailCampaign" value="${g.createLink(mapping: 'politicianMassMailingHtml', params: [campaignId:campaign.id], absolute: true)}"/>
<iframe id="campaignEmailContainer" src="${mailCampaign}">
    iframe para cargar el html del email
</iframe>
