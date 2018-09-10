<g:render
        template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIconGeneric"
        model="[
                districtProposal: districtProposal,
                iconClass:'fa-shopping-cart',
                iconNumber:districtProposal.numVotes,
                isActive:districtProposal.isVoted,
                callButtonActionClass:'disabled',
                callButtonAction:g.createLink(mapping:'participatoryBudgetDistrictProposalVote', params:districtProposal.encodeAsLinkProperties())
        ]" />