<g:render
        template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIconGeneric"
        model="[
                districtProposal: districtProposal,
                iconClass:'fa-shopping-cart',
                iconNumber:districtProposal.numVotes,
                isActive:districtProposal.isVoted,
                callButtonActionClass:'districtProposal-vote',
                callButtonAction:g.createLink(mapping:'participatoryBudgetDistrictProposalVote', params:districtProposal.encodeAsLinkProperties())
        ]" />