<g:render
        template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIconGeneric"
        model="[
                districtProposal: districtProposal,
                iconClass:'fa-rocket',
                iconNumber:districtProposal.numSupports,
                isActive:districtProposal.isSupported,
                callButtonActionClass:'disabled',
                callButtonAction:g.createLink(mapping:'participatoryBudgetDistrictProposalSupport', params:districtProposal.encodeAsLinkProperties())
        ]" />