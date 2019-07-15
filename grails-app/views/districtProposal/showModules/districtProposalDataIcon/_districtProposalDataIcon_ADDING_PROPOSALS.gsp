<g:if test="${districtProposal.activeSupport}">
<g:render
        template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIconGeneric"
        model="[
                districtProposal: districtProposal,
                iconClass:'fa-rocket',
                iconNumber:districtProposal.numSupports,
                isActive:districtProposal.isSupported,
                callButtonActionClass:'districtProposal-support',
                callButtonAction:g.createLink(mapping:'participatoryBudgetDistrictProposalSupport', params:districtProposal.encodeAsLinkProperties())
        ]" />
</g:if>