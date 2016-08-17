<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>

<li class="${campaign.status}" id="${idx}">
    <span class="state">${campaign.status}</span>
    <h3 class="title"><g:link mapping="politicianMassMailingEdit" params="[campaignId:campaign.id]">${campaign.name}</g:link></h3>
    <p class="name">
        <g:message code="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.class.name}.${campaign.status}"/>
        <span class="date"><input class="timestamp" type="hidden" val="${campaign?.sentOn?.time?:''}" /><g:formatDate date="${campaign.sentOn}"/></span>
        - ${campaign.filter?.name?:'All contacts'}
    </p>
    <ul>
        <li class="recipients"><span class="recip-number"></span> recipients</li>
        <li class="open"><span class="open-number"></span> open</li>
        <li class="click"><span class="click-number"></span> click</li>
    </ul>
    <g:link mapping="politicianMassMailingEdit" params="[campaignId:campaign.id]" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit</span></g:link>
    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
</li>