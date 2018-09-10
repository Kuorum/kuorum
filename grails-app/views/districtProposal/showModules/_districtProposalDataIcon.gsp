<g:if test="${districtProposal.published}">
    <g:render template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIcon_${districtProposal.participatoryBudget.status}" model="[districtProposal:districtProposal]"/>
</g:if>
