
<g:render template="/debate/showModules/mainContent/debateData" model="[debate: debate, debateUser: debateUser, proposalPage: proposalPage, poweredByKuorum:poweredByKuorum?:false]" />
<g:if test="${debate.published}">
    <g:render template="/debate/showModules/proposalSection" model="[debate: debate, debateUser: debateUser, proposalPage: proposalPage]" />
</g:if>

<g:render template="/campaigns/showModules/campingModalEditScheduled"/>