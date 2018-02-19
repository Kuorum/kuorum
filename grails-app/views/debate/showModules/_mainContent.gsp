
<g:render template="/debate/showModules/mainContent/debateData" model="[debate: debate, debateUser: debateUser, proposalPage: proposalPage, poweredByKuorum:poweredByKuorum?:false]" />
<g:if test="${debate.campaignStatusRSDTO == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
    <g:render template="/debate/showModules/proposalSection" model="[debate: debate, debateUser: debateUser, proposalPage: proposalPage]" />
</g:if>

<g:render template="/campaigns/showModules/campingModalEditScheduled"/>