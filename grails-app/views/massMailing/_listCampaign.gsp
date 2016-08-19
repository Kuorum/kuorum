<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>

<li class="${campaign.status}" id="${idx}">
    <span class="state">${campaign.status}</span>
    <h3 class="title"><g:link mapping="politicianMassMailingShow" params="[campaignId:campaign.id]">${campaign.name}</g:link></h3>
    <p class="name">
        <g:message code="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.class.name}.${campaign.status}"/>
        <span class="date"><input class="timestamp" type="hidden" val="${campaign?.sentOn?.time?:''}" />
            (<g:formatDate date="${campaign.sentOn}" type="datetime" style="LONG" timeStyle="SHORT"/>)
        </span>
        - ${campaign.filter?.name?:g.message(code:'tools.massMailing.fields.filter.to.all')}
    </p>
    <ul>
        <li class="recipients"><span class="recip-number">${campaign.numberRecipients}</span> <g:message code="tools.massMailing.list.recipients"/> </li>
        <li class="open"><span class="open-number">${campaign.numberOpens/campaign.numberRecipients*100}</span> <g:message code="tools.massMailing.list.opens"/></li>
        <li class="click"><span class="click-number">${campaign.numberClicks/campaign.numberRecipients*100}</span> <g:message code="tools.massMailing.list.click"/></li>
    </ul>
    <g:link mapping="politicianMassMailingShow" params="[campaignId:campaign.id]" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit</span></g:link>
    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
</li>